package com.kulik.airbnb.dao.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductPhotoDao {
    private static final String SELECT_PHOTOS_URLS_BY_PRODUCT_ID = "SELECT url FROM photos WHERE products_id = :id";
    private static final String INSERT_PHOTO = "INSERT INTO photos (products_id, users_id, url) "
            + "VALUES (:products_id, :users_id, :url)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductPhotoDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getPhotosByProductId(Long id) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", id);

            return jdbcTemplate.query(SELECT_PHOTOS_URLS_BY_PRODUCT_ID, parameters, (rs, rowNum) ->
                    rs.getString("url"));
        } catch (Exception e) {
            return null;
        }
    }

    public void createPhoto(Long productId, Long userId, String url) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("products_id", productId)
                .addValue("users_id", userId)
                .addValue("url", url);
        jdbcTemplate.update(INSERT_PHOTO, parameters);
    }
}
