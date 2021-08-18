package com.kulik.airbnb.dao.dto;

public class AuthRequestDto {
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
