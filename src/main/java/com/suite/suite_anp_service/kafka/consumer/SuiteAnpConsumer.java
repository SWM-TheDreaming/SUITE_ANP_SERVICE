package com.suite.suite_anp_service.kafka.consumer;

import com.suite.suite_anp_service.anp.entity.AnpOfMember;
import com.suite.suite_anp_service.anp.repository.AnpOfMemberRepository;
import com.suite.suite_anp_service.exception.PaymentFailedException;
import com.suite.suite_anp_service.exception.RepositoryException;
import com.suite.suite_anp_service.kafka.producer.SuiteAnpProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteAnpConsumer {
    private final AnpOfMemberRepository anpOfMemberRepository;
    private final SuiteAnpProducer suiteAnpProducer;
    @Value("${topic.DEPOSIT_PAYMENT_ERROR}") private String DEPOSIT_PAYMENT_ERROR;
    @Value("${topic.SUITEROOM_JOIN}") private String SUITEROOM_JOIN;

    @KafkaListener(topics = "${topic.USER_REGISTRATION_FCM}", groupId = "suite", containerFactory = "kafkaListenerContainerFactory")
    public void userRegistrationFCMConsume(ConsumerRecord<String, String> record) throws IOException, ParseException {
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

    @Transactional
    @KafkaListener(topics = "${topic.DEPOSIT_PAYMENT}", groupId = "suite", containerFactory = "kafkaListenerContainerFactory")
    public void suiteRoomJoinConsume(ConsumerRecord<String, String> record) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject = (JSONObject) parser.parse(record.value());
        JSONObject data = ((JSONObject) jsonObject.get("data"));
        Long suiteRoomId = Long.parseLong(data.get("suiteRoomId").toString());
        JSONObject authorizerDto = ((JSONObject) data.get("authorizerDto"));
        int depositAmount = Integer.parseInt(data.get("depositAmount").toString());
        Long memberId = Long.parseLong(authorizerDto.get("memberId").toString());

        try {
            AnpOfMember anpOfMember = anpOfMemberRepository.findByMemberId(memberId).orElseThrow(() -> new RepositoryException());
            anpOfMember.payPoints(depositAmount);
            suiteAnpProducer.sendMessage(SUITEROOM_JOIN, record.value());
            anpOfMember.increaseAlarmCount();
            //알림 전송 코드
        } catch (PaymentFailedException e) {
            log.error("Payment failed: Insufficient points -> While member {} is entering room {}", memberId, suiteRoomId);
            //rollback 진행
            suiteAnpProducer.sendMessage(DEPOSIT_PAYMENT_ERROR, record.value());
        }
    }


}
