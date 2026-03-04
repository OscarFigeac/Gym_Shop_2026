package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Subscription;

import java.util.List;

/**
 * Defines method signatures to support CRUD operations on subscriptions persistence.
 *
 * @author Cal Woods
 */
public interface SubscriptionDAO {
    /**
     * Creates a {@link Subscription} entity with parameter data for subscriptions persistence.
     * @param subscription Given created {@link Subscription} object
     * @return True if created successfully
     */
    boolean createSubscription(Subscription subscription);

    /**
     * Gets all stored subscription records for a {@link org.example.gym_shop_2026.entities.User} using userId.
     * @param userId Given id of user
     * @return Found {@link List<Subscription>} entities or blank list
     */
    List<Subscription> getSubscriptionsByUserId(int userId);

    /**
     * Retrieves a {@link List<Subscription>} with all stored subscription records.
     * @return {@link List<Subscription>} with all records or a blank {@link List<Subscription>}
     */
    List<Subscription> getAllSubscriptions();
    /**
     * Gets a {@link Subscription} entity from stored subscriptions persistence
     * @param planId Given {@link Subscription} plan id
     * @return Found stored record as {@link Subscription}
     */
    Subscription getSubscriptionById(int planId);

    /**
     * Replaces stored {@link Subscription} entity with new subscription
     * @param planId Given stored {@link Subscription} plan id
     * @param newSubscription Given new {@link Subscription} to replace old stored subscription
     * @return True if updated successfully
     */
    boolean updateSubscription(int planId, Subscription newSubscription);

    /**
     * Deletes stored {@link Subscription} entity from persistence.
     * @param planId Given plan id
     * @return Deleted stored {@link Subscription} entity or null if entity to delete not found
     */
    Subscription deleteSubscriptionByPlanId(int planId);
}
