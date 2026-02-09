package org.example.gym_shop_2026.connector;

import org.example.gym_shop_2026.configs.ConnectorTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = ConnectorTestConfig.class)
public class MySqlConnectorTest {
    @Autowired
    private Connector connector;

    @Test
    void getConnectionReturnsValidConnection() throws SQLException {
        assertNotNull(connector);
        assertNotNull(connector.getConnection());
        assertTrue(connector.getConnection().isValid(6));
    }

    @Test
    void freeConnectionReturnsTrueWithValidConnector() {
        assertNotNull(connector);
        assertNotNull(connector.getConnection());
        assertTrue(connector.freeConnection());
    }
}