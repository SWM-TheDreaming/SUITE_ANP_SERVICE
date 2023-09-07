package com.suite.suite_anp_service.anp.entity;

import com.suite.suite_anp_service.anp.dto.ResAnpOfMemberDto;
import com.suite.suite_anp_service.exception.PaymentFailedException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "anp_of_member")
public class AnpOfMember {
    @Id
    @Column(name = "member_id", unique = true, nullable = false)
    private Long memberId;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "point")
    private Integer point;

    @Column(name = "alarm_count")
    private Long alarmCount;

    @Builder
    public AnpOfMember(Long memberId, String fcmToken, Integer point, Long alarmCount) {
        this.memberId = memberId;
        this.fcmToken = fcmToken;
        this.point = point;
        this.alarmCount = alarmCount;
    }

    public void payPoints(Integer point) {
        if(this.point - point >= 0)
            this.point -= point;
        else throw new PaymentFailedException();
    }

    @Transactional
    public void refundPoints(Integer point) {
        this.point += point;
    }

    public void increaseAlarmCount() {
        this.alarmCount += 1;
    }

    public ResAnpOfMemberDto toResAnpOfMemberDto() {
        return ResAnpOfMemberDto.builder()
                .memberId(memberId)
                .point(point).build();
    }
}
