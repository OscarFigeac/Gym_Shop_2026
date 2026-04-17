package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;


import java.sql.*;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO {
    private final Connector connector;
    private final PasswordEncoder passwordEncoder;

    public UserDAOImpl(Connector connector, @Lazy PasswordEncoder pEncoder) {
        this.connector = connector;
        this.passwordEncoder = pEncoder;
    }

    //Functionality:

    /**
     *Method to allow the user to log-in
     * <p>
     * Let's the user enter their username and password, which are checked in the database to see if they match a specific user and if so they can log-in
     *
     * @param uName The username the user has entered
     * @param pWord The password of the same user
     *
     * @return True if the user can log in and False if the user can't
     *
     * @throws SQLException If their is a problem with the SQL query accessing the database
     *
     * @author Eoghan Carroll
     */
    @Override
    public boolean login(String uName, String pWord) throws SQLException {
        if (connector == null) {
            throw new SQLException("login() - Unable to establish a connection to the database !");
        }
        try (PreparedStatement ps = connector.getConnection().prepareStatement("SELECT * FROM users WHERE username = ?")) {
            ps.setString(1, uName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashPassword = rs.getString("password");
                    return passwordEncoder.matches(pWord, hashPassword);
                }
                return false;
            } catch (SQLException e) {
                log.error("login() - An issue has arisen when the query was executed or processing the result set. Exception - {}", e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Method to allow the user to register for the app
     * <p>
     * Takes in the appropriate fields for the user to create an account and returns a boolean if all the fields are entered correctly and uniquely
     *
     * @param uName The username chosen by the user
     * @param fName The users full name
     * @param type The type of customer the user chooses to be
     * @param eMail The users email address
     * @param pWord The password of the user
     * @param dob The users date of birth
     * @param address The users address
     * @param eircode The eircode of the user
     *
     * @return True if the registration is a success and False if not
     *
     * @throws SQLException If there is an error with the SQL Statement accessing the database
     *
     * @author Eoghan Carroll
     */
    @Override
    public boolean register(String uName, String fName, String type, String eMail, String pWord,
                            Date dob, String address, String eircode) throws SQLException {

        Connection conn = connector.getConnection();

        if (conn == null) {
            log.error("DATABASE CONNECTION IS NULL - Check your Connector class and DB credentials!");
            throw new SQLException("Unable to establish a connection to the database.");
        }

        String hashPassword = passwordEncoder.encode(pWord);

        String sql = "INSERT INTO users (username, full_name, user_type, email, password, dob, " +
                "address, eircode, secret_key, is_2fa_enabled) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setString(1, uName);
            ps.setString(2, fName);
            ps.setString(3, type);
            ps.setString(4, eMail);
            ps.setString(5, hashPassword);
            ps.setDate(6, dob);
            ps.setString(7, address);
            ps.setString(8, eircode);
            ps.setString(9, "NOT_SET");
            ps.setBoolean(10, false);

            int addedRows = ps.executeUpdate();
            return addedRows == 1;
        } catch (SQLException e) {
            log.error("Registration SQL failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Searches the user table via usernames
     * <p>
     * Takes in a username and searches through the database for the user with corresponding username as usernames are a unique field
     *
     * @param Username Username being searched for
     *
     * @return The user with the corresponding username
     *
     * @throws SQLException If there is an error with the SQL Statement accessing the database
     *
     * @author Eoghan Carroll
     */
    @Override
    public User findByUsername(String Username) throws SQLException {
        if (stringValidation(Username)) {
            throw new IllegalArgumentException("Username - Field cannot be null or empty for search function.");
        }

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findByUsername() - Unable to establish connection to the database !");
        }

        User user = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            ps.setString(1, Username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) user = mapUserRow(rs);
            }
        } catch (SQLException e) {
            log.error("findByUsername(): SQL error. \nException: {}", e.getMessage());
            throw e;
        }
        return user;
    }

    //CRUD Methods:

    /**
     * CRUD Method to create a new User
     * <p>
     * Adds a new user to the user table with a premade user entity instead of allowing the user to manually enter each detail
     *
     * @param toBeCreated The user being added to the database
     *
     * @return True if the user is successfully added and false if not
     *
     * @throws SQLException If there is an error with the SQL Statement accessing the database
     *
     * @author Eoghan Carroll
     */
    @Override
    public boolean createUser(User toBeCreated) throws SQLException{
        if(toBeCreated == null){
            throw new IllegalArgumentException("toBeCreated - User cannot be null to go through creation process !");
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            throw new SQLException("createUser() - Unable to establish a connection to the database !");
        }

        int addedRows = 0;

        try(PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, full_name, user_type, email, password, dob) VALUES (?,?,?,?,?,?)")){
            ps.setString(1, toBeCreated.getUsername());
            ps.setString(2, toBeCreated.getFullName());
            ps.setString(3, toBeCreated.getUserType());
            ps.setString(4, toBeCreated.getEmail());
            ps.setString(5, passwordEncoder.encode(toBeCreated.getPassword()));
            ps.setDate(6, new java.sql.Date(toBeCreated.getDob().getTime()));

            addedRows = ps.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            log.error("createUser() - Username \"{}\" is not available !", toBeCreated.getUsername());
        }catch(SQLException e){
            log.error("createUser() - The SQL query could not be executed or prepared by the program. Exception: {}", e.getMessage());
            throw e;
        }
        return addedRows == 1;
    }

    /**
     * CRUD Method for searching through the user table
     * <p>
     * Takes in a unique user ID  and searches through the table for the user with that ID and returns the user
     *
     * @param ID The ID of the user being searched for
     *
     * @return The user if found, Null if the user isn't found
     *
     * @throws SQLException If there is an error with the SQL Statement accessing the database
     *
     * @author Eoghan Carroll
     */
    @Override
    public User findUserByID(int ID) throws SQLException{
        if(ID <= 0){
            throw new IllegalArgumentException("ID - ID cannot be less than or equal to zero for search process !");
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            throw new SQLException("findUserByID - Unable to establish a connection to the database !");
        }

        User found = null;
        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?")){
            ps.setInt(1, ID);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) found = mapUserRow(rs);
            }
        } catch (SQLException e) {
            log.error("findByUsername(): SQL error. \nException: {}", e.getMessage());
            throw e;
        }
        return found;
    }

    /**
     * CRUD Method for updating a user in the table
     * <p>
     * Takes in a user entity with the same ID as one already in the table but with other different fields (eg. Username) and changes the details of the user in the table with the user that was inserted into this method.
     *
     * @param toBeUpdated The user with the updated details for the table
     *
     * @return True if the user was updated, false if not
     *
     * @throws SQLException If there is an error with the SQL Statement accessing the database
     *
     * @author Eoghan Carroll
     */
    @Override
    public boolean updateUser(User toBeUpdated) throws SQLException {
        if (toBeUpdated == null) {
            throw new IllegalArgumentException("toBeUpdated - User cannot be null to go through update process !");
        }

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("updateUser() - Unable to establish a connection to the database !");
        }

        try (PreparedStatement ps = conn.prepareStatement("UPDATE users SET username = ?, full_name = ?, user_type = ?, email = ?, password = ?, dob = ?, secret_key = ?, is_2fa_enabled = ? WHERE user_id = ?")) {
            ps.setString(1, toBeUpdated.getUsername());
            ps.setString(2, toBeUpdated.getFullName());
            ps.setString(3, toBeUpdated.getUserType());
            ps.setString(4, toBeUpdated.getEmail());
            ps.setString(5, toBeUpdated.getPassword());
            ps.setDate(6, new java.sql.Date(toBeUpdated.getDob().getTime()));
            // 2FA fields
            ps.setString(7, toBeUpdated.getSecretKey());
            ps.setBoolean(8, toBeUpdated.is2faEnabled());
            ps.setInt(9, toBeUpdated.getUser_id());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                log.warn("updateUser() - No User Found With User_ID: {}", toBeUpdated.getUser_id());
            }
            return rowsAffected == 1;
        } catch (SQLException e) {
            log.error("updateUser() - Database Error: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * CRUD Method for deleting users from the table
     * <p>
     * Takes in a user entity and searches the table for the same user and deletes it from the table
     *
     * @param toBeDeleted The user to deleted from the table
     *
     * @return True if the user has successfully deleted, False if not
     *
     * @throws SQLException If there is an error with the SQL Statement accessing the database
     *
     *  @author Eoghan Carroll
     */
    @Override
    public boolean deleteUser (User toBeDeleted) throws SQLException{
        if(toBeDeleted == null){
            throw new IllegalArgumentException("toBeDeleted - User cannot be null to go through deletion process !");
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            throw new SQLException("deleteUser() - Unable to establish a connection to the database !");
        }

        int affectedRows = 0;

        try(PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE user_id = ?")){
            ps.setInt(1, toBeDeleted.getUser_id());

            affectedRows = ps.executeUpdate();
            if(affectedRows == 0){
                log.warn("deleteUser() - No User Found With User_ID: {}", toBeDeleted.getUser_id());
            }
        }catch(SQLException e){
            log.error("deleteUser() - The SQL query could not be executed or prepared by the program. Exception: {}", e.getMessage());
            throw e;
        }
        return affectedRows == 1;
    }

    //Private Methods:

    private static boolean stringValidation(String x) {
        return x == null || x.isEmpty();
    }

    private static User mapUserRow(ResultSet rs) throws SQLException {
        return User.builder()
                .user_id(rs.getInt("user_id"))
                .username(rs.getString("username"))
                .fullName(rs.getString("full_name"))
                .userType(rs.getString("user_type"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .dob(rs.getDate("dob"))
                .address(rs.getString("address"))
                .eircode(rs.getString("eircode"))
                .secretKey(rs.getString("secret_key"))
                .is2faEnabled(rs.getBoolean("is_2fa_enabled"))
                .build();
    }
}


