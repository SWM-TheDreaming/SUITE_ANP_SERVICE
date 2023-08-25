package com.suite.suite_anp_service.alarm.service;

import com.suite.suite_anp_service.alarm.entity.AlarmHistory;
import com.suite.suite_anp_service.alarm.repository.AlarmHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmHistoryRepository alarmHistoryRepository;

    @Override
    public void test(AlarmHistory alarmHistory) {
        alarmHistoryRepository.save(alarmHistory);
    }
}
