package org.example.gym_shop_2026.services;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.Subscription;
import org.example.gym_shop_2026.persistence.SubscriptionDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SubscriptionService {
    private SubscriptionDAO subscriptionDAO;

    public SubscriptionService(SubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    /**
     * Gets all subscriptions as {@link Subscription} objects.
     * @return {@link List<Subscription>} with all found subscriptions
     */
    public List<Subscription> getAllSubscriptions() {
        try {
            List<Subscription> subscriptions = subscriptionDAO.getAllSubscriptions();

            return subscriptions;
        }
        catch (SQLException e) {
            log.error("Could not perform get all subscriptions service operation as there was a problem! {}", e.toString());
            return new ArrayList<>();
        }
    }

    /**
     * Gets {@link Subscription} with a given plan id for viewing, deleting.
     * @param planId Given subscription plan id
     * @return True if subscription 
     * @throws SQLException
     */
    public Subscription getSubscriptionPlanById(int planId) throws SQLException {
        if(planId < 1) {
            log.error("Could not perform get subscription service operation as planId was less than 1");
            throw new IllegalArgumentException("Plan id must be greater than or equal to 1.");
        }

        return subscriptionDAO.deleteSubscriptionByPlanId(planId);
    }

    /**
     * Updates stored subscription with data from a given {@link Subscription} object.
     * @param subscription Given{@link Subscription} object
     * @return True if update successful, else false
     * @throws SQLException
     */
    public boolean updateSubscription(Subscription subscription) throws SQLException {
        if(subscription == null) {
            log.error("Could not perform update subscription service operation as subscription was null");
        }

        boolean result = false;

        //Get subscription by plan id
        Subscription found = subscriptionDAO.getSubscriptionById(subscription.getPlanId());
        if(found == null) {
            log.info("Could not perform update subscription service operation as given subscription did not exist!");
            return result;
        }

        if(found.getPlanId() != subscription.getPlanId()) {
            log.error("Could not perform update operation as new subscription plan id did not match found subscription plan id!");
            return result;
        }

        result = subscriptionDAO.updateSubscription(found.getPlanId(), subscription);

        return result;
    }

    /**
     * Deletes a stored subscription plan with a given plan id.
     * @param planId Given plan id
     * @return Deleted subscription information as {@link Subscription} object or null if
     * subscription was not found
     * @throws SQLException
     */
    public Subscription deleteSubscriptionByPlanId(int planId) throws SQLException {
        if(planId < 1) {
            log.error("Could not perform delete subscription by plan id service operation as subscription was less than 1!");
            throw new IllegalArgumentException("Subscription plan id must be greater than or equal to 1!");
        }

        Subscription deleted = subscriptionDAO.deleteSubscriptionByPlanId(planId);

        return deleted;
    }
}
