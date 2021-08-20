package com.kulik.airbnb.controller;

import com.kulik.airbnb.dao.dto.UserDto;
import com.kulik.airbnb.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    ResponseEntity<?> getUsers(@RequestParam("limit") int limit, @RequestParam("offset") int offset) {
        return userService.getPage(limit, offset);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getUsers(@PathVariable int id) {
        return userService.get(id);
    }

    @PatchMapping("/")
    ResponseEntity<?> updateUser(@RequestBody UserDto updatedUserDto) {
        return userService.updateUser(updatedUserDto);
    }

    /*

    TODO: make user account disable

    @DeleteMapping("/")
    ResponseEntity<?> deleteUser() {
        return userService.deleteUser();
    }
     */

}