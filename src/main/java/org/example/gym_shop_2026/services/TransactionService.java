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

    /**
     * Constructor for use by Spring when auto-wiring service class with given {@link TransactionDAO}.
     * @param transactionDAO Given {@link TransactionDAO} implementation
     */
    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }
//
//    /**
//     * Provides all transactions for a user with matching id
//     * @param user_id Given id of user in system
//     * @return {@link List<Transaction>} objects with data from configured {@link TransactionDAO}
//     */
//    public List<Transaction> getAllTransactionsForUser(int user_id) {
//        if(user_id <= 0) {
//            log.error("Cannot get transactions for user with ID " + user_id + " as given user id must be > 0!");
//            throw new IllegalArgumentException("Cannot get transactions for user with ID " + user_id + " as given user id must be > 0!");
//
//            try {
//                transactionDAO.getTransactions();
//            }
//            catch (SQLException e) {
//
//            }
//        }
    }
