package com.suite.suite_anp_service.alarm.controller;

import com.suite.suite_anp_service.alarm.entity.AlarmHistory;
import com.suite.suite_anp_service.alarm.service.AlarmService;
import com.suite.suite_anp_service.slack.SlackMessage;
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
    private final SlackMessage slackMessage;

    @PostMapping("/test")
    public String test(@RequestBody AlarmHistory alarmHistory) {
        alarmService.test(alarmHistory);
        return "success";
    }

    @PostMapping("/slack")
    public void slack() {
        slackMessage.sendNotification("방가 방가 ~");
    }
}
