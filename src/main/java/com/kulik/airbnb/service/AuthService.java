package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.GoogleOAuthClient;
import com.kulik.airbnb.model.AuthRequest;
import com.kulik.airbnb.model.ServiceResponse;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final GoogleOAuthClient googleOAuthClient;
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    private final String GOOGLE_USER_PASSWORD = "google-user-password";

    public AuthService(AuthenticationManager authenticationManager, GoogleOAuthClient googleOAuthClient, UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.googleOAuthClient = googleOAuthClient;
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ServiceResponse<?> authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String token = jwtTokenProvider.createToken(request.getEmail());

            return new ServiceResponse<String>("ok", token);
        } catch (AuthenticationException e) {
            return new ServiceResponse<>("Cannot authenticate user: invalid login or password", null);
        }
    }

    public ServiceResponse<?> register(User user) {
        user.setRole("ROLE_USER");
        if (user.getOrigin() == null) {
            user.setOrigin("native");
        }

        int status = userDao.create(user);

        if (status > 0) {
            return authenticate(new AuthRequest(user.getEmail(), user.getPassword()));
        } else {
            return new ServiceResponse<>("Cannot register user", null);
        }
    }

    public void logout() {

    }

    public ServiceResponse<?> loginOrRegisterViaGoogle(String authorizationCode) throws IOException {
        User userFromGoogle = googleOAuthClient.getUser(authorizationCode);
        User userFromDatabase = userDao.getByEmail(userFromGoogle.getEmail());

        if (userFromDatabase == null) {
            if (userFromGoogle.getGender() == null) {
                userFromGoogle.setGender("unknown");
            }
            if (userFromGoogle.getShowEmail() == null) {
                userFromGoogle.setShowEmail(true);
            }
            if (userFromGoogle.getPassword() == null) {
                userFromGoogle.setPassword(GOOGLE_USER_PASSWORD);
            }
            if (userFromGoogle.getRole() == null) {
                userFromGoogle.setRole("ROLE_USER");
            }
            if (userFromGoogle.getOrigin() == null) {
                userFromGoogle.setOrigin("google");
            }
            return register(userFromGoogle);
        } else {
            return authenticate(new AuthRequest(userFromDatabase.getEmail(), GOOGLE_USER_PASSWORD));
        }
    }

}
