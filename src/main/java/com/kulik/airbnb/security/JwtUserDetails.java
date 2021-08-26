package com.kulik.airbnb.security;

import com.kulik.airbnb.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class JwtUserDetails extends User implements UserDetails {

    public JwtUserDetails(Long id, String name, Date birthDate, String gender,
                          String avatar, String email, Boolean showEmail, String password,
                          String role, String origin, String description, Timestamp recordDate) {
        super(id, name, birthDate, gender, avatar, email, showEmail, password, role, origin, description, recordDate);
    }

    public JwtUserDetails(User user) {
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
