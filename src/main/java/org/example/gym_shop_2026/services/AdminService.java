package org.example.gym_shop_2026.services;


import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.persistence.ProductDAO;
import org.example.gym_shop_2026.persistence.ProductDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class AdminService {

    public final ProductDAO pDAO;

    public AdminService(ProductDAO pD){
        this.pDAO = pD;
    }

    public List<Product> getReorderProducts() throws SQLException{
        return pDAO.getProductsLowStock(2);
    }

    public List<Product> getBestSellers() throws SQLException{
        return pDAO.getBestSellers(10);
    }
}
