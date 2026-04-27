package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class BasketDAOImpl implements BasketDAO {

    private final DataSource dataSource;

    public BasketDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Basket findByUserID(int userId) {
        String sql = "SELECT * FROM basket WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.sql.Timestamp ts = rs.getTimestamp("created_at");
                    java.time.LocalDateTime createdAt = (ts != null) ? ts.toLocalDateTime() : java.time.LocalDateTime.now();

                    return Basket.builder()
                            .basketId(rs.getInt("basket_id"))
                            .user_id(rs.getInt("user_id"))
                            .status(rs.getString("status"))
                            .createdAt(Timestamp.valueOf(createdAt))
                            .build();
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching basket for user: {}", userId, e);
            throw new RuntimeException("Could not retrieve basket.", e); // Important: rethrow or handle the error properly
        }
        return null;
    }

    @Override
    public boolean addProductToBasket(int basketId, int productId, int quantity) {
        String sql = "INSERT INTO basket_item (basket_id, product_id, itemQuantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE itemQuantity = itemQuantity + ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, basketId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("addProductToBasket(): SQL error. Exception: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeProduct(int basketId, int productId) {
        String sql = "DELETE FROM basket_item WHERE basket_id = ? AND product_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, basketId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("removeProduct(): SQL error. Exception: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean clearBasket(int basketId) {
        String sql = "DELETE FROM basket_item WHERE basket_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, basketId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("clearBasket(): SQL error. Exception: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<BasketItem> findItemsByBasketId(int basketId) {
        List<BasketItem> items = new ArrayList<>();
        String sql = "SELECT * FROM basket_item WHERE basket_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, basketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(BasketItem.builder()
                            .productId(rs.getInt("product_id"))
                            .itemQuantity(rs.getInt("itemQuantity"))
                            .build());
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching items for basket: {}", basketId, e);
        }
        return items;
    }
}