package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.GoogleOAuthClient;
import com.kulik.airbnb.model.AuthRequest;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.security.JwtTokenProvider;
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
            String token = jwtTokenProvider.createAuthorizationToken(request.getEmail());

            return token;
        } catch (AuthenticationException e) {
            return null;
        }
    }

    public String register(User user) {
        int status = 0;

        if (user.getPassword() != null) {
            status = userDao.create(user);
        }

        if (status > 0) {
            sendConfirmationLink(user.getEmail());
            return authenticate(new AuthRequest(user.getEmail(), user.getPassword()));
        } else {
            return null;
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

    void sendConfirmationLink(String email) {
        
    }
}
