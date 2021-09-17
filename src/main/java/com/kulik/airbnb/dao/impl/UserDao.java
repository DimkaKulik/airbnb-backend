package com.kulik.airbnb.dao.impl;

import com.kulik.airbnb.dao.Dao;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.mapper.UserMapper;
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
public class UserDao implements Dao<User> {
    private static final String INSERT_USER = "INSERT INTO users (name, birth_date, gender, avatar, email, "
            + "show_email, password, description) "
            + "VALUES (:name, :birth_date, :gender, :avatar, :email, :show_email, "
            + ":password, :description)";
    private static final String SELECT_USERS_PAGE = "SELECT * FROM users LIMIT :limit OFFSET :offset";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = (:id)";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = (:email)";
    private static final String SELECT_USER_CONFIRMATION_BY_EMAIL = "SELECT confirmation FROM users WHERE email = (:email)";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = (:id)";
    private static final String DELETE_USER_BY_EMAIL = "DELETE FROM users WHERE email = (:email)";
    private static final String UPDATE_USER = "UPDATE users SET "
            + "name = IFNULL(:name, name) , birth_date = IFNULL(:birth_date, birth_date), gender = IFNULL(:gender, gender), "
            + "avatar = IFNULL(:avatar, avatar), email = IFNULL(:email, email), show_email = IFNULL(:show_email, show_email), "
            + "password = IFNULL(:password, password), description = IFNULL(:description, description) "
            + "WHERE id = :id";
    private static final String CONFIRM_USER_ACCOUNT = "UPDATE users SET confirmation = 'confirmed' WHERE email = (:email)";
    private static final String UPDATE_USER_CONFIRMATION_TOKEN = "UPDATE users SET confirmation = (:token) WHERE email = (:email)";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public User getById(Long id) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

            return (User) jdbcTemplate.queryForObject(SELECT_USER_BY_ID,
                    parameters, new UserMapper());
        } catch (Exception e) {
            return null;
        }
    }


    public User getByEmail(String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);
        try {
            return (User) jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL,
                    parameters, new UserMapper());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<User> getPage(int limit, int offset) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", offset);

            return jdbcTemplate.query(SELECT_USERS_PAGE, parameters, new UserMapper());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int create(User user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("birth_date", user.getBirthDate())
                .addValue("gender", user.getGender())
                .addValue("avatar", user.getAvatar())
                .addValue("email", user.getEmail())
                .addValue("show_email", user.getShowEmail())
                .addValue("password", user.getPassword() == null
                        ? null : passwordEncoder.encode(user.getPassword()))
                .addValue("description", user.getDescription());
        final KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(INSERT_USER, parameters, holder, new String[] {"id"});
        return holder.getKey().intValue();
    }

    @Override
    public int update(User user) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", user.getId())
                    .addValue("name", user.getName())
                    .addValue("birth_date", user.getBirthDate())
                    .addValue("gender", user.getGender())
                    .addValue("avatar", user.getAvatar())
                    .addValue("email", user.getEmail())
                    .addValue("show_email", user.getShowEmail())
                    .addValue("password", user.getPassword() == null
                            ? null : passwordEncoder.encode(user.getPassword()))
                    .addValue("description", user.getDescription());

            return jdbcTemplate.update(UPDATE_USER, parameters);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int delete(User user) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", user.getId());

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

    public void confirm(String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);
        jdbcTemplate.update(CONFIRM_USER_ACCOUNT, parameters);
    }

    public String getConfirmationField(String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);
        return jdbcTemplate.queryForObject(SELECT_USER_CONFIRMATION_BY_EMAIL, parameters, (rs, rowNum) ->
                rs.getString("confirmation"));

    }

    public void insertConfirmationToken(String email, String token) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("token", token);
        jdbcTemplate.update(UPDATE_USER_CONFIRMATION_TOKEN, parameters);
    }
}
