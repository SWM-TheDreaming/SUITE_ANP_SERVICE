package com.suite.suite_anp_service.alarm.entity;


import com.suite.suite_anp_service.baseTime.BaseTimeMongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collation = "alarm_history")
@Getter
@NoArgsConstructor
public class AlarmHistory extends BaseTimeMongoEntity {

    @Id
    private String alarmId;
    private String category;
    private Long memberId;
    private String title;
    private String content;
    private Long suiteRoomId;
}
