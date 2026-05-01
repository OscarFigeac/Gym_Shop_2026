package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.OrderItem;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class OrderItemDAOImpl implements OrderItemDAO{

    private final DataSource dataSource;

    public OrderItemDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean createOrderItem(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItem.getOrder_id());
            ps.setInt(2, orderItem.getProduct_id());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(4, orderItem.getPriceAtPurchase());
            return ps.executeUpdate() > 0;
        }
    }
}
