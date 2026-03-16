package org.example.gym_shop_2026.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.paymentMethod;
import org.example.gym_shop_2026.persistence.paymentMethodDAO;
import org.example.gym_shop_2026.services.BasketService;
import org.example.gym_shop_2026.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final PaymentService paymentService;
    private final paymentMethodDAO paymentDAO;
    private final BasketService basketService;

    @Autowired
    public CheckoutController(PaymentService paymentService, paymentMethodDAO paymentDAO, BasketService basketService) {
        this.paymentService = paymentService;
        this.paymentDAO = paymentDAO;
        this.basketService = basketService;
    }

    @GetMapping
    public String showCheckout(HttpSession session, Model model) throws SQLException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; //forces login to access payment methods change on next release
        }

        List<paymentMethod> methods = paymentDAO.findAllMethodsByUserId(user.getUser_id());
        model.addAttribute("paymentMethods", methods);
        model.addAttribute("basket", basketService.getBasketForUser(user.getUser_id()));

        double total = basketService.getBasketTotal(user.getUser_id());
        model.addAttribute("orderTotal", total);

        return "checkout";
    }

    @PostMapping("/confirm")
    public String processPayment(@RequestParam("methodId") int methodId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        boolean success = paymentService.completePurchase(user.getUser_id(), methodId);

        if (success) {
            session.setAttribute("basketTotal", 0.0);
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
