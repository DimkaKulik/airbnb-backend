package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.ServiceResponse;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.service.UserService;
import org.springframework.http.HttpStatus;
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
        ServiceResponse response = userService.getPage(limit, offset);
        return ServiceResponse.returnResponseEntity(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getUsers(@PathVariable int id) {
        ServiceResponse response = userService.get(id);
        return ServiceResponse.returnResponseEntity(response);
    }

    @PatchMapping("/")
    ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        ServiceResponse response = userService.updateUser(updatedUser);
        return ServiceResponse.returnResponseEntity(response);
    }

    /*

    TODO: make user account disable

    @DeleteMapping("/")
    ResponseEntity<?> deleteUser() {
        return userService.deleteUser();
    }
     */

}