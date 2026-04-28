package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;

import java.sql.SQLException;
import java.util.List;

public interface BasketDAO {
    Basket findByUserID(int userId);
    List<BasketItem> findItemsByBasketId(int basketId);
    boolean addProductToBasket(int basketId, int productId, int quantity);
    boolean removeProduct(int basketId, int productId);
    boolean clearBasket(int basketId);
    boolean createBasket(int userId);
}
