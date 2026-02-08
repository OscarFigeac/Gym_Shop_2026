package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Users;
import org.example.gym_shop_2026.utils.PasswordHasher;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.example.gym_shop_2026.utils.PasswordHasher;

import java.sql.*;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO{
    private final Connector connector;

    public UserDAOImpl(Connector connector){
        this.connector = connector;
    }

    @Override
    public boolean login(String uName, String pWord) throws SQLException {
        if(connector == null){
            throw new SQLException("login() - Unable to establish a connection to the database !");
        }
        try(PreparedStatement ps = connector.getConnection().prepareStatement("SELECT * FROM users WHERE username = ?")){
            ps.setString(1, uName);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    String hashPassword = rs.getString("password");
                    return PasswordHasher.verifyPassword(pWord, hashPassword);
                }
                return false;
            }catch (SQLException e){
                log.error("login() - An issue has arisen when the query was executed or processing the result set. Exception - {}", e.getMessage());
                throw e;
            }
        }

        return false;
    }

    @Override
    public boolean register(String uName, String fName, String type, String eMail, String pWord, Date dob) throws SQLException{
        if(stringValidation(uName)){
            throw new IllegalArgumentException("Username - Field cannot be null or empty for registration process !");
        }
        if(stringValidation(fName)){
            throw new IllegalArgumentException("Full Name - Field cannot be null or empty for registration process !");
        }
        if(stringValidation(type)){
            throw new IllegalArgumentException("User Type - Field cannot be null or empty for registration process !");
        }
        if(stringValidation(eMail)){
            throw new IllegalArgumentException("E-Mail - Field cannot be null or empty for registration process !");
        }
        if(stringValidation(pWord)){
            throw new IllegalArgumentException("Password - Field cannot be null or empty for registration process !");
        }
        if(dob == null){
            throw new IllegalArgumentException("Date of Birth - Field cannot be null or empty for registration process !");
        };
        if(connector.getConnection() == null){
            throw new SQLException("register() - Unable to establish connection to the database !");
        }

        String hashPassword = PasswordHasher.hashPassword(pWord);
        int addedRows = 0;

        try(PreparedStatement ps = connector.getConnection().prepareStatement("INSERT INTO users (username, full_name, user_type, email, password, dob) VALUES (?,?,?,?,?,?)")){
            ps.setString(1, uName);
            ps.setString(2, fName);
            ps.setString(3, type);
            ps.setString(4, eMail);
            ps.setString(5, hashPassword);
            ps.setDate(6, dob);

            addedRows = ps.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            log.error("register() - Username \"{}\" unavailable !", uName);
        }catch(SQLException e){
            log.error("register() - The SQL query could not be executed or prepared by the program. Exception: {}", e.getMessage());
            throw e;
        }
        return addedRows == 1;
    }

    @Override
    public Users findByUsername(String Username) throws SQLException{
        if(stringValidation(Username)){
            throw new IllegalArgumentException("Username - Field cannot be null or empty for search function.");
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            throw new SQLException("findByUsername() - Unable to establish connection to the database !");
        }

        Users user = null;
        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?")){
            ps.setString(1, Username);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) user = mapUserRow(rs);
            }
        } catch (SQLException e) {
            log.error("findByUsername(): SQL error. \nException: {}", e.getMessage());
            throw e;
        }
        return user;
    }


    private static boolean stringValidation(String x){
        return x==null||x.isEmpty();
    }

    private static Users mapUserRow(ResultSet rs) throws SQLException {
        return Users.builder()
                .user_id(rs.getInt("user_id"))
                .username(rs.getString("username"))
                .fullName(rs.getString("fullName"))
                .userType(rs.getString("userType"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .dob(rs.getDate("dob"))
                .build();
    }
}
