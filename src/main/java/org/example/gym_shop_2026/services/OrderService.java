package org.example.gym_shop_2026.services;

import org.example.gym_shop_2026.persistence.OrderDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public int countActiveOrdersForUser(int userId) throws SQLException {
        return orderDAO.getActiveOrderCount(userId);
    }
}
