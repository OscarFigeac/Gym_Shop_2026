package org.example.gym_shop_2026.persistence;

import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.connector.Connector;
import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.User;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@Slf4j
public class BasketDAOImpl implements BasketDAO {
    private final Connector connector;

    public BasketDAOImpl(Connector connector) {
        this.connector = connector;
    }

    //Functionality

    @Override
    public Basket findByUserID(int user_id) throws SQLException {
        if(user_id <= 0){
            throw new IllegalArgumentException("ID - ID cannot be less than or equal to zero for search process !");
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            throw new SQLException("findUserByID - Unable to establish a connection to the database !");
        }

        Basket found = null;
        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM basket WHERE user_id = ?")){
            ps.setInt(1, user_id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) found = mapBasketRow(rs);
            }
        } catch (SQLException e) {
            log.error("findByUsername(): SQL error. \nException: {}", e.getMessage());
            throw e;
        }
        return found;
    }

    @Override
    public boolean updateStatus(int basketId, String status) throws SQLException{
        if(basketId <= 0){
            throw new IllegalArgumentException("ID - ID cannot be less than or equal to zero for search process !");
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            throw new SQLException("updateStatus - Unable to establish a connection to the database !");
        }

        try(PreparedStatement ps = conn.prepareStatement("UPDATE basket SET status = ? WHERE basket_id = ?")){
            ps.setString(1, status);
            ps.setInt(2, basketId);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                log.warn("updateStatus() - No User Found With User_ID: {}", basketId);
            }

            return rowsAffected == 1;

        } catch (SQLException e) {
            log.error("findByUsername(): SQL error. \nException: {}", e.getMessage());
            throw e;
        }
    }

    //Private Methods

    private static Basket mapBasketRow(ResultSet rs) throws SQLException {
        return Basket.builder()
                .basketId(rs.getInt("basket_id"))
                .user_id(rs.getInt("user_id"))
                .status(rs.getString("status"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
    }
}
