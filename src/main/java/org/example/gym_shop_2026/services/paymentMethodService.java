package org.example.gym_shop_2026.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.paymentMethod;
import org.example.gym_shop_2026.persistence.paymentMethodDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class paymentMethodService {
    private final paymentMethodDAO paymentMethodDao;
    private final UserService userService;

    public paymentMethodService(paymentMethodDAO paymentMethodDao, UserService userService) {
        this.paymentMethodDao = paymentMethodDao;
        this.userService = userService;
    }

    public boolean addPaymentMethod(paymentMethod method) throws SQLException, StripeException {
        User user = userService.getUserById(method.getUserId());
        String stripeCustomerId = user.getStripeCustomerId();

        if (stripeCustomerId == null || stripeCustomerId.isEmpty()) {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(user.getEmail())
                    .setName(user.getFullName())
                    .build();
            Customer customer = Customer.create(customerParams);
            stripeCustomerId = customer.getId();

            userService.updateStripeCustomerId(user.getUser_id(), stripeCustomerId);
        }

        PaymentMethod pm = PaymentMethod.retrieve(method.getStripePaymentMethodId());
        pm.attach(PaymentMethodAttachParams.builder()
                .setCustomer(stripeCustomerId)
                .build());

        return paymentMethodDao.insertPaymentMethod(method);
    }

//    /**
//     * Calls insertPaymentMethodTesting() method from {@link paymentMethodDAO}
//     * @param method {@link paymentMethod} to insert
//     * @return True if dao method successful
//     * @throws SQLException If dao method fails
//     */
//    public boolean addPaymentMethodTest(paymentMethod method) throws SQLException {
//        if (paymentMethodDao.isPaymentTokenUnique(method.getProcessorToken())) {
//            return paymentMethodDao.insertPaymentMethodTesting(method);
//        }
//        return false;
//    }

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

    public paymentMethod deletePaymentMethod(int id) throws SQLException, StripeException {
        paymentMethod method = paymentMethodDao.findMethodById(id);
        if (method != null) {
            PaymentMethod pm = PaymentMethod.retrieve(method.getStripePaymentMethodId());
            pm.detach();

            return paymentMethodDao.deleteMethodById(id);
        }
        return null;
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
