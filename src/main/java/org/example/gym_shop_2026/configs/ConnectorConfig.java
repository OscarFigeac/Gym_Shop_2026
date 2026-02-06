package org.example.gym_shop_2026.configs;

import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.connector.MySqlConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * A Spring Boot {@link Configuration} class that contains a {@link org.springframework.context.annotation.Bean} method
 * to construct a {@link org.example.gym_shop_2026.connector.Connector} implementation.
 *
 * @author Cal Woods
 */
@Configuration
public class ConnectorConfig {
    @Autowired
    Environment env;

    /**
     * A Spring Boot {@link Bean} method that returns a constructed {@link Connector}
     * implementation {@link MySqlConnector}.
     *
     * @return {@link MySqlConnector} instance
     */
    @Bean
    public Connector connector() {
        Connector connector = new MySqlConnector(env);

        return connector;
    }
}
