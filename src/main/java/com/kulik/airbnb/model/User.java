package com.kulik.airbnb.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

public class User {

    protected Long id;
    protected String name;
    protected GregorianCalendar birthDate;
    protected String gender;
    protected String avatar;
    protected String email;
    protected Boolean showEmail;
    protected String password;
    protected String role;
    protected String description;
    protected Timestamp recordDate;

    public User(Long id, String name, GregorianCalendar birthDate, String gender,
                String avatar, String email, Boolean showEmail,
                String password, String role, String description,
                Timestamp recordDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.avatar = avatar;
        this.email = email;
        this.showEmail = showEmail;
        this.password = password;
        this.role = role;
        this.description = description;
        this.recordDate = recordDate;
    }

    public User(User user) {
        id = user.id;
        name = user.name;
        birthDate = user.birthDate;
        gender = user.gender;
        avatar = user.avatar;
        email = user.email;
        showEmail = user.showEmail;
        password = user.password;
        role = user.role;
        description = user.description;
        recordDate = user.recordDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Boolean getShowEmail() {
        return showEmail;
    }

    public GregorianCalendar getBirthDate() {
        return birthDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getGender() {
        return gender;
    }

    public Timestamp getRecordDate() {
        return recordDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setBirthDate(GregorianCalendar birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setRecordDate(Timestamp recordDate) {
        this.recordDate = recordDate;
    }

    public void setShowEmail(Boolean showEmail) {
        this.showEmail = showEmail;
    }

}

