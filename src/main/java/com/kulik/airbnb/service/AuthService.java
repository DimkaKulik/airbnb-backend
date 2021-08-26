package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.GoogleOAuthDao;
import com.kulik.airbnb.dao.dto.AuthRequestDto;
import com.kulik.airbnb.dao.dto.UserDto;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final GoogleOAuthDao googleOAuthDao;
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager, GoogleOAuthDao googleOAuthDao, UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.googleOAuthDao = googleOAuthDao;
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> authenticate(AuthRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String token = jwtTokenProvider.createToken(request.getEmail());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> register(UserDto userDto) {
        userDto.setRole("ROLE_USER");
        int status = userDao.create(userDto);

        if (status > 0) {
            return authenticate(new AuthRequestDto(userDto.getEmail(), userDto.getPassword()));
        } else {
            return new ResponseEntity<>("Something went wrong, maybe such user already exists", HttpStatus.CONFLICT);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    public ResponseEntity<?> loginOrRegisterViaGoogle(String authorizationCode) throws IOException {
        UserDto userFromGoogle = googleOAuthDao.getUser(authorizationCode);
        UserDto userFromDatabase = userDao.getByEmail(userFromGoogle.getEmail());

        if (userFromDatabase == null) {
            userFromGoogle.fixNullFields();
            return register(userFromGoogle);
        } else {
            return authenticate(new AuthRequestDto(userFromDatabase.getEmail(), userFromDatabase.getPassword()));
        }
    }

}
