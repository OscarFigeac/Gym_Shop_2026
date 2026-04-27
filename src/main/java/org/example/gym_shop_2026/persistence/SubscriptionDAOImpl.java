package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
//import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Subscription;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
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
    private final DataSource dataSource;

    public SubscriptionDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
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
        if(!validateSubscription(subscription)) {
            log.error("Could not perform create subscription operation as given subscription contained errors!");
            throw new IllegalArgumentException("Could not perform create subscription operation as given subscription contained errors!");
        }

        String sql = "INSERT INTO subscriptions(description, plan_name, plan_price, plan_duration) VALUES(?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subscription.getDescription());
            ps.setString(2, subscription.getPlanName());
            ps.setDouble(3, subscription.getPlanPrice());
            ps.setInt(4, subscription.getPlanDuration());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            log.error("Create subscription failed: {}", e.getMessage());
            throw new SQLException("Could not perform create subscription operation due to storage error!");
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
        String sql = "SELECT * FROM subscriptions";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                subscriptions.add(mapSubscriptionRow(rs));
            }
        } catch (SQLException e) {
            log.error("Error fetching all subscriptions: {}", e.getMessage());
            throw e;
        }
        return subscriptions;
    }

    /**
     * Gets a subscription from configured storage matching given planId value.
     * @param planId Given {@link Subscription} plan id
     * @return {@link Subscription} object containing found subscription data or null if not found
     * @throws SQLException If operation fails
     */
    @Override
    public Subscription getSubscriptionById(int planId) throws SQLException {
        if (!validatePlanId(planId)) {
            throw new IllegalArgumentException("PlanId must be greater than 0");
        }

        String sql = "SELECT * FROM subscriptions WHERE plan_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapSubscriptionRow(rs);
            }
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
    public boolean updateSubscription(int planId, Subscription newSubscription) throws SQLException {
        if (!validatePlanId(planId) || !validateSubscription(newSubscription)) {
            throw new IllegalArgumentException("Invalid planId or Subscription data");
        }

        String sql = "UPDATE subscriptions SET plan_name = ?, description = ?, plan_price = ?, plan_duration = ? WHERE plan_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newSubscription.getPlanName());
            ps.setString(2, newSubscription.getDescription());
            ps.setDouble(3, newSubscription.getPlanPrice());
            ps.setInt(4, newSubscription.getPlanDuration());
            ps.setInt(5, planId);

            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Deletes subscription from configured storage.
     * @param planId Given plan id
     * @return Deleted stored subscription if planId matches or null if not found.
     * @throws SQLException If operation fails
     */
    @Override
    public Subscription deleteSubscriptionByPlanId(int planId) throws SQLException {
        Subscription deletedSubscription = getSubscriptionById(planId);
        if (deletedSubscription == null) return null;

        String sql = "DELETE FROM subscriptions WHERE plan_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, planId);
            ps.executeUpdate();
        }
        return deletedSubscription;
    }

    /**
     * Maps results from given {@link ResultSet} to a {@link Subscription} object.
     * @param rs Given {@link ResultSet} object
     * @return {@link Subscription} containing {@link ResultSet} values or null if {@link ResultSet}
     * @throws SQLException
     */
    private Subscription mapSubscriptionRow(ResultSet rs) throws SQLException {
        return new Subscription(
                rs.getInt("plan_id"),
                rs.getString("plan_name"),
                rs.getString("description"),
                rs.getDouble("plan_price"),
                rs.getInt("plan_duration")
        );
    }

    /**
     * Validates given plan id.
     * @param planId Given plan id
     * @return True if given plan id is > 0, else false
     */
    private boolean validatePlanId(int planId) {
        return planId > 0;
    }

    /**
     * Validates given {@link Subscription} object.
     * @param subscription Given {@link Subscription} object
     * @return True if validation successful, else false
     */
    private boolean validateSubscription(Subscription subscription) {
        return subscription != null && subscription.getPlanName() != null;
    }
}