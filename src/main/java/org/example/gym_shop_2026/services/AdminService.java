package org.example.gym_shop_2026.services;


import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.entities.Transaction;
import org.example.gym_shop_2026.persistence.ProductDAO;
import org.example.gym_shop_2026.persistence.TransactionDAO;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


@Service
public class AdminService {

    public final ProductDAO pDAO;
    public final TransactionDAO tDAO;

    public AdminService(ProductDAO pD, TransactionDAO tD){
        this.pDAO = pD;
        this.tDAO = tD;
    }

    public List<Product> getReorderProducts() throws SQLException{
        return pDAO.getProductsLowStock(2);
    }

    public List<Product> getBestSellers() throws SQLException{
        return pDAO.getBestSellers(10);
    }

    public List<Transaction> getAllTransactions() {
        try {
            return tDAO.getAllTransactions();
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
