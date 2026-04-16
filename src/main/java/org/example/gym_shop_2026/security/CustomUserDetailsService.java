package org.example.gym_shop_2026.security;

import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.persistence.UserDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;
    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userDAO.findByUsername(username);
            if (user == null) throw new UsernameNotFoundException("User not found");

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getUserType())
                    .build();
        } catch (SQLException e) {
            throw new UsernameNotFoundException("Database error during login");
        }
    }
}