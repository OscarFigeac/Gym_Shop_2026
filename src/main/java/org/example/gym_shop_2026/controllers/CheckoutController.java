package org.example.gym_shop_2026.controllers;

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
    private final paymentMethodDAO paymentDAO;
    private final BasketService basketService;
    private final UserService userService;

    @Autowired
    public CheckoutController(PaymentService paymentService, paymentMethodDAO paymentDAO,
                              BasketService basketService, UserService userService) {
        this.paymentService = paymentService;
        this.paymentDAO = paymentDAO;
        this.basketService = basketService;
        this.userService = userService;
    }

    @GetMapping("/begin")
    public String beginCheckout(Principal principal) throws SQLException {
        User user = userService.findUser(principal.getName());
        List<paymentMethod> methods = paymentDAO.findAllMethodsByUserId(user.getUser_id());

        if (methods.isEmpty()) {
            return "redirect:/payment-methods?error=no_card_found";
        }
        return "redirect:/checkout";
    }

    @GetMapping
    public String showCheckout(Principal principal, Model model) throws SQLException {
        User user = userService.findUser(principal.getName());

        List<paymentMethod> methods = paymentDAO.findAllMethodsByUserId(user.getUser_id());
        model.addAttribute("paymentMethods", methods);
        model.addAttribute("basket", basketService.getBasketForUser(user.getUser_id()));

        double total = basketService.getBasketTotal(user.getUser_id());
        model.addAttribute("orderTotal", total);

        return "checkout";
    }

    @PostMapping("/confirm")
    public String processPayment(@RequestParam("methodId") int methodId, Principal principal) throws SQLException {
        User user = userService.findUser(principal.getName());

        boolean success = paymentService.completePurchase(user.getUser_id(), methodId);

        if (success) {
            basketService.clearUserBasket(user.getUser_id());
            return "redirect:/checkout/success";
        } else {
            return "redirect:/checkout?error=payment_failed";
        }
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "checkout-success";
    }
}