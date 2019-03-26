package com.epam.esm.configuration;

import com.epam.esm.connection.ConnectionPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
@ComponentScan(basePackages = {"com.epam.esm.connection", "com.epam.esm.repository"})
public class RepositoryConfiguration {

    private static final String URL = "url";
    private static final String DB_USER = "dbuser";
    private static final String DB_PASSWORD = "dbpassword";
    private static final String POOL_SIZE = "poolSize";

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public com.epam.esm.connection.ConnectionPoolConfig connectionPoolConfig(Environment environment) {
        String url = environment.getRequiredProperty(URL);
        String user = environment.getRequiredProperty(DB_USER);
        String password = environment.getRequiredProperty(DB_PASSWORD);
        int poolSize = environment.getRequiredProperty(POOL_SIZE, Integer.class);
        return new ConnectionPoolConfig(url, poolSize, user, password);
    }
}
