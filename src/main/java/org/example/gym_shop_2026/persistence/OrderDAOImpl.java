package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.Order;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class OrderDAOImpl implements OrderDAO {
    private final DataSource dataSource;

    public OrderDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @author Oscar
     * Takes in an Order object and inserts it to keep record of the orders for a user
     * @param order The object expected to be inserted
     * @return 1 if created; -1 otherwise
     * @throws SQLException if the connection to the database fails at any point
     */
    @Override
    public int createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUser_id());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) return generatedKeys.getInt(1);
            }
        }
        return -1;
    }
}
