package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;
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

    @Override
    public boolean createTransaction(Transaction newTransaction) throws SQLException {
        if(newTransaction == null) {
            log.error("Could not perform create transaction operation as given Transaction object was null!");
            throw new IllegalArgumentException("Could not perform create transaction operation as given Transaction object was null!");
        }


        try(PreparedStatement ps = connector.getConnection().prepareStatement("INSERT INTO transactions " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            ps.setInt(1, newTransaction.getTransactionId());
            ps.setInt(2, newTransaction.getUserId());
            ps.setInt(3, newTransaction.getPlanId());
            ps.setInt(4, newTransaction.getMethodId());
            ps.setDouble(5, newTransaction.getAmountPaid());
            ps.setTimestamp(6, Timestamp.valueOf(newTransaction.getTransactionDate()));
            ps.setString(7, newTransaction.getStatus());

            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) {
            log.error("Could not perform create transaction operation as sql insert failed! {}", e);
            throw new SQLException("Could not perform create transaction operation as sql insert failed!");
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
                throw new SQLException("Could not perform get transactions operation as there was a problem preparing sql statement!");
            }
        }
        catch (SQLException e) {
            log.error("Could not perform get transactions operation! {}", e);
            throw new SQLException("Could not perform get transactions operation! ");
        }

        return transactions;
    }

    @Override
    public Transaction findTransactionById(int transactionId) throws SQLException {
        if(transactionId <= 0) {
            log.error("Could not perform find transaction operation as given Transaction id {} was invalid!", transactionId);
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
                log.error("Could not perform find transaction by id operation as sql SELECT failed! {}", e);
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
    public int updateTransactionById(int transactionId, Transaction newTransaction) throws SQLException {
        if(transactionId <= 0) {
            log.error("Could not perform update transaction by id operation as given transaction id {} was invalid!", transactionId);
            throw new IllegalArgumentException("Could not perform update transaction by id operation as given transaction id " + transactionId + " was invalid!");
        }
        if(newTransaction == null) {
            log.error("Could not perform update transaction by id operation as given new transaction object was null!");
            throw new IllegalArgumentException("Given new transaction object was null!");
        }

        try(PreparedStatement ps = connector.getConnection().prepareStatement("UPDATE transactions SET status = ? WHERE transaction_id = ?")) {
            ps.setString(1, newTransaction.getStatus());
            ps.setInt(2, transactionId);

            return ps.executeUpdate();
        }
        catch(SQLException e) {
            log.error("Could not perform update transaction by id operation! {}", e);
            throw new SQLException("Could not perform update transaction by id operation as prepared statement threw exception!" + e);
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

    /**
     * Helper that maps transaction record data from database to a {@link Transaction} object.
     * @param rs Given {@link ResultSet} object, to be passed from other methods,
     * should only contain transaction record data
     * @return A {@link Transaction} object with data from {@link ResultSet} or null if operation
     * fails.
     */
    private Transaction mapTransactionRow(ResultSet rs) {
        if(rs == null) {
            log.error("Could not map transaction object as given ResultSet was null!");
            return null;
        }

        try {
            Transaction transaction = new Transaction(rs.getInt(1),
                    rs.getInt(2),
                    rs.getInt(3),
                    rs.getInt(4),
                    rs.getDouble(5),
                    rs.getTimestamp(6).toLocalDateTime(),
                    rs.getString(7));

            return transaction;
        }
        catch (SQLException e) {
            log.error("Could not map transaction object! {}", e);
            return null;
        }
    }
}