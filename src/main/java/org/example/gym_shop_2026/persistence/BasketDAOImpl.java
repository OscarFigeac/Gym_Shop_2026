package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;
import org.example.gym_shop_2026.entities.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class BasketDAOImpl implements BasketDAO {
    private final Connector connector;

    public BasketDAOImpl(Connector connector) {
        this.connector = connector;
    }

    //Functionality

    @Override
    public Basket findByUserID(int user_id) throws SQLException {
        if(user_id <= 0){
            throw new IllegalArgumentException("ID - ID cannot be less than or equal to zero for search process !");
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            throw new SQLException("findUserByID - Unable to establish a connection to the database !");
        }

        Basket found = null;
        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM basket WHERE user_id = ?")){
            ps.setInt(1, user_id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) found = mapBasketRow(rs);
            }
        } catch (SQLException e) {
            log.error("findByUsername(): SQL error. \nException: {}", e.getMessage());
            throw e;
        }
        return found;
    }

    /**
     * @author Oscar
     * Takes in a basket Id to display the content of the basket.
     * @param basketId The basket being looked for
     * @return A List of Items stored in the Basket
     * @throws SQLException If the Connection to the database fails at any point
     */
    @Override
    public List<BasketItem> findItemsByBasketId(int basketId) throws SQLException {
        List<BasketItem> items = new ArrayList<>();
        String sql = "SELECT * FROM basket_item WHERE basketId = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, basketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(BasketItem.builder()
                            .itemId(rs.getInt("itemId"))
                            .productId(rs.getInt("productId"))
                            .basketId(rs.getInt("basketId"))
                            .itemQuantity(rs.getInt("itemQuantity"))
                            .build());
                }
            }
        }
        return items;
    }

    /**
     * @author Oscar
     * Adds an item into the basket, it's id and quantity
     * @param basketId The basket identifier where the products are being added
     * @param productId The Identifier of the product being added
     * @param quantity The number of Items being added
     * @throws SQLException If the connection to the database fails at any point
     */
    @Override
    public void addItemToBasket(int basketId, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO basket_item (basket_id, product_id, item_quantity) " +
                "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE item_quantity = item_quantity + ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, basketId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);
            ps.executeUpdate();
        }
    }

    /**
     * @author Oscar
     * Updates the amount of an item currently in a basket
     * @param itemId The item being updated
     * @param newQuantity The new amount being added into the basket
     * @throws SQLException If the connection to the database fails at any point
     */
    @Override
    public void updateItemQuantity(int itemId, int newQuantity) throws SQLException {
        String sql = "UPDATE basket_item SET item_quantity = ? WHERE item_id = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, itemId);
            ps.executeUpdate();
        }
    }

    /**
     * @author Oscar
     * Deletes an item from a basket
     * @param itemId The item being deleted
     * @throws SQLException If the connection to the database fails at any point
     */
    @Override
    public void removeItem(int itemId) throws SQLException {
        String sql = "DELETE FROM basket_item WHERE item_id = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }

    /**
     * @author Oscar
     * creates a basket for a user. The userId is passed as a parameter
     * @param userId The owner of the basket being created
     * @return The full Basket Object so that the service can handle it immediately
     * @throws SQLException If the connection to the database fails at any point
     */
    @Override
    public Basket createBasket(int userId) throws SQLException {
        String sql = "INSERT INTO basket (user_id, status, created_at) VALUES (?, 'Active', CURRENT_TIMESTAMP)";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    return findByUserID(userId);
                }
            }
        }
        throw new SQLException("Creating basket failed, no ID obtained.");
    }

    //Private Methods

    private static Basket mapBasketRow(ResultSet rs) throws SQLException {
        return Basket.builder()
                .basketId(rs.getInt("basket_id"))
                .user_id(rs.getInt("user_id"))
                .status(rs.getString("status"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
    }
}
