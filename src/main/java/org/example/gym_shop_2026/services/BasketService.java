package org.example.gym_shop_2026.services;

import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;
import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.persistence.BasketDAO;
import org.example.gym_shop_2026.persistence.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

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
    public Basket getBasketForUser(int userId) throws SQLException {
        Basket basket = basketDAO.findByUserID(userId);
        if (basket != null) {
            List<BasketItem> items = basketDAO.findItemsByBasketId(basket.getBasketId());
            basket.setItems(items);
        }
        return basket;
    }

    /**
     * @author Oscar
     * Adds a product to the basket
     * @param userId The owner of the basket being modified
     * @param productId The identifier of the product being added
     * @param quantity The amount of products being added
     * @throws SQLException If the connection to the database fails at any point
     */
    public void addProductToBasket(int userId, int productId, int quantity) throws SQLException {
        Basket basket = basketDAO.findByUserID(userId);
        if (basket == null) {
            basket = basketDAO.createBasket(userId);
        }
        basketDAO.addItemToBasket(basket.getBasketId(), productId, quantity);
    }

    /**
     * @author Oscar
     * Calculates the total of the products being purchased by the customer.
     * @param basketId The Identifier of the basket the calculation is being made for
     * @return A number indicating the total.
     * @throws SQLException If the connection to the database fails at any point.
     */
    public double getBasketTotal(int basketId) throws SQLException {
        List<BasketItem> items = basketDAO.findItemsByBasketId(basketId);
        double total = 0;
        for (BasketItem item : items) {
            Product p = productDAO.getProductById(item.getProductId());
            if (p != null) {
                total += p.getPrice() * item.getItemQuantity();
            }
        }
        return total;
    }

    /**
     * @author Oscar
     * Takes in an itemId and the new quantity desired for the product in the basket.
     * @param itemId The identifier of the object being updated.
     * @param quantity The new amount desired in the basket
     * @throws SQLException If the connection to the database fails at any point.
     */
    public void updateQuantity(int itemId, int quantity) throws SQLException {
        if (quantity <= 0) {
            basketDAO.removeItem(itemId);
        } else {
            basketDAO.updateItemQuantity(itemId, quantity);
        }
    }

    /**
     * @author Oscar
     * Takes in an item to remove them from the basket
     * @param itemId The identifier of the item being removed
     * @throws SQLException If the connection to the database fails at any point.
     */
    public void removeItem(int itemId) throws SQLException {
        basketDAO.removeItem(itemId);
    }

    /**
     * @author Oscar
     * Clears the basket of the user provided
     * @param userId The identifier of the user whose basket is being cleared.
     * @throws SQLException If the connection to the database fails at any point.
     */
    public void clearUserBasket(int userId) throws SQLException {
        Basket basket = basketDAO.findByUserID(userId);
        if (basket != null) {
            basketDAO.clearBasket(basket.getBasketId());
        }
    }
}
