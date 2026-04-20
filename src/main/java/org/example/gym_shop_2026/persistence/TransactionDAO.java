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
     * Gets all transactions with a given user id.
     * @param userId Given user_id.
     * @return {@link List<Transaction>} containing all transactions with user id
     * @throws SQLException If there is a communications link failure with sql
     */
    List<Transaction> getTransactionsByUserId(int userId) throws SQLException;
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
     * @return Number of rows affected by delete.
     * @throws SQLException If operation fails
     */
    int updateTransactionById(int transactionId, Transaction newTransaction) throws SQLException;

    /**
     * Deletes a transaction using transaction id.
     * @param transactionId Given transaction id, must be > 0
     * @return Deleted transaction information via a {@link Transaction}
     * object.
     * @throws SQLException If operation fails.
     */
    Transaction deleteTransactionById(int transactionId) throws SQLException;

    /**
     * @author oscar
     * Takes in an identifier string and looks for the transaction correlated to the
     * provided string.
     * @param stripeId the identifier being looked for
     * @return A Transaction object if found; null otherwise
     * @throws SQLException if the connection to the database fails at any point.
     */
    Transaction findTransactionByStripeId(String stripeId) throws SQLException;

    /**
     * @author oscar
     * Takes in a transaction to update its status (PENDING, SUCCESS, etc.)
     * @param txn The transaction object being updated
     * @return true if updated; false otherwise
     * @throws SQLException if the connection to the database fails at any point.
     */
    boolean updateTransaction(Transaction txn) throws SQLException;
}