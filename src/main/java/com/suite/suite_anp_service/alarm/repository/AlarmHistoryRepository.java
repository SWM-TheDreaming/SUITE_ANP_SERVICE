package com.suite.suite_anp_service.alarm.repository;

import com.suite.suite_anp_service.alarm.entity.AlarmHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmHistoryRepository extends MongoRepository<AlarmHistory, String> {

}
