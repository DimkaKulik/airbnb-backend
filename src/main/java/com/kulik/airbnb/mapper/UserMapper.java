package com.kulik.airbnb.mapper;

import com.kulik.airbnb.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class UserMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setGender(resultSet.getString("gender"));
        user.setAvatar(resultSet.getString("avatar"));
        user.setEmail(resultSet.getString("email"));
        user.setShowEmail(resultSet.getBoolean("show_email"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role"));
        user.setDescription(resultSet.getString("description"));
        user.setRecordDate(resultSet.getTimestamp("record_date"));

        GregorianCalendar calendar = null;
        if (resultSet.getDate("birth_date") != null) {
            calendar = new GregorianCalendar(resultSet.getDate("birth_date").getYear(),
                    resultSet.getDate("birth_date").getMonth(),
                    resultSet.getDate("birth_date").getDate());
        }
        user.setBirthDate(calendar);

        return user;
    }
}
