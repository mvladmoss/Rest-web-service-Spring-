package com.epam.esm.connection;

import com.epam.esm.exception.ConnectionCloseException;
import com.epam.esm.exception.NotInitializedConnectionPoolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ConnectionPool {

    private static final String NOT_INITIALIZE_CONNECTION_POOL_EXCEPTION = "Unable to initialize connection pool";

    private static final int INITIAL_CAPACITY = 15;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionPool.class);
    private final ConnectionPoolConfig connectionPoolConfig;
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private ReentrantLock lock = new ReentrantLock();
    private ArrayBlockingQueue<Connection> freeConnections = new ArrayBlockingQueue<>(INITIAL_CAPACITY);
    private ArrayBlockingQueue<Connection> releaseConnections = new ArrayBlockingQueue<>(INITIAL_CAPACITY);

    @Autowired
    public ConnectionPool(ConnectionPoolConfig connectionPoolConfig) {
        this.connectionPoolConfig = connectionPoolConfig;
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException exception) {
            throw new NotInitializedConnectionPoolException(NOT_INITIALIZE_CONNECTION_POOL_EXCEPTION,exception);
        }
    }

    @PostConstruct
    public void init() {
        if (!initialized.get()) {
            try {
                lock.lock();
                if (!initialized.get()) {
                    String url = connectionPoolConfig.getUrl();
                    String user = connectionPoolConfig.getUserName();
                    String password = connectionPoolConfig.getUserPassword();
                    int initialCapacity = connectionPoolConfig.getPoolSize();
                    for (int i = 0; i < initialCapacity; i++) {
                        try {
                            Connection connection = DriverManager.getConnection(url, user, password);
                            freeConnections.add(connection);
                        } catch (SQLException exception) {
                            throw new NotInitializedConnectionPoolException(exception);
                        }
                    }
                    initialized.set(true);
                }
            } finally {
                lock.unlock();
            }
        }

    }

    Connection getConnection() {
        if (initialized.get()) {
            try {
                Connection connection = freeConnections.take();
                releaseConnections.offer(connection);
                return (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
                        new Class[]{Connection.class},
                        new ProxyConnectionReturnerToPoolInvocationHandler(this, connection));
            } catch (InterruptedException exception) {
                throw new NotInitializedConnectionPoolException(NOT_INITIALIZE_CONNECTION_POOL_EXCEPTION,exception);
            }
        } else {
            throw new NotInitializedConnectionPoolException(NOT_INITIALIZE_CONNECTION_POOL_EXCEPTION);
        }
    }

    void releaseConnection(Connection connection) {
        releaseConnections.remove(connection);
        freeConnections.offer(connection);
    }

    @PreDestroy
    public void destroy() {
        if (!initialized.get()) {
            closeConnections(freeConnections);
            closeConnections(releaseConnections);
        } else {
            throw new NotInitializedConnectionPoolException(NOT_INITIALIZE_CONNECTION_POOL_EXCEPTION);
        }
    }

    private void closeConnections(ArrayBlockingQueue<Connection> releaseConnections) {
        for (int i = 0; i < releaseConnections.size(); i++) {
            try {
                Connection connection = releaseConnections.take();
                connection.close();
            } catch (InterruptedException | SQLException e) {
                LOGGER.error("Unable to close connections", e);
                throw new ConnectionCloseException("Unable to close connections");
            }
        }
    }

}
