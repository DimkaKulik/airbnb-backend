package com.kulik.airbnb.mapper;

import com.kulik.airbnb.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product(
                resultSet.getLong("id"),
                resultSet.getLong("users_id"),
                resultSet.getString("main_photo"),
                resultSet.getString("type"),
                resultSet.getBoolean("full"),
                resultSet.getString("address"),
                resultSet.getBoolean("wifi"),
                resultSet.getBoolean("parking"),
                resultSet.getBoolean("pool"),
                resultSet.getBoolean("conditioner"),
                resultSet.getBoolean("extinguisher"),
                resultSet.getBoolean("smoke_detector"),
                resultSet.getString("description"),
                resultSet.getBoolean("approved")
        );

        return product;
    }
}
