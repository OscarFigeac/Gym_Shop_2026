package org.example.gym_shop_2026.services;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Transaction;
import org.example.gym_shop_2026.persistence.TransactionDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Provides all transactions for a user with matching id
     * @param user_id Given id of user in system
     * @return {@link List<Transaction>} objects with data from configured {@link TransactionDAO}
     */
    public List<Transaction> getAllTransactionsForUser(int user_id) {
        if(user_id <= 0) {
            log.error("Cannot get transactions for user with ID " + user_id + " as given user id must be > 0!");
            throw new IllegalArgumentException("Cannot get transactions for user with ID " + user_id + " as given user id must be > 0!");
        }

        List<Transaction> foundTransactions = new ArrayList<>();

        try {
            foundTransactions = transactionDAO.getTransactionsByUserId(user_id);
        }
        catch (SQLException e) {
            log.error("Could not perform get all transactions service operation due to error! {}", e.toString());
        }

        return foundTransactions;
    }

    /**
     * Adds a new transaction to persistence with given {@link Transaction} object.
     * @param transaction Given {@link Transaction} object
     * @return True if transaction added successfully, else false
     */
    public boolean addTransaction(Transaction transaction) throws SQLException {
//        if(transaction == null) {
//            log.error("Cannot perform add transaction operation service as given Transaction object was null!");
//            throw new IllegalArgumentException("Cannot add transaction as given Transaction was null!");
//        }
//
//        boolean addResult = false;
//
//        try {
//            addResult = transactionDAO.createTransaction(transaction);
//        }
//        catch (SQLException e) {
//            log.error("Could not perform add transaction service operation due to error! {}", e.toString());
//        }

        return transactionDAO.createTransaction(transaction);

    }

    /**
     * Updates transaction status to new given {@link Transaction} object's transaction status.
     * @param transaction Given {@link Transaction} object
     * @return True if one update occurred in persistence.
     *
     * @apiNote Must be same transaction id as original transaction that exists in persistence
     */
    public boolean updateTransactionStatus(Transaction transaction) {
        if (transaction == null) return false;

        try {
            Transaction found = transactionDAO.findTransactionById(transaction.getTransactionId());
            if (found == null) return false;

            return transactionDAO.updateTransaction(transaction);
        } catch (SQLException e) {
            log.error("Error updating transaction status: {}", e.getMessage());
            return false;
        }
    }

    public Transaction getTransactionByStripeId(String stripeId) throws SQLException {
        return transactionDAO.findTransactionByStripeId(stripeId);
    }

    public void updateTransaction(Transaction txn) throws SQLException {
        transactionDAO.updateTransaction(txn);
    }

    public Transaction findTransactionByID(int id) throws SQLException{
        return transactionDAO.findTransactionById(id);
    }
}
