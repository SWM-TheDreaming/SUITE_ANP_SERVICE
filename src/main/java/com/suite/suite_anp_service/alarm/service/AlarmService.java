package com.suite.suite_anp_service.alarm.service;

import com.suite.suite_anp_service.alarm.dto.AlarmCategory;
import com.suite.suite_anp_service.alarm.dto.AlarmMessage;
import com.suite.suite_anp_service.alarm.entity.AlarmHistory;

public interface AlarmService {

    void test(AlarmHistory alarmHistory);
    void saveAlarmHistory(Long memberId, String keyword, Long suiteRoomId, String fcmToken, AlarmCategory alarmCategory, AlarmMessage alarmMessage);
}
