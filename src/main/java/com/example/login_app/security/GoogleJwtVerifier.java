package com.example.login_app.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;

public class GoogleJwtVerifier {

    private static final String CLIENT_ID =
            "753396852554-rfqhhu5rujso5b3vfqh7ajelr07ecbjb.apps.googleusercontent.com";

    public static String verify(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            ).setAudience(Collections.singletonList(CLIENT_ID)).build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken != null) {
                return idToken.getPayload().getEmail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
