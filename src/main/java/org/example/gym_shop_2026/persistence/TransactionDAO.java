package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO {
    /**
     * Creates and adds a transaction record in database.
     * @param newTransaction {@link Transaction} object to use for adding record
     * @return True if added successfully
     *
     * @throws SQLException If operation fails
     */
    boolean createTransaction(Transaction newTransaction) throws SQLException;

    List<Transaction> getTransactions() throws SQLException;

    /**
     * Retrieves a transaction record using given transaction
     * id.
     * @param transactionId Given transaction id
     * @return {@link Transaction} object containing found record information or
     * null if none found
     * @throws SQLException If operation fails
     */
    Transaction findTransactionById(int transactionId) throws SQLException;

    /**
     * Updates a {@link Transaction} object with a given id using a
     * new Transaction object.
     * @param transactionId Given transaction id
     * @param newTransaction Given {@link Transaction} object with changed data.
     * @return {@link Transaction} object that was inserted
     * @throws SQLException If operation fails
     */
    Transaction UpdateTransactionById(int transactionId, Transaction newTransaction) throws SQLException;

    /**
     * Deletes a transaction using transaction id.
     * @param transactionId Given transaction id, must be > 0
     * @return Deleted transaction information via a {@link Transaction}
     * object.
     * @throws SQLException If operation fails.
     */
    Transaction deleteTransactionById(int transactionId) throws SQLException;

}