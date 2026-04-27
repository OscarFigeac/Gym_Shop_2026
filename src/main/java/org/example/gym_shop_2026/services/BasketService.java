package org.example.gym_shop_2026.services;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;
import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.persistence.BasketDAO;
import org.example.gym_shop_2026.persistence.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class BasketService {

    private final BasketDAO basketDAO;
    private final ProductDAO productDAO;

    @Autowired
    public BasketService(BasketDAO basketDAO, ProductDAO productDAO) {
        this.basketDAO = basketDAO;
        this.productDAO = productDAO;
    }

    /**
     * @author Oscar
     * Retrieves the basket  for a specified user
     * @param userId The user being looked for
     * @return The Basket Object
     * @throws SQLException If the connection to the database fails at any point
     */
    public Basket getBasketForUser(int userId) {
        try {
            Basket basket = basketDAO.findByUserID(userId);
            if (basket != null) {
                List<BasketItem> items = basketDAO.findItemsByBasketId(basket.getBasketId());

                for (BasketItem item : items) {
                    Product p = productDAO.getProductById(item.getProductId());
                    item.setProduct(p);
                }

                basket.setItems(items);
            }
            return basket;
        } catch (Exception e) {
            log.error("Error fetching basket for user: {}", userId, e);
            throw new RuntimeException("Could not retrieve basket.");
        }
    }

    /**
     * @author Oscar
     * Adds a product to the basket
     * @param userId The owner of the basket being modified
     * @param productId The identifier of the product being added
     * @param quantity The amount of products being added
     * @throws SQLException If the connection to the database fails at any point
     */
    public void addProductToBasket(int userId, int productId, int quantity) {
        try {
            Basket basket = basketDAO.findByUserID(userId);

            if (basket != null) {
                basketDAO.addProductToBasket(basket.getBasketId(), productId, quantity);
            } else {
                log.warn("Attempted to add product to non-existent basket for user: {}", userId);
            }
        } catch (Exception e) {
            log.error("Error adding product {} to basket for user {}", productId, userId, e);
            throw new RuntimeException("Failed to add item to basket.");
        }
    }

    /**
     * @author Oscar
     * Calculates the total of the products being purchased by the customer.
     * @param basketId The Identifier of the basket the calculation is being made for
     * @return A number indicating the total.
     * @throws SQLException If the connection to the database fails at any point.
     */
    public double getBasketTotal(int basketId) {
        try {
            List<BasketItem> items = basketDAO.findItemsByBasketId(basketId);
            double total = 0;
            for (BasketItem item : items) {
                Product p = productDAO.getProductById(item.getProductId());
                if (p != null) {
                    total += p.getPrice() * item.getItemQuantity();
                }
            }
            return total;
        } catch (SQLException e) {
            log.error("Error calculating total for basket: {}", basketId, e);
            throw new RuntimeException("Error calculating basket total");
        }
    }

    /**
     * @author Oscar
     * Takes in an itemId and the new quantity desired for the product in the basket.
     * @param itemId The identifier of the object being updated.
     * @param quantity The new amount desired in the basket
     * @throws SQLException If the connection to the database fails at any point.
     */
    public void updateProductQuantity(int basketId, int productId, int quantity) {
        try {
            if (quantity <= 0) {
                basketDAO.removeProduct(basketId, productId);
            } else {
                basketDAO.addProductToBasket(basketId, productId, quantity);
            }
        } catch (Exception e) {
            log.error("Error updating quantity for product {} in basket {}", productId, basketId, e);
            throw new RuntimeException("Error updating quantity.");
        }
    }

    /**
     * @author Oscar
     * Takes in an item to remove them from the basket
     * @param itemId The identifier of the item being removed
     * @throws SQLException If the connection to the database fails at any point.
     */
    public void removeProductFromBasket(int basketId, int productId) {
        try {
            basketDAO.removeProduct(basketId, productId);
        } catch (Exception e) {
            log.error("Error removing product {} from basket {}", productId, basketId, e);
            throw new RuntimeException("Error removing item.");
        }
    }

    /**
     * @author Oscar
     * Clears the basket of the user provided
     * @param userId The identifier of the user whose basket is being cleared.
     * @throws SQLException If the connection to the database fails at any point.
     */
    public void clearUserBasket(int userId) {
        try {
            Basket basket = basketDAO.findByUserID(userId);
            if (basket != null) {
                basketDAO.clearBasket(basket.getBasketId());
            }
        } catch (Exception e) {
            log.error("Error clearing basket for user: {}", userId, e);
            throw new RuntimeException("Error clearing basket.");
        }
    }
}