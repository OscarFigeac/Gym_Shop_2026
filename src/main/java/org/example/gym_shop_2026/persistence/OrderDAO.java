package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Order;

import java.sql.SQLException;

public interface OrderDAO {

    public int createOrder(Order order) throws SQLException;
}
