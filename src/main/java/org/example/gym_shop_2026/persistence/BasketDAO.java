package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;

import java.sql.SQLException;
import java.util.List;

public interface BasketDAO {
    Basket findByUserID(int user_id) throws SQLException;
    public List<BasketItem> findItemsByBasketId(int basketId) throws SQLException;
    void addItemToBasket(int basketId, int productId, int quantity) throws SQLException;
    void updateItemQuantity(int itemId, int newQuantity) throws SQLException;
    void removeItem(int itemId) throws SQLException;
    public Basket createBasket(int userId) throws SQLException;
    void clearBasket(int basketId) throws SQLException;
}
