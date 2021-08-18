package com.kulik.airbnb.dao.mapper;

import com.kulik.airbnb.dao.dto.ProductDto;
import com.kulik.airbnb.dao.dto.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductDto productDto = new ProductDto(
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

        return productDto;
    }
}
