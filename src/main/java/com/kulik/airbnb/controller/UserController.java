package com.kulik.airbnb.controller;

import com.kulik.airbnb.dao.dto.UserDto;
import com.kulik.airbnb.dao.impl.UserDao;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserDao userDao;

    UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/users")
    List<UserDto> getUsers() {
        return userDao.getAll();
    }

    @GetMapping("/users/{id}")
    UserDto getUsers(@PathVariable Long id) {
        return userDao.get(id);
    }

    @PatchMapping("/users")
    int updateUser(@RequestBody UserDto updatedUserDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDto authenticatedUserDto = userDao.get(email);

        updatedUserDto.setId(authenticatedUserDto.getId());
        updatedUserDto.setRole(authenticatedUserDto.getRole());

        return userDao.update(updatedUserDto);
    }

    @DeleteMapping("/users")
    int deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.deleteByEmail(email);
    }
}