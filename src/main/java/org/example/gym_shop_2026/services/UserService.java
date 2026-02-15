package org.example.gym_shop_2026.services;

import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.persistence.UserDAO;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;

@Service
public class UserService {
    private final UserDAO userDao;

    public UserService(UserDAO userDao){
        this.userDao = userDao;
    }

    //Functionality Methods

    public boolean loginUser(String username, String password) throws SQLException {
        return userDao.login(username, password);
    }

    public boolean registerUser (String username, String fullName, String userType, String eMail, String password, Date DoB) throws SQLException{
        return userDao.register(username, fullName, userType, eMail, password, DoB);
    }

    public User findUser (String toBeFound) throws SQLException{
        return userDao.findByUsername(toBeFound);
    }

    //CRUD Methods

    public boolean createUser (User toBeCreated) throws SQLException{
        return userDao.createUser(toBeCreated);
    }
    public User readUser (int UserID) throws SQLException{
        return userDao.findUserByID(UserID);
    }
    public boolean updateUser (User toBeUpdated) throws SQLException{
        return userDao.updateUser(toBeUpdated);
    }
    public boolean deleteUser (User toBeDeleted) throws SQLException{
        return userDao.deleteUser(toBeDeleted);
    }
}
