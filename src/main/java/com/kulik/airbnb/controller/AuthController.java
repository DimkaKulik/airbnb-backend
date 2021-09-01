package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.AuthRequest;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.service.AuthService;
import com.mashape.unirest.http.exceptions.UnirestException;
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
        String token = authService.authenticate(request);

        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return new ResponseEntity<>("Invalid login/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/login/google")
    ResponseEntity<?> loginOrRegister(@RequestBody Map<String, String> body) throws IOException {
        String token = authService.loginOrRegisterViaGoogle(body.get("code"));

        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return new ResponseEntity<>("Cannot authorize user", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) throws UnirestException {
        String token = authService.register(user);

        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return new ResponseEntity<>("Cannot register user", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout();
    }
}
