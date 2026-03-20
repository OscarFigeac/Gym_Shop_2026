package org.example.gym_shop_2026.services;


import org.example.gym_shop_2026.persistence.ProductDAO;
import org.example.gym_shop_2026.persistence.ProductDAOImpl;

public class AdminService {

    public final ProductDAO pDAO;

    public AdminService(ProductDAO pD){
        this.pDAO = pD;
    }


}
