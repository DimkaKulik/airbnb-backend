package com.kulik.airbnb.dao.impl;

import com.kulik.airbnb.dao.Dao;
import com.kulik.airbnb.dao.dto.UserDto;
import com.kulik.airbnb.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

@Component
public class UserDao implements Dao<UserDto> {
    private final String INSERT_USER = "INSERT INTO users (name, birth_date, gender, avatar, email, "
            + "show_email, password, role, description) "
            + "VALUES (:name, :birth_date, :gender, :avatar, :email, :show_email, "
            + ":password, :role, :description)";
    private final String SELECT_ALL_USERS = "SELECT * FROM users";
    private final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = (:id)";
    private final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = (:email)";
    private final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = (:id)";
    private final String DELETE_USER_BY_EMAIL = "DELETE FROM users WHERE email = (:email)";
    private final String UPDATE_USER = "UPDATE users SET "
            + "name = IFNULL(:name, name) , birth_date = IFNULL(:birth_date, birth_date), gender = IFNULL(:gender, gender), "
            + "avatar = IFNULL(:avatar, avatar), email = IFNULL(:email, email), show_email = IFNULL(:show_email, show_email), "
            + "password = IFNULL(:password, password), role = IFNULL(:role, role), description = IFNULL(:description, description) "
            + "WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public UserDto get(Long id) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

            return (UserDto) jdbcTemplate.queryForObject(SELECT_USER_BY_ID,
                    parameters, new UserMapper());
        } catch (Exception e) {
            return null;
        }
    }


    public UserDto get(String email) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

            return (UserDto) jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL,
                    parameters, new UserMapper());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<UserDto> getAll() {
        try {
            return jdbcTemplate.query(SELECT_ALL_USERS, new UserMapper());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Long create(UserDto userDto) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("name", userDto.getName())
                    .addValue("birth_date", userDto.getBirthDate())
                    .addValue("gender", userDto.getGender())
                    .addValue("avatar", userDto.getAvatar())
                    .addValue("email", userDto.getEmail())
                    .addValue("show_email", userDto.getShowEmail())
                    .addValue("password", passwordEncoder.encode(userDto.getPassword()))
                    .addValue("role", userDto.getRole())
                    .addValue("description", userDto.getDescription());
            final KeyHolder holder = new GeneratedKeyHolder();

            jdbcTemplate.update(INSERT_USER, parameters, holder, new String[] {"id"});
            return holder.getKey().longValue();
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public int update(UserDto userDto) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", userDto.getId())
                    .addValue("name", userDto.getName())
                    .addValue("birth_date", userDto.getBirthDate())
                    .addValue("gender", userDto.getGender())
                    .addValue("avatar", userDto.getAvatar())
                    .addValue("email", userDto.getEmail())
                    .addValue("show_email", userDto.getShowEmail())
                    .addValue("password", passwordEncoder.encode(userDto.getPassword()))
                    .addValue("role", userDto.getRole())
                    .addValue("description", userDto.getDescription());

            return jdbcTemplate.update(UPDATE_USER, parameters);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int delete(UserDto userDto) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", userDto.getId());

            return jdbcTemplate.update(DELETE_USER_BY_ID, parameters);
        } catch (Exception e) {
            return 0;
        }
    }


    public int deleteByEmail(String email) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);
            return jdbcTemplate.update(DELETE_USER_BY_EMAIL, parameters);
        } catch (Exception e) {
            return 0;
        }
    }
}
