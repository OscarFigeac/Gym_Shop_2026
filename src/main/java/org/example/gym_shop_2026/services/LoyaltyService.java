package org.example.gym_shop_2026.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.entities.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * Handles loyalty programme services.
 *
 * @author Cal Woods
 */

@Slf4j
@AllArgsConstructor
@Service
public class LoyaltyService {
    private UserService userService;
    private ProductService productService;

    /**
     * Calculates {@link Product} discount by checking given {@link User} birthday against current date.
     * @param product Given {@link Product} object
     * @param discountPercentage Given percentage to discount
     * @param user Given {@link User}
     * @return New discounted product price
     * @throws SQLException If operation fails
     */
    public double calculateBirthdayLoyaltyDiscount(Product product, double discountPercentage, User user) throws SQLException {
        if(product == null) {
            log.error("Could not perform calculate birthday loyalty operation as given Product object was null!");
            throw new IllegalArgumentException("Given Product object was null!");
        }
        if(discountPercentage <= 0.00) {
            log.error("Bad discount parameter, must not be < 0.00");
            throw new IllegalArgumentException("Bad discount parameter, must not be < 0.00");
        }
        if(user == null) {
            log.error("User may not be null!");
            throw new IllegalArgumentException("User may not be null!");
        }

        User existingUser = userService.readUser(user.getUser_id());

        if(existingUser == null) {
            log.error("Could not perform calculate birthday loyalty as given authenticated user not found!");
            throw new NullPointerException("Authenticated user not found!");
        }
        if(productService.getDetails(product.getProductId()) == null) {
            log.error("Could not perform calculate birthday loyalty operation as given Product object was not found!");
        }

        double discountedPrice = product.getPrice() - (product.getPrice() * discountPercentage);

        return discountedPrice;
    }
}
