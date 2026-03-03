package org.example.gym_shop_2026.configs;

import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.connector.MySqlConnector;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class ConnectorTestConfig {
    @Bean
    public Connector connector() {
        Connector connector = new MySqlConnector("localhost:3306", "root", "", "gymshopdbtest");

        return connector;
    }
}
