package com.suite.suite_anp_service.alarm.entity;


import com.suite.suite_anp_service.alarm.dto.AlarmCategory;
import com.suite.suite_anp_service.baseTime.BaseTimeMongoEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Document(collation = "alarm_history")
@Getter
@NoArgsConstructor
public class AlarmHistory extends BaseTimeMongoEntity {

    @Id
    private String alarmId;
    @Enumerated(EnumType.STRING)
    private AlarmCategory category;
    private Long memberId;
    private String title;
    private String content;
    private Long suiteRoomId;

    @Builder
    public AlarmHistory(String alarmId, AlarmCategory category, Long memberId, String title, String content, Long suiteRoomId) {
        this.alarmId = alarmId;
        this.category = category;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.suiteRoomId = suiteRoomId;
    }
}
