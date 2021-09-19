package com.kulik.airbnb.model;

public class AuthRequest {
    private String email;
    private String password;

    public AuthRequest() {

    }

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthRequest(AuthRequest auth) {
        this.email = auth.email;
        this.password = auth.password;
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
