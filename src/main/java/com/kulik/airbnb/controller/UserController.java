package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.User;
import com.kulik.airbnb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<?> getUsersPage(@RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        List<User> users = userService.getPage(limit, offset);

        if (users != null) {
            return ResponseEntity.ok(users);
        } else {
            return new ResponseEntity<>("Cannot get users page", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.get(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>("Cannot get user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/confirmation")
    ResponseEntity<?> confirmUser(@RequestParam(value = "token") String token) {
        try {
            return ResponseEntity.ok(userService.confirmUser(token));
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid confirmation link", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping
    ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        int status = userService.updateUser(updatedUser);

        if (status > 0) {
            return ResponseEntity.ok(status);
        } else {
            return new ResponseEntity<>("Cannot update user", HttpStatus.FORBIDDEN);
        }
    }

    //TODO: make user account disabled
}
