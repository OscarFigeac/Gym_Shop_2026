package org.example.gym_shop_2026.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;
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
    private final BasketService basketService;
    private final UserService userService;

    public CheckoutController(PaymentService paymentService,
                              BasketService basketService,
                              UserService userService) {
        this.paymentService = paymentService;
        this.basketService = basketService;
        this.userService = userService;
    }

    @GetMapping("/begin")
    public String showCheckoutPage(Model model, Principal principal) throws SQLException {
        User user = userService.findUser(principal.getName());
        Basket basket = basketService.getBasketForUser(user.getUser_id());

        model.addAttribute("basket", basket);

        double total = basketService.getBasketTotal(basket.getBasketId());
        model.addAttribute("grandTotal", total);

        return "checkout";
    }

    @PostMapping("/charge")
    @ResponseBody
    public String processPayment(Principal principal) throws Exception {
        String username = principal.getName();

        User user = userService.findUser(username);

        return paymentService.initiatePayment(user.getUser_id());
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "checkout-success";
    }
}