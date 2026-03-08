package org.example.gym_shop_2026.services;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.persistence.TransactionDAO;
import org.springframework.stereotype.Service;

/**
 * A service class for interactions with transactions.
 *
 * @author Cal Woods
 */
@Service
@Slf4j
public class TransactionService {
    private TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }
}
