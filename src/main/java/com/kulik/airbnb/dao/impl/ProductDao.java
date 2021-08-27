package com.kulik.airbnb.dao.impl;

import com.kulik.airbnb.dao.Dao;
import com.kulik.airbnb.model.Product;
import com.kulik.airbnb.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDao implements Dao<Product> {

    private static final String INSERT_PRODUCT = "INSERT INTO products (users_id, main_photo, type, full, address, wifi, "
            + "parking, pool, conditioner, extinguisher, smoke_detector, description) "
            + "VALUES (:users_id, :main_photo, :type, :full, :address, :wifi, :parking, "
            + ":pool, :conditioner, :extinguisher, :smoke_detector, :description)";
    private static final String SELECT_PRODUCTS_PAGE = "SELECT * FROM products LIMIT :limit OFFSET :offset";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE id = (:id)";
    private static final String SELECT_USER_EMAIL_BY_PRODUCT_ID = "SELECT email FROM products CROSS JOIN users " +
            "ON products.users_id = users.id WHERE products.id = (:product_id)";
    //TODO: select product by host email
    private static final String DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE id = (:id)";
    private static final String UPDATE_PRODUCT = "UPDATE products SET "
            + "users_id = IFNULL(:users_id, users_id), main_photo = IFNULL(:main_photo, main_photo), "
            + "type = IFNULL(:type, type), full = IFNULL(:full, full), "
            + "address = IFNULL(:address, address), parking = IFNULL(:parking, parking), pool = IFNULL(:pool, pool), "
            + "conditioner = IFNULL(:conditioner, conditioner), extinguisher = IFNULL(:extinguisher, extinguisher), "
            + "smoke_detector = IFNULL(:smoke_detector, smoke_detector), description = IFNULL(:description, description), "
            + "approved = IFNULL(:approved, approved) WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product getById(int id) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", id);

            return (Product) jdbcTemplate.queryForObject(SELECT_PRODUCT_BY_ID,
                    parameters, new ProductMapper());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List getPage(int limit, int offset) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("limit", limit)
                    .addValue("offset", offset);

            return jdbcTemplate.query(SELECT_PRODUCTS_PAGE, parameters, new ProductMapper());
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserEmailByProductId(Long productId) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("product_id", productId);

            return jdbcTemplate.queryForObject(SELECT_USER_EMAIL_BY_PRODUCT_ID, parameters, (rs, rowNum) ->
                    rs.getString("email"));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int create(Product product) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("users_id", product.getUsersId())
                    .addValue("main_photo", product.getMainPhoto())
                    .addValue("type", product.getType())
                    .addValue("full", product.getFull())
                    .addValue("address", product.getAddress())
                    .addValue("wifi", product.getWifi())
                    .addValue("parking", product.getParking())
                    .addValue("pool", product.getPool())
                    .addValue("conditioner", product.getConditioner())
                    .addValue("extinguisher", product.getExtinguisher())
                    .addValue("smoke_detector", product.getSmokeDetector())
                    .addValue("description", product.getDescription())
                    .addValue("approved", product.getApproved());
            final KeyHolder holder = new GeneratedKeyHolder();

            jdbcTemplate.update(INSERT_PRODUCT, parameters, holder, new String[] {"id"});
            return holder.getKey().intValue();
        } catch (Exception e) {
            return 0;
        }
    }


    @Override
    public int update(Product product) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", product.getId())
                .addValue("users_id", product.getUsersId())
                .addValue("main_photo", product.getMainPhoto())
                .addValue("type", product.getType())
                .addValue("full", product.getFull())
                .addValue("address", product.getAddress())
                .addValue("wifi", product.getWifi())
                .addValue("parking", product.getParking())
                .addValue("pool", product.getPool())
                .addValue("conditioner", product.getConditioner())
                .addValue("extinguisher", product.getExtinguisher())
                .addValue("smoke_detector", product.getSmokeDetector())
                .addValue("description", product.getDescription())
                .addValue("approved", product.getApproved());

            return jdbcTemplate.update(UPDATE_PRODUCT, parameters);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int delete(Product product) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", product.getId());

            return jdbcTemplate.update(DELETE_PRODUCT_BY_ID, parameters);
        } catch (Exception e) {
            return 0;
        }
    }
}
