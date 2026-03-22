package org.example.gym_shop_2026.controllers;

import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.services.BasketService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final BasketService basketService;
    private final UserService userService;

    public GlobalControllerAdvice(BasketService basketService, UserService userService) {
        this.basketService = basketService;
        this.userService = userService;
    }

    @ModelAttribute("basketCount")
    public int getBasketCount(Principal principal) {
        if (principal == null) {
            return 0;
        }

        try {
            User user = userService.findUser(principal.getName());

            if (user == null) return 0;

            Basket basket = basketService.getBasketForUser(user.getUser_id());

            if (basket != null && basket.getItems() != null) {
                return basket.getItems().stream()
                        .mapToInt(BasketItem::getItemQuantity)
                        .sum();
            }
        } catch (Exception e) {
            System.err.println("Error calculating basket count: " + e.getMessage());
        }

        return 0;
    }
}