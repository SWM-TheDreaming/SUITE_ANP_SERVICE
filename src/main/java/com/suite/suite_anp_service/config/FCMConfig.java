package com.suite.suite_anp_service.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Configuration
public class FCMConfig {
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        ClassPathResource resource = new ClassPathResource("firebase/suite-firebase-admin.json");

        //InputStream refreshToken = resource.getInputStream();

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

        if(firebaseAppList != null && !firebaseAppList.isEmpty()) {
            for(FirebaseApp app : firebaseAppList) {
                if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                    firebaseApp = app;
            }
        } else {
            try {
                InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                Gson gson = new Gson();
                FirebaseOptions options = gson.fromJson(reader, FirebaseOptions.class);
//                FirebaseOptions options = FirebaseOptions.builder()
//                        .setCredentials(GoogleCredentials.fromStream(refreshToken)).build();
                firebaseApp = FirebaseApp.initializeApp(options);

            } catch(Exception e) {
                throw new RuntimeException("Failed to read Firebase Admin SDK JSON file", e);
            }

        }
        return FirebaseMessaging.getInstance(firebaseApp);
    }

}
