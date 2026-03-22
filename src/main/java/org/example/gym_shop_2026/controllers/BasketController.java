package org.example.gym_shop_2026.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.services.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping
    public String viewBasket(HttpSession session, Model model) throws SQLException {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Basket basket = basketService.getBasketForUser(user.getUser_id());
        model.addAttribute("basket", basket);

        if (basket != null) {
            model.addAttribute("total", basketService.getBasketTotal(basket.getBasketId()));
        }

        return "basket";
    }

    @PostMapping("/add")
    public String addToBasket(@RequestParam int productId,
                              @RequestParam(defaultValue = "1") int quantity,
                              HttpSession session) throws SQLException {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            basketService.addProductToBasket(user.getUser_id(), productId, quantity);

            Basket basket = basketService.getBasketForUser(user.getUser_id());
            session.setAttribute("basketTotal", basketService.getBasketTotal(basket.getBasketId()));
        }

        return "redirect:/products";
    }

    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable int itemId) throws SQLException {
        basketService.removeItem(itemId);
        return "redirect:/basket";
    }
}
