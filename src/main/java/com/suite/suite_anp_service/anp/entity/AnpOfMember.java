package com.suite.suite_anp_service.anp.entity;

import com.suite.suite_anp_service.baseTime.BaseTimeMongoEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "anp_of_member")
public class AnpOfMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long memberId;
    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "point")
    private Long point;

    @Column(name = "alarm_count")
    private Long alarmCount;
}
