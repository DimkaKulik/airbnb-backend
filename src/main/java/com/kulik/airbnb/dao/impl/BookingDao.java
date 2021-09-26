package com.kulik.airbnb.dao.impl;

import com.kulik.airbnb.mapper.BookingMapper;
import com.kulik.airbnb.model.Booking;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingDao {
    private static final String SELECT_BOOKING_BY_ID = "SELECT * FROM booking WHERE id = :id";
    private static final String SELECT_USERS_BOOKINGS = "SELECT * FROM booking WHERE users_id = :user_id "
            + "LIMIT :limit OFFSET :offset";
    private static final String INSERT_BOOKING = "INSERT INTO booking (id, products_id, users_id, start, finish, "
            + "description) VALUES (:id, :products_id, :users_id, :start, :finish, :description)";
    private static final String DELETE_BOOKING = "DELETE FROM booking WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookingDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Booking> getUserBookingsPage(int userId, int limit, int offset) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("limit", limit)
                .addValue("offset", offset);

        return jdbcTemplate.query(SELECT_USERS_BOOKINGS, parameters, new BookingMapper());
    }

    public Booking getBookingById(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return (Booking) jdbcTemplate.queryForObject(SELECT_BOOKING_BY_ID, parameters, new BookingMapper());
    }

    public void insertBooking(Booking booking) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", booking.getId())
                .addValue("products_id", booking.getProductId())
                .addValue("users_id", booking.getUserId())
                .addValue("start", booking.getStart())
                .addValue("finish", booking.getFinish())
                .addValue("description", booking.getDescription());

        jdbcTemplate.update(INSERT_BOOKING, parameters);
    }

    public void deleteBookingById(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(DELETE_BOOKING, parameters);
    }
}
