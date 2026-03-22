package org.example.gym_shop_2026.controllers;

import org.example.gym_shop_2026.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.SQLException;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(
            @RequestParam String username,
            @RequestParam String fName,
            @RequestParam String lName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String dob,
            @RequestParam String address,
            @RequestParam String eircode,
            @RequestParam String userType,
            Model model) {

        try {
            String fullName = fName + " " + lName;

            Date sqlDate = Date.valueOf(dob);

            boolean success = userService.registerUser(
                    username,
                    fullName,
                    userType,
                    email,
                    password,
                    sqlDate,
                    address,
                    eircode
            );

            if (success) {
                return "redirect:/login?success";
            } else {
                model.addAttribute("error", "Username already exists. Try another one!");
                return "register";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid date format or missing fields.");
            return "register";
        } catch (SQLException e) {
            model.addAttribute("error", "Database connection error. Try again later.");
            return "register";
        }
    }
}
