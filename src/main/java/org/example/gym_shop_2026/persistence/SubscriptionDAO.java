package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Subscription;

import java.sql.SQLException;
import java.util.List;

/**
 * Defines method signatures to support CRUD operations on subscriptions persistence database.
 *
 * @author Cal Woods
 */
public interface SubscriptionDAO {
    /**
     * Creates a {@link Subscription} entity with parameter data for subscriptions persistence.
     * @param subscription Given created {@link Subscription} object
     * @return True if created successfully
     *
     * @throws SQLException
     */
    boolean createSubscription(Subscription subscription) throws SQLException;

    /**
     * Retrieves a {@link List<Subscription>} with all stored subscription records.
     * @return {@link List<Subscription>} with all records or a blank {@link List<Subscription>}
     *
     * @throws SQLException
     */
    List<Subscription> getAllSubscriptions() throws SQLException;
    /**
     * Gets a {@link Subscription} entity from stored subscriptions persistence
     * @param planId Given {@link Subscription} plan id
     * @return Found stored record as {@link Subscription}
     *
     * @throws SQLException
     */
    Subscription getSubscriptionById(int planId) throws SQLException;

    /**
     * Replaces stored {@link Subscription} entity with new subscription
     * @param planId Given stored {@link Subscription} plan id
     * @param newSubscription Given new {@link Subscription} to replace old stored subscription
     * @return True if updated successfully
     *
     * @throws SQLException
     */
    boolean updateSubscription(int planId, Subscription newSubscription) throws SQLException;

    /**
     * Deletes stored {@link Subscription} entity from persistence.
     * @param planId Given plan id
     * @return Deleted stored {@link Subscription} entity or null if entityto delete not found
     *
     * @throws SQLException
     */
    Subscription deleteSubscriptionByPlanId(int planId) throws SQLException;
}