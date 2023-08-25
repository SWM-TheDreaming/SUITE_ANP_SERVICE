package com.suite.suite_anp_service.payment.entity;

import com.suite.suite_anp_service.baseTime.BaseTimeMongoEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collation = "payment_history")
public class PaymentHistory extends BaseTimeMongoEntity {
    @Id
    private String paymentId;
    private Long memberId;
    private Long suiteRoomId;
    private int pointCharge;
}
