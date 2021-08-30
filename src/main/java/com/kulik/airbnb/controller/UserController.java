package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.User;
import com.kulik.airbnb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    ResponseEntity<?> getUsersPage(@RequestParam("limit") int limit, @RequestParam("offset") int offset) {
        List<User> users = userService.getPage(limit, offset);

        if (users != null) {
            return ResponseEntity.ok(users);
        } else {
            return new ResponseEntity<>("Cannot get users page", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getUser(@PathVariable int id) {
        User user = userService.get(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>("Cannot get user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/")
    ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        int status = userService.updateUser(updatedUser);

        if (status > 0) {
            return ResponseEntity.ok(status);
        } else {
            return new ResponseEntity<>("Cannot update user", HttpStatus.FORBIDDEN);
        }
    }

    /*

    TODO: make user account disable

    @DeleteMapping("/")
    ResponseEntity<?> deleteUser() {
        return userService.deleteUser();
    }
     */

}