package org.example.gym_shop_2026.services;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.BasketItem;
import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.entities.Transaction;
import org.example.gym_shop_2026.persistence.BasketDAO;
import org.example.gym_shop_2026.persistence.ProductDAO;
import org.example.gym_shop_2026.persistence.TransactionDAO;
import org.example.gym_shop_2026.persistence.paymentMethodDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Slf4j
@Service
public class PaymentService {
    private final BasketService basketService;
    private final ProductService productService;
    private final TransactionService transactionService;
    private final paymentMethodDAO paymentDAO;

    @Autowired
    public PaymentService(BasketService basketService, ProductService productService,
                          TransactionService transactionService, paymentMethodDAO paymentDAO) {
        this.basketService = basketService;
        this.productService = productService;
        this.transactionService = transactionService;
        this.paymentDAO = paymentDAO;
    }

    public boolean completePurchase(int userId, int methodId) {
        try {
            Basket basket = basketService.getBasketForUser(userId);
            if (basket == null || basket.getItems() == null || basket.getItems().isEmpty()) {
                return false;
            }

            double totalAmount = 0;
            for (BasketItem item : basket.getItems()) {
                Product p = productService.getDetails(item.getProductId());
                if (p == null || p.getQuantity() < item.getItemQuantity()) {
                    log.warn("Insufficient stock for product ID: {}", item.getProductId());
                    return false;
                }
                totalAmount += p.getPrice() * item.getItemQuantity();
            }

            Transaction txn = Transaction.builder()
                    .userId(userId)
                    .planId(0) // Default for shop purchases instead of subs
                    .methodId(methodId)
                    .amountPaid(totalAmount)
                    .status("SUCCESS")
                    .build();

            boolean txnSaved = transactionService.addTransaction(txn);
            if (!txnSaved) return false;

            for (BasketItem item : basket.getItems()) {
                productService.purchaseProduct(item.getProductId(), item.getItemQuantity());
            }

            basketService.clearUserBasket(userId);

            return true;
        } catch (SQLException e) {
            log.error("Payment Process Error: {}", e.getMessage());
            return false;
        }
    }
}
