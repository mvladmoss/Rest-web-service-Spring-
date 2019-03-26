package com.epam.esm.connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

class ProxyConnectionReturnerToPoolInvocationHandler implements InvocationHandler {

    private static String CLOSE_METHOD_NAME = "close";
    private static String IS_CLOSE_METHOD_NAME = "isClose";
    private Connection connection;
    private ConnectionPool connectionPool;

    ProxyConnectionReturnerToPoolInvocationHandler(ConnectionPool connectionPool, Connection connection) {
        this.connection = connection;
        this.connectionPool = connectionPool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals(CLOSE_METHOD_NAME)) {
            connectionPool.releaseConnection(connection);
            connection = null;
            return null;
        }
        if (method.getName().equals(IS_CLOSE_METHOD_NAME)) {
            return connection == null;
        }
        if (connection == null) {
            throw new SQLException("Attempt to use close connection");
        }
        try {
            return method.invoke(connection, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

}
