package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.paymentMethod;

import java.sql.SQLException;
import java.util.List;

public interface paymentMethodDAO {
    paymentMethod findMethodById(int id) throws SQLException;
    boolean isUserPaymentMethodAvailable(int userId) throws SQLException;
    boolean isPaymentTokenUnique(String processorToken) throws SQLException;
    paymentMethod findPrimaryMethodByUserId(int userId) throws SQLException;
    List<paymentMethod> findAllMethodsByUserId(int userId) throws SQLException;
    boolean insertPaymentMethod(paymentMethod newMethod) throws SQLException;
    boolean insertPaymentMethodTesting(paymentMethod newMethod) throws SQLException;
    boolean updatePaymentMethod(paymentMethod method) throws SQLException;
    boolean updateMethodValidity(int methodId, boolean isValid) throws SQLException;
    paymentMethod deleteMethodById(int id) throws SQLException;
    paymentMethod deleteMethodByIdTesting(int id) throws SQLException;
}
