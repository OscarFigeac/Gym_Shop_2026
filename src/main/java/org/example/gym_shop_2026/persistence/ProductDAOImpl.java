package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.springframework.stereotype.Repository;
import org.example.gym_shop_2026.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ProductDAOImpl implements ProductDAO{
    private final Connector connector;

    public ProductDAOImpl(Connector connector) {
        this.connector = connector;
    }

    /**
     * @author Oscar
     * Maps all the results from the database after retrieval.
     * @param rs the results being pulled from the database.
     * @return A product object with the results from the database.
     * @throws SQLException If the connection to the database fails at any point.
     */
    private static Product mapProductRow(ResultSet rs) throws SQLException {
        return Product.builder()
                .productId(rs.getInt("product_id"))
                .productCategory(rs.getString("product_category"))
                .name(rs.getString("name"))
                .price(rs.getDouble("price"))
                .quantity(rs.getInt("quantity"))
                .build();
    }

    /**
     * @author Oscar
     * Retrieves all the Products stored in the database
     * @return A list of products containing all the elements from the Products table.
     * @throws SQLException If the connection to the database fails at any point.
     */
    public List<Product> getAllProducts() throws SQLException {
        Connection conn = connector.getConnection();
        if(conn == null) throw new SQLException("Could not connect to the database.");

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try(PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                products.add(mapProductRow(rs));
            }
        } catch(SQLException e) {
            log.error("Error fetching gym products: {}", e.getMessage());
            throw e;
        }
        return products;
    }

    /**
     * @author Oscar
     * Takes in a product object to be added to the database
     * @param p The product to be added into the database
     * @return True if added into the database; false otherwise.
     * @throws SQLException If the connection to the database fails at any point.
     */
    public boolean addProduct(Product p) throws SQLException {
        if(p == null) throw new IllegalArgumentException("Product cannot be null");

        Connection conn = connector.getConnection();
        String sql = "INSERT INTO products (product_id, product_category, name, price, quantity) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getProductId());
            ps.setString(2, p.getProductCategory());
            ps.setString(3, p.getName());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getQuantity());

            return ps.executeUpdate() == 1;
        } catch(SQLException e) {
            log.error("Failed to add product to AWS: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * @author Oscar
     * Takes in a unique product id and looks for it within the database
     * @param id the Identifier of the product being looked for
     * @return the mapped product if found; null otherwise
     * @throws SQLException If the connection to the database fails at any point.
     */
    public Product getProductById(int id) throws SQLException {
        Connection conn = connector.getConnection();
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return mapProductRow(rs);
            }
        }
        return null;
    }

    /**
     * @author Oscar
     * Takes in a productId and the quantity to remove from stock and updates
     * the stock in the database
     * @param productId The product being updated
     * @param quantity The amount being purchased by the customer
     * @return The remaining amount, false if not more to remove
     * @throws SQLException If the connection to the database fails at any point.
     */
    public boolean reduceStock(int productId, int quantity) throws SQLException {
        Connection conn = connector.getConnection();
        String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * @author Oscar
     * Takes in a name to be looked for within the list of products available in the database
     * @param keyword The parameter being looked for
     * @return A list of products containing the keyword
     * @throws SQLException If the connection to the database fails at any point.
     */
    public List<Product> searchProductsByName(String keyword) throws SQLException {
        Connection conn = connector.getConnection();
        if(conn == null) throw new SQLException("AWS Connection failed.");

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProductRow(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Search failed for query: {}", keyword);
            throw e;
        }
        return products;
    }

    /**
     * Method to find all the products that are need or restocking
     * <p>
     * Searches through the product table for products that are at the quantity of the reorder limit and will then be put into a list for the admin to know what to reorder
     *
     * @param reOrder The set limit for the stock quantity to be at or under before the product is restocked
     *
     * @return A list of products at a low stock
     *
     * @throws SQLException If there is an error with the SQL Statement accessing the database
     *
     * @author Eoghan Carroll
     */
    @Override
    public List<Product> getProductsLowStock (int reOrder) throws SQLException{
        Connection conn = connector.getConnection();
        if(conn == null) throw new SQLException("AWS Connection failed.");

        List<Product> toBeStocked = new ArrayList<>();

        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE quantity <= ?")){
            ps.setInt(1, reOrder);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    toBeStocked.add(mapProductRow(rs));
                }
            }
        }
        return toBeStocked;
    }

    /**
     * Method for finding the bestselling products for the Admin
     * <p>
     * Searches through the products table joined with the transaction table to see which are the bestselling products and only shows the top most (eg. SellLimit = 5 then the top 5 bestsellers)
     *
     * @param sellLimit The limit for displaying the bestsellers
     *
     * @return A list of the selected amount of bestselling items
     *
     * @throws SQLException If there is an error with the SQL Statement accessing the database
     *
     * @author Eoghan Carroll
     */
    @Override
    public List<Product> getBestSellers (int sellLimit) throws SQLException{
        Connection conn = connector.getConnection();
        if(conn == null) throw new SQLException("AWS Connection failed.");

        List<Product> bestSellers = new ArrayList<>();

        String sql = "SELECT p.*, SUM(b.itemQuantity) AS total_sold " +
                "FROM products p " +
                "JOIN basket_item b ON p.product_id = b.product_id " +
                "GROUP BY p.product_id ORDER BY total_sold DESC LIMIT ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sellLimit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bestSellers.add(mapProductRow(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Error calculating best sellers: {}", e.getMessage());
            throw e;
        }

        return bestSellers;
    }
}
