package org.example.gym_shop_2026.controllers;


import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.services.AdminService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    public final AdminService aService;
    public final UserService uService;

    public AdminController(AdminService adminService, UserService userService){

        this.aService = adminService;
        this.uService = userService;
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model){
        try{
            var restock = aService.getReorderProducts();
            var sellers = aService.getBestSellers();

            var allUsers = uService.getAllUsers();

            model.addAttribute("restockProducts", (restock != null) ? restock : new ArrayList<>());
            model.addAttribute("bestSellers", (sellers != null) ? sellers : new ArrayList<>());
            model.addAttribute("users", allUsers);

            return "admindashboard";
        }catch (SQLException e){
            log.error("Failed to load Admin Dashboard: {}", e.getMessage());
            model.addAttribute("errorMessage", "Could not load admin data.");
            return "error";
        }
    }

    @PostMapping("/user/create")
    public String adminCreateUser (@ModelAttribute User user) throws SQLException {
        uService.createUser(user);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/user/update")
    public String adminUpdateUser (@ModelAttribute User user) throws SQLException {
        uService.updateUser(user);
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/user/delete/{id}")
    public String adminDeleteUser(@PathVariable int id) throws SQLException {
        User toBeDeleted = uService.readUser(id);
        if(toBeDeleted != null){
            uService.deleteUser(toBeDeleted);
        }
        return "redirect:/admin/dashboard";
    }
}
