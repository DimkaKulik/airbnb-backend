package com.kulik.airbnb.mapper;

import com.kulik.airbnb.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("birth_date"),
                resultSet.getString("gender"),
                resultSet.getString("avatar"),
                resultSet.getString("email"),
                resultSet.getBoolean("show_email"),
                resultSet.getString("password"),
                resultSet.getString("role"),
                resultSet.getString("description"),
                resultSet.getTimestamp("record_date")
        );

        return user;
    }
}
