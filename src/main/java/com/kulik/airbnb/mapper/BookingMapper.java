package com.kulik.airbnb.mapper;

import com.kulik.airbnb.model.Booking;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Booking booking = new Booking();

        booking.setId(resultSet.getLong("id"));
        booking.setProductId(resultSet.getLong("products_id"));
        booking.setUserId(resultSet.getLong("users_id"));
        booking.setStart(resultSet.getTimestamp("start"));
        booking.setFinish(resultSet.getTimestamp("finish"));
        booking.setDescription(resultSet.getString("description"));

        return booking;
    }
}
