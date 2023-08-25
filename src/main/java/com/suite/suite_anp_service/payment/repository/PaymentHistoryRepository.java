package com.suite.suite_anp_service.payment.repository;


import com.suite.suite_anp_service.payment.entity.PaymentHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository extends MongoRepository<PaymentHistory, String> {
}
