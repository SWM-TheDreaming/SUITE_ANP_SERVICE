package com.suite.suite_anp_service.anp.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "anp_of_member")
public class AnpOfMember {
    @Id
    @Column(name = "member_id", unique = true, nullable = false)
    private Long memberId;
    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "point")
    private Long point;

    @Column(name = "alarm_count")
    private Long alarmCount;

    @Builder
    public AnpOfMember(Long memberId, String fcmToken, Long point, Long alarmCount) {
        this.memberId = memberId;
        this.fcmToken = fcmToken;
        this.point = point;
        this.alarmCount = alarmCount;
    }
}
