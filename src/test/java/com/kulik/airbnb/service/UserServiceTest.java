package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.security.JwtTokenProvider;
import com.kulik.airbnb.security.JwtUserDetailsService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class UserServiceTest {

    private static final String ABSOLUTE_ADDRESS = "https://absolute.com/address";
    private static final String RELATIVE_ADDRESS = "/relative/address";
    private static final String INVALID_TOKEN = "invalid_token";

    @Value("${google_cloud_url}")
    String googleCloudUrl;

    @Mock
    private UserDao userDao;
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.userService = new UserService(new JwtTokenProvider(new JwtUserDetailsService(userDao)), userDao);
    }

    @Test
    public void getPage() {
        List<User> daoResult = getUsersWithDifferentAvatarTypes();
        given(userDao.getPage(anyInt(), anyInt())).willReturn(daoResult);

        List<User> serviceResult = userService.getPage(3, 0);

        assertTrue(serviceResult.get(0).getAvatar().equals(googleCloudUrl + RELATIVE_ADDRESS));
        assertTrue(serviceResult.get(1).getAvatar().equals(ABSOLUTE_ADDRESS));
        assertTrue(serviceResult.get(2).getAvatar() == null);
    }

    private List<User> getUsersWithDifferentAvatarTypes() {
        User relativeAvatarPathUser = new User();
        User absoluteAvatarPathUser = new User();
        User nullAvatarPathUser = new User();

        relativeAvatarPathUser.setAvatar(RELATIVE_ADDRESS);
        absoluteAvatarPathUser.setAvatar(ABSOLUTE_ADDRESS);
        nullAvatarPathUser.setAvatar(null);

        List<User> result = new ArrayList<>();
        result.add(relativeAvatarPathUser);
        result.add(absoluteAvatarPathUser);
        result.add(nullAvatarPathUser);

        return result;
    }

    @Test
    public void get() {
        User daoResultUser = new User();
        daoResultUser.setName("John Jackson");
        daoResultUser.setGender("male");
        daoResultUser.setAvatar(ABSOLUTE_ADDRESS);

        given(userDao.getById(anyLong())).willReturn(daoResultUser);
        assertTrue(userService.get(anyLong()).getName().equals("John Jackson"));
        assertTrue(userService.get(anyLong()).getGender().equals("male"));
        assertTrue(userService.get(anyLong()).getAvatar().equals(ABSOLUTE_ADDRESS));
    }

    @Test
    public void confirmUserInvalidTokenTest() throws Exception {
        assertThrows(Exception.class, () -> {
            userService.confirmUser(INVALID_TOKEN);
        });
    }
}