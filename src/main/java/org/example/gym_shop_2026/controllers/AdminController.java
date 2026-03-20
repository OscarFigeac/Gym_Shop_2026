package org.example.gym_shop_2026.controllers;


import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.services.AdminService;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class AdminController {

    public final AdminService aService;

    public AdminController(AdminService adminService){
        this.aService = adminService;
    }
}
