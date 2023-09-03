package com.suite.suite_anp_service.kafka.consumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.suite.suite_anp_service.anp.entity.AnpOfMember;
import com.suite.suite_anp_service.anp.repository.AnpOfMemberRepository;
import com.suite.suite_anp_service.exception.PaymentFailedException;
import com.suite.suite_anp_service.exception.RepositoryException;
import com.suite.suite_anp_service.kafka.producer.SuiteAnpProducer;
import com.suite.suite_anp_service.slack.SlackMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteAnpConsumer {
    private final AnpOfMemberRepository anpOfMemberRepository;
    private final SuiteAnpProducer suiteAnpProducer;
    @Value("${SUITEROOM_JOIN_ERROR}") private String SUITEROOM_JOIN_ERROR;

    @KafkaListener(topics = "${topic.USER_REGISTRATION_FCM}", groupId = "suite", containerFactory = "kafkaListenerContainerFactory")
    public void userRegistrationFCMconsume(ConsumerRecord<String, String> record) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        int retryCount = 0;

        JSONObject jsonObject = (JSONObject) parser.parse(record.value());
        JSONObject data = ((JSONObject) jsonObject.get("data"));
        Long memberId = Long.parseLong(data.get("memberId").toString());
        String fcm = data.get("fcm").toString();

        /*if(retryCount < 3) {
            throw new RuntimeException();
        }*/
        anpOfMemberRepository.save(
                AnpOfMember.builder()
                        .memberId(memberId)
                        .fcmToken(fcm)
                        .point(0)
                        .alarmCount(0L).build()
        );

    }

    @KafkaListener(topics = "${SUITEROOM_JOIN}", groupId = "suite", containerFactory = "kafkaListenerContainerFactory")
    public void suiteRoomJoinconsume(ConsumerRecord<String, String> record) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject = (JSONObject) parser.parse(record.value());
        JSONObject data = ((JSONObject) jsonObject.get("data"));
        Long memberId = Long.parseLong(data.get("memberId").toString());
        Long suiteRoomId = Long.parseLong(data.get("suiteRoomId").toString());
        int depositAmount = Integer.parseInt(data.get("depositAmount").toString());

        try {
            AnpOfMember anpOfMember = anpOfMemberRepository.findByMemberId(memberId).orElseThrow(() -> new RepositoryException());
            anpOfMember.payPoints(depositAmount);
            anpOfMember.increaseAlarmCount();
            //알림 전송 코드
        } catch (PaymentFailedException e) {
            log.error("Payment failed: Insufficient points -> While member {} is entering room {}", memberId, suiteRoomId);
            //rollback 진행
            suiteAnpProducer.sendRollBackMessage(SUITEROOM_JOIN_ERROR, record.value());
        }



    }


}
