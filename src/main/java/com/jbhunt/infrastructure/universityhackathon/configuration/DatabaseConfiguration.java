package com.jbhunt.infrastructure.universityhackathon.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {
        "com.jbhunt.infrastructure.universityhackathon.repository"
})
public class DatabaseConfiguration {

    @Value("${username}")
    private String userId;
    @Value("${password}")
    private String password;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .username(userId)
                .password(password)
                .build();
    }

}
