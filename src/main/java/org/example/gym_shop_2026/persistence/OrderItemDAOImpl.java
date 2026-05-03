package org.example.gym_shop_2026.persistence;

import org.example.gym_shop_2026.entities.OrderItem;
import org.example.gym_shop_2026.entities.Product;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderItemDAOImpl implements OrderItemDAO{

    private final DataSource dataSource;

    public OrderItemDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean createOrderItem(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItem.getOrder_id());
            ps.setInt(2, orderItem.getProduct_id());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(4, orderItem.getPriceAtPurchase());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<OrderItem> getItemsForOrder(int orderId) throws SQLException {
        String sql = "SELECT oi.*, p.name FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = ?";

        List<OrderItem> items = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = OrderItem.builder()
                            .order_id(rs.getInt("order_id"))
                            .product_id(rs.getInt("product_id"))
                            .quantity(rs.getInt("quantity"))
                            .priceAtPurchase(rs.getDouble("price_at_purchase"))
                            .build();

                    Product product = new Product();
                    product.setName(rs.getString("name"));
                    item.setProduct(product);

                    items.add(item);
                }
            }
        }
        return items;
    }
}
