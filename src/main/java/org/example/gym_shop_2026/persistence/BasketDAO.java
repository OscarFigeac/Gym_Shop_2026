package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Basket;

import java.sql.SQLException;
import java.sql.Timestamp;

public interface BasketDAO {
    Basket findByUserID(int user_id) throws SQLException;
    boolean updateStatus(int basketId, String status) throws SQLException;
}
