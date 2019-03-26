package com.epam.esm.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class ConnectionDataSource extends AbstractDataSource {

    private final ConnectionPool connectionPool;

    @Autowired
    public ConnectionDataSource(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() {
        return connectionPool.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) {
        throw new UnsupportedOperationException();
    }

}
