package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.AuthRequest;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        try {
            String token = authService.authenticate(request);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            // + e.getMessage() added for development purposes
            return new ResponseEntity<>("Cannot login with such credentials : " + e.getMessage(), HttpStatus.FORBIDDEN);
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
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            authService.register(user);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            // + e.getMessage() added for development purposes
            return new ResponseEntity<>("Registration error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout();
    }
}
