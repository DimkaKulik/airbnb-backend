package com.kulik.airbnb.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Booking {
    private Long id;
    private Long productId;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
    private Timestamp start;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
    private Timestamp finish;
    private String description;

    public Booking() {

    }

    public Booking(Long id, Long productId, Long userId, Timestamp start, Timestamp finish, String description) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.start = start;
        this.finish = finish;
        this.description = description;
    }

    public Booking(Booking booking) {
        id = booking.id;
        productId = booking.productId;
        userId = booking.userId;
        start = booking.start;
        finish = booking.finish;
        description = booking.description;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getUserId() {
        return userId;
    }

    public Timestamp getStart() {
        return start;
    }

    public Timestamp getFinish() {
        return finish;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public void setFinish(Timestamp finish) {
        this.finish = finish;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
