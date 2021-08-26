package com.kulik.airbnb.dao;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.kulik.airbnb.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class GoogleOAuthClient {

    private final String FIELDS = "names,emailAddresses,genders,birthdays,photos";

    @Value("${google.client_id}")
    String CLIENT_ID;

    @Value("${google.client_secret}")
    String CLIENT_SECRET;

    @Value("${google.redirect_uri}")
    String REDIRECT_URI;

    public User getUser(String authorizationCode) throws IOException {
        GoogleTokenResponse response =
                new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new GsonFactory(),
                        CLIENT_ID, CLIENT_SECRET, authorizationCode, REDIRECT_URI)
                        .execute();

        GoogleCredential credential = new GoogleCredential();
        credential.setAccessToken(response.getAccessToken());

        PeopleService peopleService = new PeopleService(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential);


        Person profile = peopleService.people().get("people/me")
                .setPersonFields(FIELDS)
                .execute();

        User user =  new User(null,
                profile.getNames() == null ? null : profile.getNames().get(0).getGivenName(),
                profile.getBirthdays() == null ? null :
                        new Date(profile.getBirthdays().get(0).getDate().getYear() - 1900,
                                profile.getBirthdays().get(0).getDate().getMonth() - 1,
                                profile.getBirthdays().get(0).getDate().getDay()),
                profile.getGenders() == null ? null : profile.getGenders().get(0).getValue(),
                profile.getPhotos() == null ? null : profile.getPhotos().get(0).getUrl(),
                profile.getEmailAddresses() == null ? null : profile.getEmailAddresses().get(0).getValue(),
                null, null, null, null, null, null);

        return user;
    }

}
