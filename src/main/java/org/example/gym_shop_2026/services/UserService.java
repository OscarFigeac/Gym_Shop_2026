package org.example.gym_shop_2026.services;

import org.example.gym_shop_2026.persistence.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDAO userDao;

    public UserService(UserDAO userDao){
        this.userDao = userDao;
    }


}
