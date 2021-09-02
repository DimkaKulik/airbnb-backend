package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.GoogleOAuthClient;
import com.kulik.airbnb.model.AuthRequest;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.security.JwtTokenProvider;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.jsonwebtoken.Jwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final GoogleOAuthClient googleOAuthClient;
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager, GoogleOAuthClient googleOAuthClient, UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.googleOAuthClient = googleOAuthClient;
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            if (userDao.getConfirmationFlag(request.getEmail()) == 0) {
                throw new Exception("Account is not confirmed");
            }

            String token = jwtTokenProvider.createAuthorizationToken(request.getEmail());

            return token;
        } catch (Exception e) {
            return null;
        }
    }

    public void register(User user) throws Exception {
        if (user.getPassword() == null) {
            throw new Exception("Password cannot be null");
        }

        if (userDao.create(user) > 0) {
            sendConfirmationLink(user.getEmail());
        }
    }

    public void logout() { }

    public String loginOrRegisterViaGoogle(String authorizationCode) throws IOException {
        User userFromGoogle = googleOAuthClient.getUser(authorizationCode);
        User userFromDatabase = userDao.getByEmail(userFromGoogle.getEmail());

        int status = 0;
        if (userFromDatabase == null) {
            status = userDao.create(userFromGoogle);
            userDao.confirm(userFromGoogle.getEmail());
        } else {
            status = 1;
        }

        if (status > 0) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userFromGoogle.getEmail(), ""));
            String token = jwtTokenProvider.createAuthorizationToken(userFromGoogle.getEmail());

            return token;
        } else {
            return null;
        }
    }

    String sendConfirmationLink(String email) throws UnirestException {
        String confirmationLink = "http://localhost:8080/users/confirmation?token=" + jwtTokenProvider.createConfirmationToken(email);
        HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/sandbox18addbc31e5d4a68ada7e2b6b4dd4237.mailgun.org/messages")
			.basicAuth("api", "026241369f0f732cf5e0dbee7086f904-156db0f1-26d8978b")
                .queryString("from", "Excited User <dzmitser.kulik@gmail.com>")
                .queryString("to", email)
                .queryString("subject", "Email verification")
                .queryString("text", "Go to " + confirmationLink + " to verify your email.")
                .asString();
        return request.getBody();
    }
}
