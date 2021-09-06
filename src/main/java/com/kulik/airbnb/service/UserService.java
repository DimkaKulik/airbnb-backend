package com.kulik.airbnb.service;

import com.kulik.airbnb.model.User;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDao userDao;

    @Autowired
    public UserService(JwtTokenProvider jwtTokenProvider, UserDao userDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDao = userDao;
    }

    public List<User> getPage(int limit, int offset) {
        List<User> users = userDao.getPage(limit, offset);
        return users;
    }

    public User get(int id) {
        User user = userDao.getById(id);
        return user;
    }

    public String confirmUser(String token) throws Exception {
        String email = jwtTokenProvider.getUsername(token);

        if (jwtTokenProvider.validateToken(token) && userDao.getConfirmationField(email).equals(token)) {
            userDao.confirm(email);
            return "User successfully confirmed";
        } else {
            throw new Exception("invalid token");
        }
    }

    public int updateUser(@RequestBody User updatedUser) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User authenticatedUser = userDao.getByEmail(email);

        updatedUser.setId(authenticatedUser.getId());

        int status = userDao.update(updatedUser);

        return status;
    }


    public int deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        int status = userDao.deleteByEmail(email);

        return status;
    }
}
