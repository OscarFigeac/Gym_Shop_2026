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
     */
    @Override
    public boolean createSubscription(Subscription subscription) throws SQLException {
        if(subscription == null) {
            log.error("Could not perform create subscription operation as given new Subscription object was null!");
            throw new IllegalArgumentException("Could not perform create subscription operation as given new Subscription object was null!");
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
}