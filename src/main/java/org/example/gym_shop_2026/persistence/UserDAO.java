package org.example.gym_shop_2026.persistence;

import java.sql.SQLException;

public interface UserDAO {
    boolean login(String username, String password) throws SQLException;
}
