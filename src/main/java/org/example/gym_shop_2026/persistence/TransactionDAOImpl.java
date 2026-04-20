package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TransactionDAOImpl implements TransactionDAO {
    private Connector connector;

    public TransactionDAOImpl(Connector connector) {
        this.connector = connector;
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
        if(newTransaction == null) {
            throw new IllegalArgumentException("Transaction object was null!");
        }

            String sql = "INSERT INTO transactions(user_id, plan_id, method_id, amount_paid, transaction_date, status, stripe_payment_intent_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, newTransaction.getUserId());
            ps.setInt(2, newTransaction.getPlanId());
            ps.setInt(3, newTransaction.getMethodId());
            ps.setDouble(4, newTransaction.getAmountPaid());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, newTransaction.getStatus());
            ps.setString(7, newTransaction.getStripePaymentIntentId()); // New Field

            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) {
            log.error("Create transaction failed! {}", e);
            throw new SQLException("Create transaction failed!");
        }
    }

    @Override
    public List<Transaction> getTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        try(PreparedStatement ps = connector.getConnection().prepareStatement("SELECT * FROM transactions")) {
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapTransactionRow(rs));
                }
            }
            catch (SQLException e) {
                log.error("Could not perform get transactions operation as there was a problem preparing sql statement! {}", e);
            }
        }
        catch (SQLException e) {
            log.error("Could not perform get transactions operation! {}", e);
        }

        return transactions;
    }

    @Override
    public Transaction findTransactionById(int transactionId) throws SQLException {
        if(transactionId <= 0) {
            log.error("Transaction id {} was invalid!", transactionId);
            throw new IllegalArgumentException("Could not perform find transaction operation as given Transaction id " + transactionId + " was invalid!");
        }

        try(PreparedStatement ps = connector.getConnection().prepareStatement("SELECT * FROM transactions WHERE transaction_id = ?")) {
            ps.setInt(1, transactionId);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTransactionRow(rs);
                }
            }
            catch (SQLException e) {
                log.error("sql SELECT failed! {}", e);
                throw new SQLException("Could not perform find transaction operation as sql operation failed!");
            }
        }
        catch (SQLException e) {
            log.error("Could not perform find transaction by id operation! {}", e);
            throw new SQLException("Could not perform find transaction by id operation! ");
        }

        return null;
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException {
        if(userId <= 0) {
            log.error("Could not perform get transactions by user id operation as given User id {} was <= 0!", userId);
            throw new IllegalArgumentException("Could not perform get transactions by user id operation as given user id " + userId + " was <= 0!");
        }

        List<Transaction> transactions = new ArrayList<>();

        try(PreparedStatement ps = connector.getConnection().prepareStatement("SELECT * FROM transactions WHERE user_id = ?")) {
            ps.setInt(1, userId);

            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapTransactionRow(rs));
                }
            }
            catch (SQLException e) {
                log.error("Could not perform get transactions by id operation as there was a problem! {}", e.toString());
                throw new SQLException("Could not perform get transactions by id operation as there was a problem!");
            }
        }
        catch (SQLException e) {
            log.error("Could not perform get transactions by id operation as there was a problem! {}", e.toString());
            throw new SQLException("Could not perform get transactions by id operation as there was a problem!");
        }

        return transactions;
    }

    @Override
    public int updateTransactionById(int transactionId, Transaction newTransaction) throws SQLException {
        if(transactionId <= 0) {
            log.error("Could not perform update transaction by id operation as given transaction id {} was invalid!", transactionId);
            throw new IllegalArgumentException("Could not perform update transaction by id operation as given transaction id " + transactionId + " was invalid!");
        }
        if(newTransaction == null) {
            log.error("Could not perform update transaction by id operation as given new transaction object was null!");
            throw new IllegalArgumentException("Given new transaction object was null!");
        }

        String sql = "UPDATE transactions SET status = ?, stripe_payment_intent_id = ? WHERE transaction_id = ?";
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setString(1, newTransaction.getStatus());
            ps.setString(2, newTransaction.getStripePaymentIntentId());
            ps.setInt(3, transactionId);
            return ps.executeUpdate();
        }
        catch(SQLException e) {
            log.error("Could not update transaction! {}", e);
            throw new SQLException("Update failed.");
        }
    }

    @Override
    public boolean updateTransaction(Transaction txn) throws SQLException {
        if (txn == null || txn.getTransactionId() <= 0) {
            throw new IllegalArgumentException("Invalid Transaction object for update");
        }

        String sql = "UPDATE transactions SET status = ?, stripe_payment_intent_id = ? WHERE transaction_id = ?";

        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setString(1, txn.getStatus());
            ps.setString(2, txn.getStripePaymentIntentId());
            ps.setInt(3, txn.getTransactionId());

            return ps.executeUpdate() > 0;
        }
        catch(SQLException e) {
            log.error("Could not update transaction! {}", e);
            throw new SQLException("Update failed.");
        }
    }

    @Override
    public Transaction deleteTransactionById(int transactionId) throws SQLException {
        if(transactionId <= 0) {
            log.error("Could not perform delete transaction operation as given transaction id {} was invalid!", transactionId);
            throw new IllegalArgumentException("Given transaction id " + transactionId + " was invalid!");
        }

        Transaction foundTransaction = findTransactionById(transactionId);
        if(foundTransaction == null) {
            log.error("Could not perform delete operation as given transaction id {} was not found!", transactionId);
            throw new SQLException("Given transaction id " + transactionId + " was not found!");
        }

        try(PreparedStatement ps = connector.getConnection().prepareStatement("DELETE FROM transactions WHERE transaction_id = ?")) {
            int rowsAffected = ps.executeUpdate();

            if(rowsAffected == 0) {
                log.error("Could not perform delete transaction by id operation as there was no transaction with id {}!", transactionId);
                return null;
            }

            return foundTransaction;
        }
        catch(SQLException e) {
            log.error("Could not perform delete transaction by id operation! {}", e);
            throw new SQLException("Could not perform delete transaction by id operation! ");
        }
    }

    @Override
    public Transaction findTransactionByStripeId(String stripeId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE stripe_payment_intent_id = ?";
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setString(1, stripeId);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTransactionRow(rs);
                }
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
    private Transaction mapTransactionRow(ResultSet rs) {
        if(rs == null) return null;

        try {
            return Transaction.builder()
                    .transactionId(rs.getInt(1))
                    .userId(rs.getInt(2))
                    .planId(rs.getInt(3))
                    .methodId(rs.getInt(4))
                    .amountPaid(rs.getDouble(5))
                    .transactionDate(rs.getTimestamp(6).toLocalDateTime())
                    .status(rs.getString(7))
                    .stripePaymentIntentId(rs.getString(8))
                    .build();
        }
        catch (SQLException e) {
            log.error("Could not map transaction object! {}", e);
            return null;
        }
    }
}