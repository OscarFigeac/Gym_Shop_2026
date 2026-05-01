package org.example.gym_shop_2026.controllers;

import org.example.gym_shop_2026.entities.Transaction;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.services.TransactionService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/dashboard/orders")
public class OrderController {

    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public OrderController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping
    public String viewMyOrders(Principal principal, Model model) throws SQLException {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findUser(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        List<Transaction> userTransactions = transactionService.getAllTransactionsForUser(user.getUser_id());

        model.addAttribute("transactions", userTransactions);

        return "orders";
    }
}