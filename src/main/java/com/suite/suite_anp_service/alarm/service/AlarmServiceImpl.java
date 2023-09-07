package com.suite.suite_anp_service.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.suite.suite_anp_service.alarm.dto.AlarmCategory;
import com.suite.suite_anp_service.alarm.dto.AlarmMessage;
import com.suite.suite_anp_service.alarm.entity.AlarmHistory;
import com.suite.suite_anp_service.alarm.repository.AlarmHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmHistoryRepository alarmHistoryRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void test(AlarmHistory alarmHistory) {
        sendFCMNotification(alarmHistory.getMemberId(), null, "fMhev3CBQlC6RwdPssW3yr:APA91bFtwTzRMwDH5zVQ-lSpzZGxLTaQ1viEPKbuHaQBkFIRQYAyOi9kO8kkHv9hBpf6TajGiuWIdeqf_6h0JIDb43bPA7CgoRTDV8X6aAigaarj-N0RoapgpVb1-R5x7vOx1uMnna4K", AlarmCategory.Test, AlarmMessage.Test);
        alarmHistoryRepository.save(alarmHistory);
    }

    @Override
    public void saveAlarmHistory(Long memberId, String keyword, Long suiteRoomId, String fcmToken, AlarmCategory alarmCategory, AlarmMessage alarmMessage) {
        AlarmHistory alarmHistory = AlarmHistory.builder()
                .category(alarmCategory)
                .memberId(memberId)
                .title(alarmCategory.getCategory())
                .content(alarmMessage.getMessage(keyword))
                .suiteRoomId(suiteRoomId).build();
        alarmHistoryRepository.save(alarmHistory);

        sendFCMNotification(memberId, keyword, fcmToken, alarmCategory, alarmMessage);
    }

    private void sendFCMNotification(Long memberId, String keyword, String fcmToken, AlarmCategory alarmCategory, AlarmMessage alarmMessage) {

        Notification notification = Notification.builder()
                .setTitle(alarmCategory.getCategory())
                .setBody((keyword == null) ? alarmMessage.getMessage() : alarmMessage.getMessage(keyword)).build();
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification).build();
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("firebaseMessage failed: While member {} ", memberId);
        }
    }

}
