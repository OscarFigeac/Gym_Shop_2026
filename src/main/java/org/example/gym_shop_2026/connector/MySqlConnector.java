package org.example.gym_shop_2026.connector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Holds a {@link Connection} for use with an sql database.
 *
 * @implNote {@link Connector} interface
 * @author Cal Woods
 */
@Slf4j
public class MySqlConnector implements Connector {
    private Connection connection;

    /**
     * Instantiates a MySqlConnector object that stores a connection to an SQL database with {@link Environment}
     * values for connection details, ie, applications.properties file.
     *
     * @implNote Meant for Spring-use only.
     */
    public MySqlConnector(Environment env) {
        if(env == null) {
            log.error("Could not construct provided Environment env as it is null!");
            throw new NullPointerException("The Environment env is null");
        }

        //Store environment properties
        String url = env.getProperty("database.url");
        String database = env.getProperty("database.dbName");
        String user = env.getProperty("database.user");
        String password = env.getProperty("database.password");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + url + "/" + database, user, password);
            this.connection = conn;
        }
        catch (SQLException e) {
            log.error("Could not connect to database: {}", e.toString());
        }
    }

    /**
     * Instantiates a MySqlConnector object using given String values for: server, database name, username & password.
     * @param server Given server value
     * @param username Given username value
     * @param password Given password value
     * @param dbName Given database name value
     */
    public MySqlConnector(String server, String username, String password, String dbName) {
        try {
            this.connection = DriverManager.getConnection(server+dbName, username, password);
        }
        catch(SQLException e) {
            log.error("Could not connect to database: {}", e.toString());
        }
    }

    @Override
    public boolean freeConnection() {
        try {
            this.connection.close();
            return true;
        }
        catch (SQLException e) {
            log.error("Could not close connection {}", e.toString());
            return false;
        }
    }
}