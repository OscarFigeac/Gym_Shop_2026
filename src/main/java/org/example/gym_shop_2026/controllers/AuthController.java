package org.example.gym_shop_2026.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.UserType;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.SQLException;

@Slf4j
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

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        try {
            if(userService.loginUser(username, password)) {
                User user = userService.findUser(username);

                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUser_id());

                return "redirect:/"; // Redirect to homepage
            } else {
                return "redirect:/login?error=Invalid%20credentials";
            }
        } catch (SQLException ex) {
            return "redirect:/login?error=Connection%20error";
        }
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

            UserType typeEnum = UserType.valueOf(userType);

            boolean success = userService.registerUser(
                    username,
                    fullName,
                    typeEnum,
                    email,
                    password,
                    sqlDate,
                    address,
                    eircode
            );

            if (success) {
//                User newUser = userService.findUser(username);
//                session.setAttribute("user", newUser);
//                session.setAttribute("userId", newUser.getUser_id());
//
//                return "redirect:/dashboard";
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
