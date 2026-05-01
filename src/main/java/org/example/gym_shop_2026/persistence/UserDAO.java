package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.UserType;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    boolean login(String username, String password) throws SQLException;
    public int register(String uName, String fName, UserType type, String eMail, String pWord,
                            Date dob, String address, String eircode) throws SQLException;
    User findByUsername(String Username) throws SQLException;
    boolean createUser(User toBeCreated) throws SQLException;
    User findUserByID(int userId) throws SQLException;
    boolean updateUser(User toBeUpdated) throws SQLException;
    boolean deleteUser(User toBeDeleted) throws SQLException;
    public boolean updateStripeId(int userId, String stripeId) throws SQLException;
    public List<User> getAllUsers() throws SQLException;
}
