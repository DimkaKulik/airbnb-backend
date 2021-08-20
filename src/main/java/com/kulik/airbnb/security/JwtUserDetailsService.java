package com.kulik.airbnb.security;

import com.kulik.airbnb.dao.impl.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("JwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    JwtUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new JwtUserDetails(userDao.getByEmail(email));
    }
}
