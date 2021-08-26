package com.kulik.airbnb.dao;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.kulik.airbnb.dao.dto.UserDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class GoogleOAuthDao {

    private final String FIELDS = "names,emailAddresses,genders,birthdays,photos";
    private final String CLIENT_ID = "194169570467-44l0mrhb2u7l0p4h6bdpuq1q1866tbbk.apps.googleusercontent.com";
    private final String CLIENT_SECRET = "kTG2VPtVayLuQC3zyOp1mA2k";
    private final String REDIRECT_URI = "http://localhost:5500";

    public UserDto getUser(String authorizationCode) throws IOException {
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

        UserDto userDto =  new UserDto(null,
                profile.getNames() == null ? null : profile.getNames().get(0).getGivenName(),
                profile.getBirthdays() == null ? null :
                        new Date(profile.getBirthdays().get(0).getDate().getYear() - 1900,
                                profile.getBirthdays().get(0).getDate().getMonth() - 1,
                                profile.getBirthdays().get(0).getDate().getDay()),
                profile.getGenders() == null ? null : profile.getGenders().get(0).getValue(),
                profile.getPhotos() == null ? null : profile.getPhotos().get(0).getUrl(),
                profile.getEmailAddresses() == null ? null : profile.getEmailAddresses().get(0).getValue(),
                null, null, null, null, null);

        return userDto;
    }

}
