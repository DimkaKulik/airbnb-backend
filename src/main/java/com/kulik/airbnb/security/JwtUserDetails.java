package com.kulik.airbnb.security;

import com.kulik.airbnb.dao.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUserDetails extends UserDto implements UserDetails {

    public JwtUserDetails(Long id, String name, Date birthDate, String gender,
                          String avatar, String email, Boolean showEmail, String password,
                          String role, String description, Timestamp recordDate) {
        super(id, name, birthDate, gender, avatar, email, showEmail, password, role, description, recordDate);
    }

    public JwtUserDetails(UserDto userDto) {
        super(userDto);
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
