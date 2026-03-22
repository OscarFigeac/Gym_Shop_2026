package org.example.gym_shop_2026.services;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.persistence.ProductDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductDAOImpl productDAO;

    public ProductService(ProductDAOImpl productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * @author Oscar
     * Fetches all products in the database for shop display.
     * @return a List of products.
     */
    public List<Product> getAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (SQLException e) {
            log.error("Error retrieving all products: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * @author Oscar
     * Handles the search bar logic.
     * @return The found product. If the searchbar is empty, it returns all products.
     */
    public List<Product> searchProducts(String query) {
        if (query == null || query.isBlank()) {
            return getAllProducts();
        }
        try {
            return productDAO.searchProductsByName(query);
        } catch (SQLException e) {
            log.error("Search failed for query: {}", query);
            return Collections.emptyList();
        }
    }

    /**
     * @author Oscar
     * Ensures stock exists before attempting the database update.
     * @return True if the update was completed, false otherwise.
     */
    public boolean purchaseProduct(int productId, int quantity) {
        try {
            Product product = productDAO.getProductById(productId);
            if (product != null && product.getQuantity() >= quantity) {
                return productDAO.reduceStock(productId, quantity);
            }
            log.warn("Purchase failed: Insufficient stock for ID {}", productId);
            return false;
        } catch (SQLException e) {
            log.error("Transaction error during purchase of ID {}: {}", productId, e.getMessage());
            return false;
        }
    }

    /**
     * @author Oscar
     * Retrieves a product's details.
     * @return The details of the found product; null otherwise
     */
    public Product getDetails(int id) {
        try {
            return productDAO.getProductById(id);
        } catch (SQLException e) {
            log.error("Could not find product details for ID: {}", id);
            return null;
        }
    }
}
