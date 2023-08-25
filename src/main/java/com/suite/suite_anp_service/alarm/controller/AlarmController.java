package com.suite.suite_anp_service.alarm.controller;

import com.suite.suite_anp_service.alarm.entity.AlarmHistory;
import com.suite.suite_anp_service.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/test")
    public String test(@RequestBody AlarmHistory alarmHistory) {
        alarmService.test(alarmHistory);
        return "success";
    }
}
