package org.example.gym_shop_2026.services;

import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.UserType;
import org.example.gym_shop_2026.persistence.BasketDAO;
import org.example.gym_shop_2026.persistence.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.SQLException;

@Service
public class UserService {
    private final UserDAO userDao;
    private final BasketDAO basketDAO;

    public UserService(UserDAO userDao, BasketDAO basketDAO){
        this.userDao = userDao;
        this.basketDAO = basketDAO;
    }

    //Functionality Methods

    public boolean loginUser(String username, String password) throws SQLException {
        return userDao.login(username, password);
    }

    @Transactional
    public boolean registerUser(String uName, String fName, UserType type, String eMail, String pWord,
                                Date dob, String address, String eircode) throws SQLException {

        int newUserId = userDao.register(uName, fName, type, eMail, pWord, dob, address, eircode);

        if (newUserId != -1) {
            return basketDAO.createBasket(newUserId);
        }

        return false;
    }

    public User findUser (String toBeFound) throws SQLException{
        return userDao.findByUsername(toBeFound);
    }

    public boolean updateStripeCustomerId(int userId, String stripeId) throws SQLException {
        return userDao.updateStripeId(userId, stripeId);
    }

    public User getUserById(int userId) throws SQLException {
        return userDao.findUserByID(userId);
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
