package com.epam.esm.config;

import com.epam.esm.configuration.RepositoryConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RepositoryConfiguration.class)
public class TestRepositoryConfiguration {

}
