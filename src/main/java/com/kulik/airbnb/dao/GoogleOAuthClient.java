package com.kulik.airbnb.dao;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.kulik.airbnb.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Component
public class GoogleOAuthClient {

    private static final String FIELDS = "names,emailAddresses,genders,birthdays,photos";

    @Value("${google.client_id}")
    String clientId;

    @Value("${google.client_secret}")
    String clientSecret;

    @Value("${google.redirect_uri}")
    String redirectUri;

    public User getUser(String authorizationCode) throws IOException {
        GoogleTokenResponse response =
                new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new GsonFactory(),
                        clientId, clientSecret, authorizationCode, redirectUri)
                        .execute();

        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod());
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
                        new GregorianCalendar(
                                profile.getBirthdays().get(0).getDate().getYear(),
                                profile.getBirthdays().get(0).getDate().getMonth() - 1,
                                profile.getBirthdays().get(0).getDate().getDay()),
                profile.getGenders() == null ? "unknown" : profile.getGenders().get(0).getValue(),
                profile.getPhotos() == null ? null : profile.getPhotos().get(0).getUrl(),
                profile.getEmailAddresses() == null ? null : profile.getEmailAddresses().get(0).getValue(),
                true, null, null, null, null);

        return user;
    }

}
