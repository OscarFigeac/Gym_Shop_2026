package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
//import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Transaction;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TransactionDAOImpl implements TransactionDAO {
    private final DataSource dataSource;

    public TransactionDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates new transaction in database using a custom {@link Transaction} object.
     * @param newTransaction {@link Transaction} object to use for adding record
     * @return True if {@link Transaction} object inserted successfully
     * @throws SQLException If there is a problem cvommunicating with database
     *
     * @implNote Ignores {@link Transaction} object's int transaction id and
     * {@link LocalDateTime} transactionDate attributes as they are dependent on database & this
     * method
     */
    @Override
    public boolean createTransaction(Transaction newTransaction) throws SQLException {
        if (newTransaction == null) throw new IllegalArgumentException("Transaction object was null!");

        String sql = "INSERT INTO transactions(user_id, plan_id, method_id, amount_paid, transaction_date, status, stripe_payment_intent_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newTransaction.getUserId());
            ps.setInt(2, newTransaction.getPlanId());
            ps.setInt(3, newTransaction.getMethodId());
            ps.setDouble(4, newTransaction.getAmountPaid());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, newTransaction.getStatus());
            ps.setString(7, newTransaction.getStripePaymentIntentId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Create transaction failed: {}", e.getMessage());
            throw new SQLException("Database error creating transaction.");
        }
    }

    @Override
    public List<Transaction> getTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapTransactionRow(rs));
            }
        }
        return transactions;
    }

    @Override
    public Transaction findTransactionById(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapTransactionRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapTransactionRow(rs));
                }
            }
        }
        return transactions;
    }

    @Override
    public boolean updateTransaction(Transaction txn) throws SQLException {
        if (txn == null || txn.getTransactionId() <= 0) throw new IllegalArgumentException("Invalid Transaction");

        String sql = "UPDATE transactions SET status = ?, stripe_payment_intent_id = ? WHERE transaction_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txn.getStatus());
            ps.setString(2, txn.getStripePaymentIntentId());
            ps.setInt(3, txn.getTransactionId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Transaction deleteTransactionById(int transactionId) throws SQLException {
        Transaction found = findTransactionById(transactionId);
        if (found == null) return null;

        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        }
        return found;
    }

    @Override
    public Transaction findTransactionByStripeId(String stripeId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE stripe_payment_intent_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stripeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapTransactionRow(rs);
            }
        }
        return null;
    }

    /**
     * Helper that maps transaction record data from database to a {@link Transaction} object.
     * @param rs Given {@link ResultSet} object, to be passed from other methods,
     * should only contain transaction record data
     * @return A {@link Transaction} object with data from {@link ResultSet} or null if operation
     * fails.
     */
    private Transaction mapTransactionRow(ResultSet rs) throws SQLException {
        return Transaction.builder()
                .transactionId(rs.getInt("transaction_id"))
                .userId(rs.getInt("user_id"))
                .planId(rs.getInt("plan_id"))
                .methodId(rs.getInt("method_id"))
                .amountPaid(rs.getDouble("amount_paid"))
                .transactionDate(rs.getTimestamp("transaction_date").toLocalDateTime())
                .status(rs.getString("status"))
                .stripePaymentIntentId(rs.getString("stripe_payment_intent_id"))
                .build();
    }
}