package com.kulik.airbnb.dao.dto;

import com.kulik.airbnb.controller.AuthController;

public class AuthRequestDto {
    private String email;
    private String password;

    public AuthRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

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
