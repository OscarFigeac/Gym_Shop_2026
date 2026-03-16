package org.example.gym_shop_2026.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.paymentMethod;
import org.example.gym_shop_2026.services.paymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/payment-methods")
public class PaymentMethodController {
    private final paymentMethodService methodService;

    @Autowired
    public PaymentMethodController(paymentMethodService methodService) {
        this.methodService = methodService;
    }

    @GetMapping
    public String listMethods(HttpSession session, Model model) throws SQLException {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<paymentMethod> methods = methodService.getAllUserMethods(user.getUser_id());
        model.addAttribute("methods", methods);
        return "payment-methods";
    }

    @PostMapping("/add")
    public String addMethod(@ModelAttribute paymentMethod newMethod, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        newMethod.setUserId(user.getUser_id());
        boolean success = methodService.addPaymentMethod(newMethod);

        if (success) {
            return "redirect:/payment-methods?success=true";
        } else {
            return "redirect:/payment-methods?error=duplicate_token";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMethod(@PathVariable int id, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute("user");
        paymentMethod target = methodService.getMethodDetails(id);

        if (user != null && target != null && target.getUserId() == user.getUser_id()) {
            methodService.deletePaymentMethod(id);
        }

        return "redirect:/payment-methods";
    }
}
