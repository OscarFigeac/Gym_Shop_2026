package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.User;

import java.sql.Date;
import java.sql.SQLException;

public interface UserDAO {
    boolean login(String username, String password) throws SQLException;
    boolean register(String username, String fullName, String userType, String eMail, String passWord, Date birthDay) throws SQLException;
    User findByUsername(String Username) throws SQLException;
    boolean createUser(User toBeCreated) throws SQLException;
    User findUserByID(int userId) throws SQLException;
    boolean updateUser(User toBeUpdated) throws SQLException;
    boolean deleteUser(User toBeDeleted) throws SQLException;
}
