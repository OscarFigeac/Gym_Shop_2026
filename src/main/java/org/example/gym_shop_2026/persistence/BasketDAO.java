package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Basket;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface BasketDAO {
    Basket findByUserID(int user_id) throws SQLException;
    boolean updateStatus(int basketId, String status) throws SQLException;
    List<Basket> findByStatus(int user_id, String status) throws SQLException;
}
