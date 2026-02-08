package org.example.gym_shop_2026.connector;

import java.sql.Connection;

public interface Connector {
    boolean freeConnection();
    Connection getConnection();

}
