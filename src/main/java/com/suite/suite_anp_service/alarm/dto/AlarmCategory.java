package com.suite.suite_anp_service.alarm.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlarmCategory {
    Test("[테스트]"),
    SuiteRoom("[스위트룸]"),
    Notice("[공지사항]"),
    Payment("[결제]"),
    HallOfFame("[명예의 전당]");

    @Getter
    private final String category;


}
