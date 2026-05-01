package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.OrderItem;

import java.sql.SQLException;

public interface OrderItemDAO {
    boolean createOrderItem(OrderItem orderItem) throws SQLException;
}
