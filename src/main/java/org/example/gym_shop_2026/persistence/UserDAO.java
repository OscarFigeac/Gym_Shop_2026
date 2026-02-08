package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Users;

import java.sql.Date;
import java.sql.SQLException;

public interface UserDAO {
    boolean login(String username, String password) throws SQLException;
    boolean register(String username, String fullName, String userType, String eMail, String passWord, Date birthDay) throws SQLException;
    Users findByUsername(String Username) throws SQLException;
}
