package org.example.gym_shop_2026.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.paymentMethod;
import org.example.gym_shop_2026.persistence.paymentMethodDAO;
import org.example.gym_shop_2026.services.BasketService;
import org.example.gym_shop_2026.services.PaymentService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final PaymentService paymentService;

    public CheckoutController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public String showCheckoutPage() {
        return "checkout";
    }

    @PostMapping("/charge")
    @ResponseBody
    public String processPayment(HttpSession session) throws Exception {
        int userId = (int) session.getAttribute("user"); //old userId but basket looking for user.
        // If crashes,
        // return to the old type and switch on BasketController as well

        return paymentService.initiatePayment(userId);
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "checkout-success";
    }
}