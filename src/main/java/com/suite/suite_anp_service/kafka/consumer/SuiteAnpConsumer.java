package com.suite.suite_anp_service.kafka.consumer;

import com.suite.suite_anp_service.anp.entity.AnpOfMember;
import com.suite.suite_anp_service.anp.repository.AnpOfMemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SuiteAnpConsumer {
    private final AnpOfMemberRepository anpOfMemberRepository;

    @KafkaListener(topics = "${topic.USER_REGISTRATION_FCM}", groupId = "suite", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) throws IOException {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(message);

            Long memberId = Long.parseLong(((JSONObject) jsonObject.get("data")).get("memberId").toString());
            String fcm = ((JSONObject) jsonObject.get("data")).get("fcm").toString();

            anpOfMemberRepository.save(
                    AnpOfMember.builder()
                            .memberId(memberId)
                            .fcmToken(fcm)
                            .point(0L)
                            .alarmCount(0L).build()
            );

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
