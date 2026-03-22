package org.example.gym_shop_2026.controllers;


import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.example.gym_shop_2026.services.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@Controller
public class AdminController {

    public final AdminService aService;

    public AdminController(AdminService adminService){
        this.aService = adminService;
    }

    @GetMapping("/admin/dashboard")
    public String viewDashboard(Model model){
        try{
            var restock = aService.getReorderProducts();
            var sellers = aService.getBestSellers();

            model.addAttribute("restockProducts", (restock != null) ? restock : new ArrayList<>());
            model.addAttribute("bestSellers", (sellers != null) ? sellers : new ArrayList<>());

            return "AdminDashboard";
        }catch (SQLException e){
            log.error("Failed to load Admin Dashboard: {}", e.getMessage());
            model.addAttribute("errorMessage", "Could not load admin data.");
            return "Error";
        }
    }
}
