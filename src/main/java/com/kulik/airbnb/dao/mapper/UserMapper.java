package com.kulik.airbnb.dao.mapper;

import com.kulik.airbnb.dao.dto.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        UserDto userDto = new UserDto(
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

        return userDto;
    }
}
