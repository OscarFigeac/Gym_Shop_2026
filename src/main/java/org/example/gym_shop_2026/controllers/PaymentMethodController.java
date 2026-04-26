package org.example.gym_shop_2026.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.paymentMethod;
import org.example.gym_shop_2026.services.UserService;
import org.example.gym_shop_2026.services.paymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/payment-methods")
public class PaymentMethodController {
    private final paymentMethodService methodService;
    private final UserService userService;

    public PaymentMethodController(paymentMethodService methodService, UserService userService) {
        this.methodService = methodService;
        this.userService = userService;
    }

    @GetMapping
    public String listMethods(Model model, Principal principal) throws SQLException {
        User user = userService.findUser(principal.getName());
        List<paymentMethod> methods = methodService.getAllUserMethods(user.getUser_id());

        model.addAttribute("methods", methods);
        return "payment-methods";
    }

    @PostMapping("/add")
    public String addMethod(@RequestParam String stripePaymentMethodId,
                            @RequestParam String lastFour,
                            @RequestParam String expDate,
                            @RequestParam String brand,
                            Principal principal) throws SQLException {

        User user = userService.findUser(principal.getName());

        paymentMethod newMethod = paymentMethod.builder()
                .userId(user.getUser_id())
                .stripePaymentMethodId(stripePaymentMethodId)
                .lastFourDigits(lastFour)
                .expiryDate(expDate)
                .cardType(brand)
                .isValid(true)
                .isPrimary(false)
                .build();

        methodService.addPaymentMethod(newMethod);
        return "redirect:/payment-methods?success=true";
    }

    @PostMapping("/delete/{id}")
    public String deleteMethod(@PathVariable int id, Principal principal) throws SQLException {
        User user = userService.findUser(principal.getName());
        paymentMethod target = methodService.getMethodDetails(id);

        if (target != null && target.getUserId() == user.getUser_id()) {
            methodService.deletePaymentMethod(id);
        }
        return "redirect:/payment-methods";
    }
}
