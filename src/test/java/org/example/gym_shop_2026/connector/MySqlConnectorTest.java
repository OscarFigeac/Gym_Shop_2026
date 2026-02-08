package org.example.gym_shop_2026.connector;

import org.example.gym_shop_2026.configs.ConnectorTestConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySqlConnectorTest {
    @Mock
    private Connector connector;

    @Test
    void freeConnectionReturnsTrue() throws SQLException {
        assertTrue(this.connector.freeConnection() == true);
    }

    @Test
    void getConnectionReturnsValidConnection() throws SQLException {
        Connection connection = this.connector.getConnection();

        assertNotNull(connection);
        assertTrue(connection.isValid(6));
    }
}