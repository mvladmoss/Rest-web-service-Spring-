package com.epam.esm.connection;


public class ConnectionPoolConfig {

    private String url;
    private int poolSize;
    private String userName;
    private String userPassword;

    public ConnectionPoolConfig(String url, int poolSize, String userName, String userPassword) {
        this.url = url;
        this.poolSize = poolSize;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
