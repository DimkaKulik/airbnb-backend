package com.kulik.airbnb.model;

public class Product {
    protected Long id;
    protected Long usersId;
    protected String mainPhoto;
    protected String type;
    protected Boolean full;
    protected String address;
    protected Boolean wifi;
    protected Boolean parking;
    protected Boolean pool;
    protected Boolean conditioner;
    protected Boolean extinguisher;
    protected Boolean smokeDetector;
    protected String description;
    protected Boolean approved;

    public Product(Long id, Long usersId, String mainPhoto, String type, Boolean full,
                   String address, Boolean wifi, Boolean parking, Boolean pool,
                   Boolean conditioner, Boolean extinguisher, Boolean smokeDetector,
                   String description, Boolean approved) {
        this.id = id;
        this.usersId = usersId;
        this.mainPhoto = mainPhoto;
        this.type = type;
        this.full = full;
        this.address = address;
        this.wifi = wifi;
        this.parking = parking;
        this.pool = pool;
        this.conditioner = conditioner;
        this.extinguisher = extinguisher;
        this.smokeDetector = smokeDetector;
        this.description = description;
        this.approved = approved;
    }

    public Product(Product product) {
        id = product.id;
        usersId = product.usersId;
        mainPhoto = product.mainPhoto;
        type = product.type;
        full = product.full;
        address = product.address;
        wifi = product.wifi;
        parking = product.parking;
        pool = product.pool;
        conditioner = product.conditioner;
        extinguisher = product.extinguisher;
        smokeDetector = product.smokeDetector;
        description = product.description;
        approved = product.approved;
    }


    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public Long getUsersId() {
        return usersId;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getApproved() {
        return approved;
    }

    public Boolean getConditioner() {
        return conditioner;
    }

    public Boolean getExtinguisher() {
        return extinguisher;
    }

    public Boolean getFull() {
        return full;
    }

    public Boolean getParking() {
        return parking;
    }

    public Boolean getPool() {
        return pool;
    }

    public Boolean getSmokeDetector() {
        return smokeDetector;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public void setConditioner(Boolean conditioner) {
        this.conditioner = conditioner;
    }

    public void setExtinguisher(Boolean extinguisher) {
        this.extinguisher = extinguisher;
    }

    public void setFull(Boolean full) {
        this.full = full;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public void setPool(Boolean pool) {
        this.pool = pool;
    }

    public void setSmokeDetector(Boolean smokeDetector) {
        this.smokeDetector = smokeDetector;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
