package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
//import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.paymentMethod;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class paymentMethodDAOImpl implements paymentMethodDAO{
    private final DataSource dataSource;

    public paymentMethodDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static paymentMethod mapPaymentRow(ResultSet rs) throws SQLException {
        return new paymentMethod(
                rs.getInt("method_id"),
                rs.getInt("user_id"),
                rs.getString("stripe_payment_method_id"),
                rs.getString("last_four_digits"),
                rs.getString("expiry_date"),
                rs.getString("card_type"),
                rs.getBoolean("is_valid"),
                rs.getBoolean("is_primary")
        );
    }


    /**
     * Finds a specific payment method by the ID
     * <p>
     * Takes in an int ID from the user and searches through the payment method database for a payment method with a corresponding ID
     *
     * @param id ID of Payment Method being searched for
     *
     * @return NULL if ID doesn't return a payment method within the library or the payment method with the corresponding ID.
     *
     * @author Oscar Figeac
     */
    public paymentMethod findMethodById(int id) throws SQLException {
        paymentMethod method = null;
        String sql = "SELECT * FROM payment_methods WHERE method_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) method = mapPaymentRow(rs);
            } catch (SQLException e) {
                log.error("findMethodById(): Error processing result set. \nException: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findMethodById(): SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return method;
    }

    /**
     * Finds the availability of payment methods for a specified user
     * <p>
     * Takes in an int User ID from the user and searches through the payment method database for a payment method available for the user in question
     *
     * @param userId ID of user who's payment method availability is being checked
     *
     * @return False if ID doesn't return a payment method isn't available and True if it is
     *
     * @author Oscar Figeac
     */
    public boolean isUserPaymentMethodAvailable(int userId) throws SQLException {
        boolean available = false;
        String sql = "SELECT EXISTS(SELECT 1 FROM payment_methods WHERE user_id = ? AND is_valid = TRUE) AS available";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) available = rs.getBoolean("available");
            } catch (SQLException e) {
                log.error("isUserPaymentMethodAvailable(): Error processing result set. \nException: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("isUserPaymentMethodAvailable(): SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return available;
    }

    /**
     * Checks if a payment token is unique
     * <p>
     * Takes in a token and iterates through the payment method database for any matching tokens to ensure that the entered one is definitely unqiue
     *
     * @param processorToken String token that may or may not be unique
     *
     * @return False if the token already exists and TRUE if it doesn't
     *
     * @author Oscar Figeac
     */
    public boolean isPaymentTokenUnique(String processorToken) throws SQLException {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM payment_methods WHERE processor_token = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, processorToken);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) exists = true;
            } catch (SQLException e) {
                log.error("isPaymentTokenUnique(): Error processing result set. \nException: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("isPaymentTokenUnique(): SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return !exists;
    }

    /**
     * Finds all the Payment Methods linked to a user ID
     * <p>
     * Takes in the ID of a user and searches to see all the payment methods linked to that user and returns them in a list
     *
     * @param userId ID of the user who's payment methods are being searched for
     *
     * @return Empty list if there are no linked methods and a full list if there are methods linked
     *
     * @author Oscar Figeac
     */
    public List<paymentMethod> findAllMethodsByUserId(int userId) throws SQLException {
        List<paymentMethod> methods = new ArrayList<>();
        String sql = "SELECT * FROM payment_methods WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) methods.add(mapPaymentRow(rs));
            } catch (SQLException e) {
                log.error("findAllMethodsByUserId(): Error processing result set. \nException: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findAllMethodsByUserId(): SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return methods;
    }

    /**
     * Inserts a new payment method into the database
     * <p>
     * Takes in a new payment method entity and inserts it into the database
     *
     * @param p New Payment Method being added
     *
     * @return 0 if the insertion fails and 1 if it is a success
     *
     * @author Oscar Figeac
     */
    public boolean insertPaymentMethod(paymentMethod p) throws SQLException {
        if (p == null) throw new IllegalArgumentException("Cannot insert null payment method");
        int rows = 0;
        String sql = "INSERT INTO payment_methods (user_id, stripe_payment_method_id, last_four_digits, expiry_date, card_type, is_valid, is_primary) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getStripePaymentMethodId());
            ps.setString(3, p.getLastFourDigits());
            ps.setString(4, p.getExpiryDate());
            ps.setString(5, p.getCardType());
            ps.setBoolean(6, p.isValid());
            ps.setBoolean(7, p.isPrimary());
            rows = ps.executeUpdate();
        } catch (SQLException e) {
            log.error("insertPaymentMethod(): SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return rows == 1;
    }

    /**
     * Insert payment-method method for testing by disabling autocommit to database.
     * @param p
     * @return
     * @throws SQLException
     *
     * @author Cal Woods
     */
    public boolean insertPaymentMethodTesting(paymentMethod p) throws SQLException {
        if (p == null) throw new IllegalArgumentException("Cannot insert null payment method");
        int rows = 0;
        String sql = "INSERT INTO payment_methods (user_id, stripe_payment_method_id, last_four_digits, expiry_date, card_type, is_valid, is_primary) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getStripePaymentMethodId());
            ps.setString(3, p.getLastFourDigits());
            ps.setString(4, p.getExpiryDate());
            ps.setString(5, p.getCardType());
            ps.setBoolean(6, p.isValid());
            ps.setBoolean(7, p.isPrimary());
            rows = ps.executeUpdate();
        } catch (SQLException e) {
            log.error("insertPaymentMethodTesting(): SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return rows == 1;
    }
    /**
     * Deletes specific payment methods from the library
     * <p>
     * Takes in an int ID from the user and searches through the payment methods database for a matching ID and deletes it
     *
     * @param id ID of payment method to be deleted
     *
     * @return NULL if nothing is deleted and the Deleted Payment method if the deletion was successful
     *
     * @author Oscar Figeac
     */
    public paymentMethod deleteMethodById(int id) throws SQLException {
        paymentMethod removed = findMethodById(id);
        if (removed == null) return null;

        String sql = "DELETE FROM payment_methods WHERE method_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("deleteMethodById(): SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return removed;
    }

    /**
     * Deletes payment method without autocommitting to database for testing.
     * @param id Payment method id to find & delete
     * @return {@link paymentMethod} if deleted successfully
     * @throws SQLException If {@link Connection} failure
     *
     * @author Cal Woods
     */
    public paymentMethod deleteMethodByIdTesting(int id) throws SQLException {
        paymentMethod removed = findMethodById(id);
        if (removed == null) return null;

        String sql = "DELETE FROM payment_methods WHERE method_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("deleteMethodByIdTesting(): SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return removed;
    }

    /**
     * Finds the primary payment method of a user
     * <p>
     * Takes in a user id and goes through the Payment method database to find the corresponding primary method to that user id
     *
     * @param userId ID of the user whose primary method is to be found
     *
     * @return NULL if there is no primary method found and the payment method will be returned if the primary method is found
     *
     * @author Oscar Figeac
     */
    public paymentMethod findPrimaryMethodByUserId(int userId) throws SQLException {
        paymentMethod method = null;
        String sql = "SELECT * FROM payment_methods WHERE user_id = ? AND is_primary = TRUE";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    method = mapPaymentRow(rs);
                }
            } catch (SQLException e) {
                log.error("findPrimaryMethodByUserId(): An issue occurred when running the query or processing result set. \nException: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findPrimaryMethodByUserId() - The SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return method;
    }

    public boolean updatePaymentMethod(paymentMethod method) throws SQLException {
        if (method == null) throw new IllegalArgumentException("Cannot update a null payment method");

        int affectedRows = 0;
        String sql = "UPDATE payment_methods SET user_id=?, stripe_payment_method_id=?, last_four_digits=?, expiry_date=?, card_type=?, is_valid=?, is_primary=? WHERE method_id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, method.getUserId());
            ps.setString(2, method.getStripePaymentMethodId());
            ps.setString(3, method.getLastFourDigits());
            ps.setString(4, method.getExpiryDate());
            ps.setString(5, method.getCardType());
            ps.setBoolean(6, method.isValid());
            ps.setBoolean(7, method.isPrimary());
            ps.setInt(8, method.getMethodId());

            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            log.error("updatePaymentMethod() - The SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return affectedRows > 0;
    }

    public boolean updateMethodValidity(int methodId, boolean isValid) throws SQLException {
        int affectedRows = 0;
        String sql = "UPDATE payment_methods SET is_valid = ? WHERE method_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isValid);
            ps.setInt(2, methodId);

            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            log.error("updateMethodValidity() - The SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
        return affectedRows > 0;
    }

    @Override
    public List<paymentMethod> getMethodsForUser(int userId) {
        List<paymentMethod> methods = new ArrayList<>();
        String sql = "SELECT * FROM payment_methods WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    methods.add(paymentMethod.builder()
                            .methodId(rs.getInt("method_id"))
                            .cardType(rs.getString("card_type"))
                            .lastFourDigits(rs.getString("last_four_digits"))
                            .build());
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching payment methods for user: {}", userId, e);
        }
        return methods;
    }
}
