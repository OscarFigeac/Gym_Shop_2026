package org.example.gym_shop_2026.services;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
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

    public PaymentService(BasketService basketService, ProductService productService, TransactionService transactionService) {
        this.basketService = basketService;
        this.productService = productService;
        this.transactionService = transactionService;
    }

    public String initiatePayment(int userId) throws Exception{
        Basket basket = basketService.getBasketForUser(userId);
        double totalAmount = calculateTotal(basket);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (totalAmount * 100)) // using cents
                .setCurrency("eur")
                .putMetadata("userId", String.valueOf(userId))
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Transaction t = Transaction.builder()
                .userId(userId)
                .amountPaid(totalAmount)
                .stripePaymentIntentId(intent.getId())
                .status("PENDING")
                .build();
        transactionService.addTransaction(t);

        return intent.getClientSecret();
    }

    public void fulfillOrder(String paymentIntentId) throws SQLException {
        Transaction txn = transactionService.getTransactionByStripeId(paymentIntentId);

        if (txn == null || !"PENDING".equals(txn.getStatus())) {
            log.warn("Transaction not found or already processed: {}", paymentIntentId);
            return;
        }

        int userId = txn.getUserId();
        Basket basket = basketService.getBasketForUser(userId);

        for (BasketItem item : basket.getItems()) {
            productService.purchaseProduct(item.getProductId(), item.getItemQuantity());
            log.info("Stock reduced for Product ID: {}", item.getProductId());
        }

        txn.setStatus("SUCCESS");
        transactionService.updateTransaction(txn);

        basketService.clearUserBasket(userId);
        log.info("Fulfilling order for PaymentIntent: {}", paymentIntentId);
    }

    private double calculateTotal(Basket basket) {
        return basket.getItems().stream()
                .mapToDouble(item -> productService.getDetails(item.getProductId()).getPrice() * item.getItemQuantity())
                .sum();
    }
}
