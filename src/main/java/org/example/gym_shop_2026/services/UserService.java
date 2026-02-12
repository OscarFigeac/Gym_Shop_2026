package org.example.gym_shop_2026.services;

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

    public boolean loginUser(String username, String password) throws SQLException {
        return userDao.login(username, password);
    }

    public boolean register(String username, String fullName, String userType, String eMail, String password, Date DoB) throws SQLException{
        return userDao.register(username, fullName, userType, eMail, password, DoB);
    }


}
