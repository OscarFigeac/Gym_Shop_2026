package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.utils.PasswordHasher;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            }catch (SQLException e){
                log.error("login() - An issue has arisen when the query was executed or processing the result set. Exception - {}", e.getMessage());
            }
        }

        return false;
    }
}
