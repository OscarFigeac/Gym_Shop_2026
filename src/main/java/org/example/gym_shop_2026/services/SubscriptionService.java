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
     * Gets all subscriptions as {@link Subscription} objects
     * @return
     */
    public List<Subscription> getAllSubscriptions() {
        try {
            List<Subscription> subscriptions = subscriptionDAO.getAllSubscriptions();

            return subscriptions;
        } catch (SQLException e) {
            log.error("Could not perform get all subscriptions service operation as there was a problem! {}", e.toString());
            return new ArrayList<>();
        }
    }
}
