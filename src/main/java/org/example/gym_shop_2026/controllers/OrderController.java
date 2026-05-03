package org.example.gym_shop_2026.controllers;

import org.example.gym_shop_2026.entities.OrderItem;
import org.example.gym_shop_2026.entities.Transaction;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.persistence.OrderItemDAO;
import org.example.gym_shop_2026.services.TransactionService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/dashboard/orders")
public class OrderController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final OrderItemDAO orderItemDAO;

    @Autowired
    public OrderController(TransactionService transactionService, UserService userService,
                           OrderItemDAO orderItemDAO) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.orderItemDAO = orderItemDAO;
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

    @GetMapping("/details/{orderId}")
    public String viewOrderDetails(@PathVariable int orderId, Principal principal, Model model) throws SQLException {
        if (principal == null) return "redirect:/login";

        List<OrderItem> items = orderItemDAO.getItemsForOrder(orderId);

        double grandTotal = items.stream()
                .mapToDouble(i -> i.getQuantity() * i.getPriceAtPurchase())
                .sum();

        model.addAttribute("items", items);
        model.addAttribute("orderId", orderId);
        model.addAttribute("total", grandTotal);

        return "order-details";
    }
}