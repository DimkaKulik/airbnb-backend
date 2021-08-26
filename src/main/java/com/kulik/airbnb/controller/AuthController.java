package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.AuthRequest;
import com.kulik.airbnb.model.ServiceResponse;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        ServiceResponse<?> response = authService.authenticate(request);

        if (response.getMessage().equals("ok")) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/login/google")
    ResponseEntity<?> loginOrRegister(@RequestBody Map<String, String> body) throws IOException {
        ServiceResponse<?> response = authService.loginOrRegisterViaGoogle(body.get("code"));

        if (response.getMessage().equals("ok")) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        ServiceResponse<?> response = authService.register(user);

        if (response.getMessage().equals("ok")) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout();
    }
}
