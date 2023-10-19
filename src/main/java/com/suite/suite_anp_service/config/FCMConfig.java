package com.suite.suite_anp_service.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
public class FCMConfig {

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {

        ClassPathResource resource = new ClassPathResource("firebase/suite-firebase-admin.json");
        System.out.println(resource);
        InputStream refreshToken = resource.getInputStream();
        System.out.println(refreshToken);

        // Gson 인스턴스 생성
        Gson gson = new GsonBuilder().setLenient().create();

        try {
            // JSON 데이터 파싱
            InputStreamReader reader = new InputStreamReader(refreshToken);
            JsonObject jsonCredentials = gson.fromJson(reader, JsonObject.class);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(jsonCredentials.toString().getBytes())))
                    .build();

            FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
            return FirebaseMessaging.getInstance(firebaseApp);
        } catch (JsonSyntaxException e) {
            e.printStackTrace(); // 원하는 오류 처리 방식으로 변경
            return null;
        }
    }
}
