package com.suite.suite_anp_service.alarm.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlarmMessage {
    Test("테스트입니다."),
    StartSuiteRoom("스터디가 시작되었습니다."),
    EnterSuiteRoom("스위트룸에 체크인하셨습니다."),
    ExitSuiteRoom("스위트룸을 나갔습니다."),
    PayComplete("보증급 납부가 완료되었습니다."),
    PayFailed("보증금 납부에 실패하였습니다."),
    Refund("보증금을 환불하였습니다."),
    DepositRefund("보증금을 환급받았습니다."),
    HallOfFrameSelection("회원님의 스위트룸이 명예의 전당 올라갔습니다.");

    private final String message;

    public String getMessage(String keyword) {
        return "\"" + keyword + "\"" + message;
    }

    public String getMessage() {
        return message;
    }

}
