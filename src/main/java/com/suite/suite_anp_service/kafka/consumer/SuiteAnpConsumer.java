package com.suite.suite_anp_service.kafka.consumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.suite.suite_anp_service.anp.entity.AnpOfMember;
import com.suite.suite_anp_service.anp.repository.AnpOfMemberRepository;
import com.suite.suite_anp_service.slack.SlackMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SuiteAnpConsumer {
    private final AnpOfMemberRepository anpOfMemberRepository;
    /*@Override
    @KafkaListener(topics = "${topic.USER_REGISTRATION_FCM}", groupId = "suite")
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        JSONParser parser = new JSONParser();
        int retryCount = 0;
        try {
            while(retryCount < 3) {
                try {
                    *//*if(retryCount < 3) {
                        throw new RuntimeException("Intentional error for testing");
                    }*//*
                    JSONObject jsonObject = (JSONObject) parser.parse(record.value());

                    Long memberId = Long.parseLong(((JSONObject) jsonObject.get("data")).get("memberId").toString());
                    String fcm = ((JSONObject) jsonObject.get("data")).get("fcm").toString();

                    anpOfMemberRepository.save(
                            AnpOfMember.builder()
                                    .memberId(memberId)
                                    .fcmToken(fcm)
                                    .point(0L)
                                    .alarmCount(0L).build()
                    );

                    acknowledgment.acknowledge();
                    return;
                }catch (Exception e) {
                    retryCount++;
                    handleError(record, acknowledgment, e, retryCount);
                }

            }
        } catch (Exception e) {
            handleError(record, acknowledgment, e, retryCount);
        }
    }

    private void handleError(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Exception e, int retryCount) {
        // 에러 처리 로직 수행
        System.err.println("Error handling message: " + record.value());
        // 에러가 3회 미만 이면 1초 뒤에 다시 시작함.
        if (retryCount < 3) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            acknowledgment.acknowledge(); // 에러 처리 후에도 메시지를 처리 완료로 표시
        } else {
            slackMessage.sendNotification(record.topic() + "메시지 소비 횟수가 3번이 넘었지만 에러가 발생하였습니다.");
            acknowledgment.acknowledge(); // 최종적으로 메시지 처리 완료로 표시
        }
    }*/

    @KafkaListener(topics = "${topic.USER_REGISTRATION_FCM}", groupId = "suite", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        int retryCount = 0;

        JSONObject jsonObject = (JSONObject) parser.parse(record.value());

        Long memberId = Long.parseLong(((JSONObject) jsonObject.get("data")).get("memberId").toString());
        String fcm = ((JSONObject) jsonObject.get("data")).get("fcm").toString();
        System.out.println("retryCount: " + retryCount);
        if(retryCount < 3) {
            throw new RuntimeException();
        }

        anpOfMemberRepository.save(
                AnpOfMember.builder()
                        .memberId(memberId)
                        .fcmToken(fcm)
                        .point(0L)
                        .alarmCount(0L).build()
        );

    }


}
