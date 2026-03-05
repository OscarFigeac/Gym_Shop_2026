package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Subscription;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    /**
     * Creates new {@link Subscription} record in configured connection storage.
     * @param subscription Given created {@link Subscription} object
     * @return True if operation successful
     *
     * @throws SQLException If operation fails
     */
    @Override
    public boolean createSubscription(Subscription subscription) throws SQLException {
        if(validateSubscription(subscription) == false) {
            log.error("Could not perform create subscription operation as given subscription contained errors! See logs.");
        }
        int rowsAffected = 0;

        try(PreparedStatement ps = connector.getConnection().prepareStatement("INSERT INTO subscriptions(description, plan_name, plan_price, plan_duration)" +
                "VALUES(?, ?, ?, ?)")) {
            ps.setString(1, subscription.getDescription());
            ps.setString(2, subscription.getPlanName());
            ps.setDouble(3, subscription.getPlanPrice());
            ps.setInt(4, subscription.getPlanDuration());

            try {
                rowsAffected = ps.executeUpdate();
                return rowsAffected == 1;
            }
            catch(SQLException e) {
                log.error("Could not perform create subscription operation as update failed to execute! {}", e.toString());
                throw new SQLException("Could not perform create subscription operation as update failed to execute!");
            }
        }
        catch(SQLException e) {
            log.error("Could not perform create subscription operation as there was a problem inserting into configured storage! {}", e.toString());
            throw new SQLException("Could not perform create subscription operation as there was a problem with inserting into configured storage!");
        }
    }

    /**
     * Gets all subscriptions from configured connection.
     * @return {@link List<Subscription>} of found subscriptions or a blank {@link List<Subscription>}
     * @throws SQLException If operation fails
     */
    @Override
    public List<Subscription> getAllSubscriptions() throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();

        try(PreparedStatement ps = connector.getConnection().prepareStatement("SELECT * FROM subscriptions")) {
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    subscriptions.add(mapSubscriptionRow(rs));
                }
                return subscriptions;
            }
            catch(SQLException e) {
                log.error("Could not perform get all subscriptions as there was a problem getting the information from connection! {}", e.toString());
                throw e;
            }
        }
        catch(SQLException e) {
            log.error("Could not get all subscriptions from configured storage! {}", e.toString());
            throw e;
        }
    }

    /**
     * Gets a subscription from configured storage matching given planId value.
     * @param planId Given {@link Subscription} plan id
     * @return {@link Subscription} object containing found subscription data or null if not found
     * @throws SQLException If operation fails
     */
    @Override
    public Subscription getSubscriptionById(int planId) throws SQLException {
        if (validatePlanId(planId) == false) {
            log.error("Could not perform get subscription by id operation as given planId < 1!");
            throw new IllegalArgumentException("Could not perform get subscription by id operation as given planId < 1!");
        }

        try(PreparedStatement ps = connector.getConnection().prepareStatement("SELECT * FROM subscriptions WHERE plan_id = ?")) {
            ps.setInt(1, planId);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapSubscriptionRow(rs);
                }
            }
            catch(SQLException e) {
                log.error("Could not perform get subscription by id! {}", e.toString());
                throw e;
            }
        }
        catch(SQLException e) {
            log.error("Could not perform get subscription by id due to query error! {}", e.toString());
            throw e;
        }

        return null;
    }

    /**
     * Updates a subscription in configured storage.
     * @param planId Given stored {@link Subscription} plan id
     * @param newSubscription Given new {@link Subscription} to replace old stored subscription
     * @return True if update successful
     *
     * @throws SQLException If operation fails
     *
     * @implNote Does not update stored plan id, but updates everything else to newSubscription values
     */
    @Override
    public boolean updateSubscription(int planId, Subscription newSubscription) {
        return false;
    }

    /**
     * Deletes subscription from configured storage.
     * @param planId Given plan id
     * @return Deleted stored subscription if planId matches or null if not found.
     * @throws SQLException If operation fails
     */
    @Override
    public Subscription deleteSubscriptionByPlanId(int planId) {
        return null;
    }

    /**
     * Maps results from given {@link ResultSet} to a {@link Subscription} object.
     * @param rs Given {@link ResultSet} object
     * @return {@link Subscription} containing {@link ResultSet} values or null if {@link ResultSet}
     * @throws SQLException
     */
    private Subscription mapSubscriptionRow(ResultSet rs) throws SQLException {
        if(rs == null) {
            log.error("Could not map subscription object row as Subscription object was null!");
            throw new IllegalArgumentException("Could not map subscription object row as Subscription object was null!");
        }
        if(rs.isClosed()) {
            log.error("Could not map subscription object row as Subscription object was closed!");
            throw new IllegalArgumentException("Could not map subscription object row as Subscription object was closed!");
        }

        Subscription subscription = new Subscription(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getDouble(4),
                rs.getInt(5)
        );

        return subscription;
    }

    /**
     * Validates given plan id.
     * @param planId Given plan id
     * @return True if given plan id is > 0, else false
     */
    private static boolean validatePlanId(int planId) {
        if(planId <= 0) {
            log.error("Could not validate given planId as it was less than zero!");
        }

        return planId > 0;
    }

    /**
     * Validates given {@link Subscription} object.
     * @param subscription Given {@link Subscription} object
     * @return True if validation successful, else false
     */
    private static boolean validateSubscription(Subscription subscription) {
        if(subscription == null) {
            log.error("Could not validate given subscription as it was null!");
            return false;
        }
        if(validatePlanId(subscription.getPlanId())) {
            log.error("Given subscription contains bad planId value < 1!");
            return false;
        }

        return true;
    }
}