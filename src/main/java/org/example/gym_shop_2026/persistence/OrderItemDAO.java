package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.OrderItem;

import java.sql.SQLException;
import java.util.List;

public interface OrderItemDAO {
    boolean createOrderItem(OrderItem orderItem) throws SQLException;
    List<OrderItem> getItemsForOrder(int orderId) throws SQLException;
}
