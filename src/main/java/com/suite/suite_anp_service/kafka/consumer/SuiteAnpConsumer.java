package com.suite.suite_anp_service.kafka.consumer;

import com.suite.suite_anp_service.alarm.dto.AlarmCategory;
import com.suite.suite_anp_service.alarm.dto.AlarmMessage;
import com.suite.suite_anp_service.alarm.service.AlarmService;
import com.suite.suite_anp_service.anp.entity.AnpOfMember;
import com.suite.suite_anp_service.anp.repository.AnpOfMemberRepository;
import com.suite.suite_anp_service.exception.PaymentFailedException;
import com.suite.suite_anp_service.exception.RepositoryException;
import com.suite.suite_anp_service.kafka.producer.SuiteAnpProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteAnpConsumer {
    private final AnpOfMemberRepository anpOfMemberRepository;
    private final SuiteAnpProducer suiteAnpProducer;
    private final AlarmService alarmService;
    @Value("${topic.SUITEROOM_JOIN}") private String SUITEROOM_JOIN;
    @Value("${topic.SUITEROOM_CANCELJOIN_ERROR}") private String SUITEROOM_CANCELJOIN_ERROR;

    @KafkaListener(topics = "${topic.USER_REGISTRATION_FCM}", groupId = "suite", containerFactory = "kafkaListenerContainerFactory")
    public void userRegistrationFCMConsume(ConsumerRecord<String, String> record) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject = (JSONObject) parser.parse(record.value());
        JSONObject data = ((JSONObject) jsonObject.get("data"));
        Long memberId = Long.parseLong(data.get("memberId").toString());
        String fcm = data.get("fcm").toString();

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
        String suiteRoomTitle = String.valueOf(data.get("suiteRoomTitle"));
        int depositAmount = Integer.parseInt(data.get("depositAmount").toString());
        Long memberId = Long.parseLong(authorizerDto.get("memberId").toString());
        boolean isHost = (boolean) data.get("isHost");

        try {
            AnpOfMember anpOfMember = anpOfMemberRepository.findByMemberId(memberId).orElseThrow(() -> new RepositoryException());
            anpOfMember.payPoints(depositAmount);
            suiteAnpProducer.sendMessage(SUITEROOM_JOIN, record.value());
            anpOfMember.increaseAlarmCount();
            //알림 전송 코드
            if(!isHost) {
                AnpOfMember host = anpOfMemberRepository.findByMemberId(Long.parseLong(data.get("hostMemberId").toString())).orElseThrow(() -> new RepositoryException());
                alarmService.saveAlarmHistory(host.getMemberId(), suiteRoomTitle, suiteRoomId, host.getFcmToken(), AlarmCategory.SuiteRoom, AlarmMessage.EnterSuiteRoom);
            }

        } catch (PaymentFailedException e) {
            log.error("Payment failed: Insufficient points -> While member {} is entering room {}", memberId, suiteRoomId);
        }
    }


    @Transactional
    @KafkaListener(topics = "${topic.SUITEROOM_CANCELJOIN}", groupId = "suite", containerFactory = "kafkaListenerDefaultContainerFactory")
    public void suiteRoomCancelJoinConsume(ConsumerRecord<String, String> record) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject = (JSONObject) parser.parse(record.value());
        JSONObject data = ((JSONObject) jsonObject.get("data"));
        Long suiteRoomId = Long.parseLong(data.get("suiteRoomId").toString());
        JSONObject authorizerDto = ((JSONObject) data.get("authorizerDto"));
        String suiteRoomTitle = String.valueOf(data.get("suiteRoomTitle"));
        int depositAmount = Integer.parseInt(data.get("depositAmount").toString());
        Long memberId = Long.parseLong(authorizerDto.get("memberId").toString());
        boolean isHost = (boolean) data.get("isHost");

        try {
            AnpOfMember anpOfMember = anpOfMemberRepository.findByMemberId(memberId).orElseThrow(() -> new RepositoryException());
            anpOfMember.refundPoints(depositAmount);
            anpOfMember.increaseAlarmCount();
            //알림 전송 코드
            if(!isHost) {
                AnpOfMember host = anpOfMemberRepository.findByMemberId(Long.parseLong(data.get("hostMemberId").toString())).orElseThrow(() -> new RepositoryException());
                alarmService.saveAlarmHistory(host.getMemberId(), suiteRoomTitle, suiteRoomId, host.getFcmToken(), AlarmCategory.SuiteRoom, AlarmMessage.ExitSuiteRoom);
            }
        } catch (Exception e) {
            log.error("Refund failed -> While member {} is entering room {}", memberId, suiteRoomId);
            suiteAnpProducer.sendMessage(SUITEROOM_CANCELJOIN_ERROR, record.value());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional
    @KafkaListener(topics = "${topic.START_NOTIFICATION}", groupId = "suite", containerFactory = "kafkaListenerDefaultContainerFactory")
    public void startNotificationConsume(ConsumerRecord<String, String> record) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(record.value());
        JSONObject data = ((JSONObject) jsonObject.get("data"));
        Long suiteRoomId = Long.parseLong(data.get("suiteRoomId").toString());
        String suiteRoomTitle = String.valueOf(data.get("suiteRoomTitle"));
        JSONArray participants = ((JSONArray) jsonObject.get("participants"));
        AnpOfMember member;
        for(Object obj : participants) {
            JSONObject participant = (JSONObject) obj;
            member = anpOfMemberRepository.findByMemberId(Long.parseLong(participant.get("memberId").toString())).orElseThrow(() -> new RepositoryException());
            alarmService.saveAlarmHistory(member.getMemberId(), suiteRoomTitle, suiteRoomId, member.getFcmToken(), AlarmCategory.SuiteRoom, AlarmMessage.StartSuiteRoom);
        }

    }

}
