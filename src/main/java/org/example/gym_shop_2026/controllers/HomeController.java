package org.example.gym_shop_2026.controllers;

import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
public class HomeController {
    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) throws SQLException {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }
}
