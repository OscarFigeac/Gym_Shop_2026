package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
//import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.User;
import org.springframework.stereotype.Repository;
import org.example.gym_shop_2026.entities.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ProductDAOImpl implements ProductDAO{
    private final DataSource dataSource;

    public ProductDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static Product mapProductRow(ResultSet rs) throws SQLException {
        return Product.builder()
                .productId(rs.getInt("product_id"))
                .productCategory(rs.getString("product_category"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getDouble("price"))
                .quantity(rs.getInt("quantity"))
                .imageUrl(rs.getString("image_url"))
                .build();
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapProductRow(rs));
            }
        } catch (SQLException e) {
            log.error("Error fetching gym products: {}", e.getMessage());
            throw e;
        }
        return products;
    }

    public boolean addProduct(Product p) throws SQLException {
        if (p == null) throw new IllegalArgumentException("Product cannot be null");
        String sql = "INSERT INTO products (product_category, name, description, price, quantity, image_url) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductCategory());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getQuantity());
            ps.setString(6, p.getImageUrl());
            return ps.executeUpdate() == 1;
        }
    }

    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapProductRow(rs);
            }
        }
        return null;
    }

    public boolean reduceStock(int productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Product> searchProductsByName(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProductRow(rs));
                }
            }
        }
        return products;
    }

    @Override
    public List<Product> getProductsLowStock(int reOrder) throws SQLException {
        List<Product> toBeStocked = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE quantity <= ?")) {
            ps.setInt(1, reOrder);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    toBeStocked.add(mapProductRow(rs));
                }
            }
        }
        return toBeStocked;
    }

    @Override
    public List<Product> getBestSellers(int sellLimit) throws SQLException {
        List<Product> bestSellers = new ArrayList<>();
        String sql = "SELECT p.*, SUM(b.item_quantity) AS sold FROM products p JOIN basket_item b ON p.product_id = b.product_id GROUP BY p.product_id ORDER BY sold DESC LIMIT ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sellLimit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = mapProductRow(rs);
                    p.setTotal_sold(rs.getInt("sold"));
                    bestSellers.add(p);
                }
            }
        }
        return bestSellers;
    }

    public List<Product> getByCategory(String category) throws SQLException {
        List<Product> categorised = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(product_category) LIKE LOWER(?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + category + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categorised.add(mapProductRow(rs));
                }
            }
        }
        return categorised;
    }

    @Override
    public boolean deleteProduct(Product product) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getProductId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET product_category = ?, name = ?, price = ?, quantity = ?, description = ?, image_url = ? WHERE product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductCategory());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setString(5, product.getDescription());
            ps.setString(6, product.getImageUrl());
            ps.setInt(7, product.getProductId());

            return ps.executeUpdate() == 1;
        }
    }

}
