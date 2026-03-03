package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Basket;

import java.sql.SQLException;

public interface BasketDAO {
    Basket findByUserID(int user_id) throws SQLException;
}
