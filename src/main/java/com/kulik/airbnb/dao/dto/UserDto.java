package com.kulik.airbnb.dao.dto;

import java.sql.Timestamp;
import java.util.Date;

public class UserDto {
    protected Long id;
    protected String name;
    protected Date birthDate;
    protected String gender;
    protected String avatar;
    protected String email;
    protected Boolean showEmail;
    protected String password;
    protected String role;
    protected String description;
    protected Timestamp recordDate;

    public UserDto(Long id, String name, Date birthDate, String gender,
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

    public UserDto(UserDto userDto) {
        id = userDto.id;
        name = userDto.name;
        birthDate = userDto.birthDate;
        gender = userDto.gender;
        avatar = userDto.avatar;
        email = userDto.email;
        showEmail = userDto.showEmail;
        password = userDto.password;
        role = userDto.role;
        description = userDto.description;
        recordDate = userDto.recordDate;
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

    public Date getBirthDate() {
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

    public void setBirthDate(Date birthDate) {
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

    public void fixNullFields() {
        if (getGender() == null) {
            setGender("unknown");
        }
        if (getShowEmail() == null) {
            setShowEmail(true);
        }
        if (getPassword() == null) {
            setPassword(generateSecurePassword());
        }
        if (getRole() == null) {
            setRole("ROLE_USER");
        }
    }

    String generateSecurePassword() {
        return "11111";
    }
}

