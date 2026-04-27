package org.example.gym_shop_2026.controllers;

import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.sql.SQLException;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) throws SQLException {
        User user = userService.findUser(userDetails.getUsername());
        model.addAttribute("user", user); // Pass the whole object for easier access
        return "dashboard";
    }

    @GetMapping("/settings")
    public String showSettings(Model model, @AuthenticationPrincipal UserDetails userDetails) throws SQLException {
        User user = userService.findUser(userDetails.getUsername());
        model.addAttribute("user", user);
        return "profile-settings";
    }

    @PostMapping("/2fa/toggle")
    public String toggle2fa(@AuthenticationPrincipal UserDetails userDetails) throws SQLException{
        User user = userService.findUser(userDetails.getUsername());

        if (user.is2faEnabled()) {
            user.set2faEnabled(false);
            user.setSecretKey(null);
            userService.updateUser(user);
            return "redirect:/dashboard/settings?disabled";
        }

        return "redirect:/setup-2fa";
    }
}
