package com.kulik.airbnb.service;

import com.kulik.airbnb.model.ServiceResponse;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.dao.impl.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public ServiceResponse<?> getPage(int limit, int offset) {
        List<User> users = userDao.getPage(limit, offset);

        if (users == null) {
            return new ServiceResponse<>("Cannot extract users from DB", null);
        } else {
            return new ServiceResponse<>("ok", users);
        }
    }

    public ServiceResponse<?> get(int id) {
        User user = userDao.getById(id);

        if (user == null) {
            return new ServiceResponse<>("Cannot extract user from DB", null);
        } else {
            return new ServiceResponse<>("ok", user);
        }
    }

    public ServiceResponse<?> updateUser(@RequestBody User updatedUser) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User authenticatedUser = userDao.getByEmail(email);

        updatedUser.setId(authenticatedUser.getId());
        updatedUser.setRole(authenticatedUser.getRole());

        //restriction: avoid non-native users to update password
        if (!authenticatedUser.getOrigin().equals("native")) {
            updatedUser.setPassword(null);
        }

        int status = userDao.update(updatedUser);

        if (status > 0) {
            return new ServiceResponse<>("ok", status);
        } else {
            return new ServiceResponse<>("Cannot update user", null);
        }
    }


    public ServiceResponse<?> deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        int status = userDao.deleteByEmail(email);

        if (status > 0) {
            return new ServiceResponse<>("ok", status);
        } else {
            return new ServiceResponse<>("Cannot delete user", null);
        }
    }

}
