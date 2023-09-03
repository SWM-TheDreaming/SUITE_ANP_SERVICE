package com.suite.suite_anp_service.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteAnpProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRollBackMessage(String topic, Object data) {
        log.info("SuiteRoom-Join-Error message : {}", data);
        JSONObject obj = new JSONObject();
        obj.put("uuid", "SuiteAnpProducer/" + Instant.now().toEpochMilli());
        obj.put("data", data);
        this.kafkaTemplate.send(topic, obj.toJSONString());
    }

}
