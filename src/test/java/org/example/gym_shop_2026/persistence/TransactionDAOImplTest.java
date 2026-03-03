package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.configs.ConnectorTestConfig;
import org.example.gym_shop_2026.entities.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDateTime;

@SpringBootTest(classes = {ConnectorTestConfig.class, TransactionDAOImpl.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TransactionDAOImplTest {
    @Autowired
    private TransactionDAOImpl transactionDAOImpl;

    @Test
    void createTransactionWithNullTransactionObjectParameterThrowsIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionDAOImpl.createTransaction(null);
        });
    }

    @Test
    void createTransactionWithTransactionObjectWithBadIdNoUsersThrowsSQLException() {
        Transaction transaction = new Transaction(-1, 1, 1, 1, 350.00, LocalDateTime.now(), "Complete");

        Assertions.assertThrows(SQLException.class, () -> {
            System.out.println(transactionDAOImpl.createTransaction(transaction));
        });
    }

    @Test
    void createTransactionWithTransactionObjectWithBadDataAndExistingUsersThrowsSQLException() {
        Transaction transaction = new Transaction(1, 1, 1, 1, 350.00, LocalDateTime.now(), "Complete");

        Assertions.assertThrows(SQLException.class, () -> {
            System.out.println(transactionDAOImpl.createTransaction(transaction));
        });
    }

    @Test
    void getTransactionsDoesNotThrowExceptionWithEmptyDatabaseTransactionsTableAndReturnsBlankListOfTransactions() {
        Assertions.assertDoesNotThrow(() -> {
            transactionDAOImpl.getTransactions();
        });
    }

    @Test
    void getTransactionsReturnsEmptyListOfTransactionsWhenNoTransactionsExist() throws SQLException {
        Assertions.assertTrue(transactionDAOImpl.getTransactions().isEmpty());
    }

    @Test
    void findTransactionByIdReturnsNullWhenTransactionDoesNotExist() throws SQLException {
        Assertions.assertNull(transactionDAOImpl.findTransactionById(1));
    }

    @Test
    void findTransactionByIdThrowsIllegalArgumentExceptionWhenBadTransactionIdIsProvided() throws SQLException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionDAOImpl.findTransactionById(-1);
        });
    }

    @Test
    void updateTransactionByIdReturnsZeroValueWhenTransactionDoesNotExist() throws SQLException {
        Assertions.assertTrue(transactionDAOImpl.updateTransactionById(1, new Transaction(1, 1, 1, 1, 350.00, LocalDateTime.now(), "Complete")) == 0);
    }

    @Test
    void updateTransactionByIdThrowsIllegalArgumentExceptionWhenGivenNewTransactionIsNull() throws SQLException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionDAOImpl.updateTransactionById(1, null);
        });
    }

    @Test
    void updateTransactionByIdThrowsIllegalArgumentExceptionWhenGivenTransactionIdIsNegative() throws SQLException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionDAOImpl.updateTransactionById(-1, new Transaction(1, 1, 1, 1, 350.00, LocalDateTime.now(), "Complete"));
        });
    }

    @Test
    void updateTransactionByIdThrowsSQLExceptionWhenNewTransactionValueDependencyDoesNotExist() throws SQLException {
        Transaction transaction = new Transaction(1, 1, 1, 1, 350.00, LocalDateTime.now(), "Complete");
        Assertions.assertTrue(transactionDAOImpl.updateTransactionById(1, transaction) == 0);
    }

    @Test
    void deleteTransactionByIdThrowsIllegalArgumentExceptionWhenGivenTransactionIdIsNegative() throws SQLException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionDAOImpl.deleteTransactionById(-1);
        });
    }

    @Test
    void deleteTransactionByIdThrowsSQLExceptionWhenGivenTransactionIdDoesNotExist() throws SQLException {
        Assertions.assertThrows(SQLException.class, () -> {
            transactionDAOImpl.deleteTransactionById(1);
        });
    }
}