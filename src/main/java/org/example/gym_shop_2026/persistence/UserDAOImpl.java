package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
//import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.entities.UserType;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    public UserDAOImpl(DataSource dataSource, @Lazy PasswordEncoder pEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = pEncoder;
    }

    @Override
    public boolean login(String uName, String pWord) throws SQLException {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return passwordEncoder.matches(pWord, rs.getString("password"));
                }
                return false;
            }
        }
    }

    @Override
    public int register(String uName, String fName, UserType type, String eMail, String pWord,
                        Date dob, String address, String eircode) throws SQLException {

        String sql = "INSERT INTO users (username, full_name, user_type, email, password, dob, address, eircode, secret_key, is_2fa_enabled) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, uName);
            ps.setString(2, fName);
            ps.setString(3, type.name());
            ps.setString(4, eMail);
            ps.setString(5, passwordEncoder.encode(pWord));
            ps.setDate(6, dob);
            ps.setString(7, address);
            ps.setString(8, eircode);
            ps.setString(9, "NOT_SET");
            ps.setBoolean(10, false);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return -1;

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    @Override
    public void updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, passwordEncoder.encode(newPassword));
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapUserRow(rs) : null;
            }
        }
    }

    @Override
    public boolean createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, full_name, user_type, email, password, dob, secret_key, is_2fa_enabled) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getUserType().name());
            ps.setString(4, user.getEmail());
            ps.setString(5, passwordEncoder.encode(user.getPassword()));
            ps.setDate(6, new java.sql.Date(user.getDob().getTime()));
            ps.setString(7, "NOT SET");
            ps.setBoolean(8, false);

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public User findUserByID(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapUserRow(rs) : null;
            }
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, full_name = ?, user_type = ?, email = ?, password = ?, dob = ?, secret_key = ?, is_2fa_enabled = ? WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getUserType().name());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.setDate(6, new java.sql.Date(user.getDob().getTime()));
            ps.setString(7, user.getSecretKey());
            ps.setBoolean(8, user.is2faEnabled());
            ps.setInt(9, user.getUser_id());

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteUser(User user) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getUser_id());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean updateStripeId(int userId, String stripeId) throws SQLException {
        String sql = "UPDATE users SET stripe_customer_id = ? WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stripeId);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public List<User> getAllUsers(){
        String sql = "SELECT * FROM users";
        ArrayList<User> allUsers = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                allUsers.add(mapUserRow(rs));
            }
        } catch (SQLException e) {
            log.error("Error retrieving all users: {}", e.getMessage());
        }

        return allUsers;
    }

    private User mapUserRow(ResultSet rs) throws SQLException {
        return User.builder()
                .user_id(rs.getInt("user_id"))
                .username(rs.getString("username"))
                .fullName(rs.getString("full_name"))
                .userType(UserType.valueOf(rs.getString("user_type")))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .dob(rs.getDate("dob"))
                .address(rs.getString("address"))
                .eircode(rs.getString("eircode"))
                .secretKey(rs.getString("secret_key"))
                .is2faEnabled(rs.getBoolean("is_2fa_enabled"))
                .stripeCustomerId(rs.getString("stripe_customer_id"))
                .build();
    }
}


