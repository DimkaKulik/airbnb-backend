package com.kulik.airbnb.controller;

import com.kulik.airbnb.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final AuthService authService;

    public OAuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login/google")
    ResponseEntity<?> loginOrRegister(@RequestParam("code") String authorizationCode) throws IOException {
        return authService.loginOrRegisterViaGoogle(authorizationCode);
    }
}
