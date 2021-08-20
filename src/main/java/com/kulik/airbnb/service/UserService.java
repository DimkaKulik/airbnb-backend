package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.dto.UserDto;
import com.kulik.airbnb.dao.impl.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpTimeoutException;
import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public ResponseEntity<?> getPage(int limit, int offset) {
        List<UserDto> users = userDao.getPage(limit, offset);

        if (users != null) {
            return ResponseEntity.ok(users);
        } else {
            return new ResponseEntity<>("Cannot extract users from DB", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> get(int id) {
        UserDto userDto = userDao.getById(id);

        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return new ResponseEntity<>("Cannot extract user from DB", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> updateUser(@RequestBody UserDto updatedUserDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDto authenticatedUserDto = userDao.getByEmail(email);

        updatedUserDto.setId(authenticatedUserDto.getId());
        updatedUserDto.setRole(authenticatedUserDto.getRole());

        int status = userDao.update(updatedUserDto);

        if (status > 0) {
            return ResponseEntity.ok(status);
        } else {
            return new ResponseEntity<>("Cannot update user", HttpStatus.CONFLICT);
        }
    }


    public ResponseEntity<?> deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        int status = userDao.deleteByEmail(email);

        if (status > 0) {
            return ResponseEntity.ok(status);
        } else {
            return new ResponseEntity<>("Cannot delete user", HttpStatus.CONFLICT);
        }
    }

}
