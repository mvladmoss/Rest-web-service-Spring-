package com.epam.esm.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.service", "com.epam.esm.validation",
        "com.epam.esm.converter"})
public class ServiceConfiguration {
}
