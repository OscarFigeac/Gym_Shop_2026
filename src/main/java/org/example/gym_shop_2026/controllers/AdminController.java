package org.example.gym_shop_2026.controllers;


import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.entities.Transaction;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.services.AdminService;
import org.example.gym_shop_2026.services.ProductService;
import org.example.gym_shop_2026.services.TransactionService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    public final AdminService aService;
    public final UserService uService;
    public final ProductService pService;
    public final TransactionService tService;

    public AdminController(AdminService adminService, UserService userService, ProductService productService, TransactionService transactionService){

        this.aService = adminService;
        this.uService = userService;
        this.pService = productService;
        this.tService = transactionService;
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model){
        try{
            var restock = aService.getReorderProducts();
            System.out.println("DEBUG: Number of low stock products found: " + (restock != null ? restock.size() : "null"));
            var sellers = aService.getBestSellers();
            var allUsers = uService.getAllUsers();

            List<Product> allProducts = pService.getAllProducts();
            List<Transaction> allOrders = aService.getAllTransactions();

            log.info("Dashboard loaded. Products: {}, Users: {}, Orders: {}", allProducts.size(), allUsers.size(), allOrders.size());

            model.addAttribute("products", (allProducts != null) ? allProducts : new ArrayList<>());
            model.addAttribute("transactions", (allOrders != null) ? allOrders : new ArrayList<>());

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
    public String adminUpdateUser (@ModelAttribute User userToBeUpdated) throws SQLException {
        User existingUser = uService.readUser(userToBeUpdated.getUser_id());

        if(existingUser != null){
            existingUser.setEmail(userToBeUpdated.getEmail());
            existingUser.setUserType(userToBeUpdated.getUserType());

            uService.updateUser(existingUser);
        }

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
    @PostMapping("/product/create")
    public String adminAddProduct(@ModelAttribute Product product) throws SQLException {
        boolean success = pService.addProduct(product);
        if(!success) {
            log.error("Failed to insert product into database");
        }

        return "redirect:/admin/dashboard";
    }
    @PostMapping("/product/delete/{id}")
    public String adminDeleteProduct(@PathVariable int id) throws SQLException {
        Product toBeDeleted = pService.findProduct(id);
        if(toBeDeleted != null){
            pService.deleteProduct(toBeDeleted);
        }
        return "redirect:/admin/dashboard";
    }
    @PostMapping("/product/update")
    public String adminUpdateProduct (@ModelAttribute Product toBeUpdated) throws SQLException {
        Product stockedProduct = pService.findProduct(toBeUpdated.getProductId());

        if(stockedProduct != null){
            stockedProduct.setName(toBeUpdated.getName());
            stockedProduct.setDescription(toBeUpdated.getDescription());
            stockedProduct.setPrice(toBeUpdated.getPrice());
            stockedProduct.setQuantity(toBeUpdated.getQuantity());
            stockedProduct.setImageUrl(toBeUpdated.getImageUrl());
            stockedProduct.setProductCategory(toBeUpdated.getProductCategory());

            pService.updateProduct(stockedProduct);
        }

        return "redirect:/admin/dashboard";
    }
    @PostMapping("/orders/deliver/{id}")
    public String changeOrderStatus(@PathVariable int id) throws SQLException{
        Transaction t = tService.findTransactionByID(id);

        if(t != null){
            t.setStatus("DELIVERED");
            tService.updateTransaction(t);
        }

        return "redirect:/admin/dashboard";
    }
}
