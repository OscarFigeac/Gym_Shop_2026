package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Subscription;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A persistence layer class that implements interface {@link SubscriptionDAO},
 *
 * @author Cal Woods
 */
@Repository
@Slf4j
public class SubscriptionDAOImpl implements SubscriptionDAO {
    Connector connector;

    public SubscriptionDAOImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public boolean createSubscription(Subscription subscription) {
        return false;
    }

    @Override
    public List<Subscription> getSubscriptionsByUserId(int userId) {
        return List.of();
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return List.of();
    }

    @Override
    public Subscription getSubscriptionById(int planId) {
        return null;
    }

    @Override
    public boolean updateSubscription(int planId, Subscription newSubscription) {
        return false;
    }

    @Override
    public Subscription deleteSubscriptionByPlanId(int planId) {
        return null;
    }
}