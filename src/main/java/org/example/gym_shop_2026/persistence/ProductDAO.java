package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    List<Product> getAllProducts() throws SQLException;
    boolean addProduct(Product p) throws SQLException;
    Product getProductById(int id) throws SQLException;
    boolean reduceStock(int productId, int quantity) throws SQLException;
    List<Product> searchProductsByName(String keyword) throws SQLException;
    List<Product> getProductsLowStock(int reOrder) throws SQLException;
    List<Product> getBestSellers(int sellCatch) throws SQLException;
    List<Product> getByCategory(String category) throws SQLException;
    boolean deleteProduct(Product product) throws SQLException;
    boolean updateProduct(Product product) throws SQLException;
}
