package org.example.gym_shop_2026.controllers;

import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.services.BasketService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;
    private final UserService userService;

    @Autowired
    public BasketController(BasketService basketService, UserService userService) {
        this.basketService = basketService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Principal principal) throws SQLException {
        if (principal == null) return null;
        return userService.findUser(principal.getName());
    }

    @GetMapping
    public String viewBasket(Principal principal, Model model) throws SQLException {
        User user = getAuthenticatedUser(principal);
        if (user == null) return "redirect:/login";

        Basket basket = basketService.getBasketForUser(user.getUser_id());

        if (basket != null) {
            model.addAttribute("basketItems", basket.getItems());

            model.addAttribute("totalPrice", basketService.getBasketTotal(basket.getBasketId()));
        } else {
            model.addAttribute("basketItems", new ArrayList<>());
        }

        return "basket";
    }

    @PostMapping("/add")
    public String addToBasket(@RequestParam("productId") int productId,
                              @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                              Principal principal) throws SQLException {
        User user = getAuthenticatedUser(principal);
        if (user == null) return "redirect:/login";

        basketService.addProductToBasket(user.getUser_id(), productId, quantity);
        return "redirect:/basket";
    }

    /**
     * Fixes the error in image_923ac7.png.
     * Maps the 'itemId' from the URL to the 'productId' in the service call.
     */
    @PostMapping("/remove/{productId}")
    public String removeItem(@PathVariable int productId, Principal principal) throws SQLException {
        User user = getAuthenticatedUser(principal);
        if (user == null) return "redirect:/login";

        Basket basket = basketService.getBasketForUser(user.getUser_id());
        if (basket != null) {
            basketService.removeProductFromBasket(basket.getBasketId(), productId);
        }

        return "redirect:/basket";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam("productId") int productId,
                                 @RequestParam("quantity") int quantity,
                                 Principal principal) throws SQLException {
        User user = getAuthenticatedUser(principal);
        if (user != null) {
            Basket basket = basketService.getBasketForUser(user.getUser_id());
            if (basket != null) {
                basketService.updateQuantity(basket.getBasketId(), productId, quantity);
            }
        }
        return "redirect:/basket";
    }
}