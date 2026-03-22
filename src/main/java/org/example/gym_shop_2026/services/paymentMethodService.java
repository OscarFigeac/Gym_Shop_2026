package org.example.gym_shop_2026.services;

import org.example.gym_shop_2026.entities.paymentMethod;
import org.example.gym_shop_2026.persistence.paymentMethodDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class paymentMethodService {
    private final paymentMethodDAO paymentMethodDao;

    public paymentMethodService(paymentMethodDAO paymentMethodDao) {
        this.paymentMethodDao = paymentMethodDao;
    }

    public boolean addPaymentMethod(paymentMethod method) throws SQLException {
        if (paymentMethodDao.isPaymentTokenUnique(method.getProcessorToken())) {
            return paymentMethodDao.insertPaymentMethod(method);
        }
        return false;
    }

    /**
     * Calls insertPaymentMethodTesting() method from {@link paymentMethodDAO}
     * @param method {@link paymentMethod} to insert
     * @return True if dao method successful
     * @throws SQLException If dao method fails
     */
    public boolean addPaymentMethodTest(paymentMethod method) throws SQLException {
        if (paymentMethodDao.isPaymentTokenUnique(method.getProcessorToken())) {
            return paymentMethodDao.insertPaymentMethodTesting(method);
        }
        return false;
    }

    public List<paymentMethod> getAllUserMethods(int userId) throws SQLException {
        return paymentMethodDao.findAllMethodsByUserId(userId);
    }

    public paymentMethod getMethodDetails(int methodId) throws SQLException {
        return paymentMethodDao.findMethodById(methodId);
    }

    public paymentMethod getPrimaryMethod(int userId) throws SQLException {
        return paymentMethodDao.findPrimaryMethodByUserId(userId);
    }

    public boolean canUserMakePayment(int userId) throws SQLException {
        return paymentMethodDao.isUserPaymentMethodAvailable(userId);
    }

    public boolean updateMethod(paymentMethod method) throws SQLException {
        return paymentMethodDao.updatePaymentMethod(method);
    }

    public boolean toggleMethodStatus(int methodId, boolean isValid) throws SQLException {
        return paymentMethodDao.updateMethodValidity(methodId, isValid);
    }

    public paymentMethod deletePaymentMethod(int id) throws SQLException {
        return paymentMethodDao.deleteMethodById(id);
    }

    /**
     * Deletes payment method with payment method id without autocommitting for testing.
     * @param id Payment method id to delete
     * @return {@link paymentMethod} if found, else null.
     * @throws SQLException If deletion fails
     */
    public paymentMethod deletePaymentMethodTest(int id) throws SQLException {
        return paymentMethodDao.deleteMethodByIdTesting(id);
    }
}
