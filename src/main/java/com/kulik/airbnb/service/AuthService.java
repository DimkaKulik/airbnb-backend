package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.dao.GoogleOAuthClient;
import com.kulik.airbnb.model.AuthRequest;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.security.JwtTokenProvider;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    private static final String CONFIRMATION_ENDPOINT = "/users/confirmation?token=";

    private final AuthenticationManager authenticationManager;
    private final GoogleOAuthClient googleOAuthClient;
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${mailgun.endpoint}")
    String mailgunEndpoint;

    @Value("${mailgun.api_key}")
    String mailgunApiKey;

    @Value("${domain}")
    String domain;

    public AuthService(AuthenticationManager authenticationManager, GoogleOAuthClient googleOAuthClient,
                       UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.googleOAuthClient = googleOAuthClient;
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String authenticate(AuthRequest request) throws Exception {
        userDao.getByEmail(request.getEmail());
        if (!userDao.getConfirmationField(request.getEmail()).equals("confirmed")) {
            throw new Exception("Account is not confirmed");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String token = jwtTokenProvider.createAuthorizationToken(request.getEmail());

        return token;
    }

    public void register(User user) throws Exception {
        if (user.getPassword() == null) {
            throw new Exception("Password cannot be null");
        }

        if (userDao.create(user) > 0) {
            sendConfirmationLink(user.getEmail());
        }
    }

    public void logout() {

    }

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
        String token = jwtTokenProvider.createConfirmationToken(email);
        String confirmationLink = domain + CONFIRMATION_ENDPOINT + token;
        userDao.insertConfirmationToken(email, token);

        HttpResponse<String> request = Unirest.post(mailgunEndpoint)
			.basicAuth("api", mailgunApiKey)
                .queryString("from", "Excited User <dzmitser.kulik@gmail.com>")
                .queryString("to", email)
                .queryString("subject", "Email verification")
                .queryString("text", "Go to " + confirmationLink + " to verify your email.")
                .asString();

        return request.getBody();
    }
}
