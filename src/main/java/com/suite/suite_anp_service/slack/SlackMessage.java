package com.suite.suite_anp_service.slack;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SlackMessage {

    private final RestTemplate restTemplate;

    @Value("${slack.webhook-url}")
    private String slackWebhookUrl;

    public SlackMessage(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.slackWebhookUrl = slackWebhookUrl;
    }

    public void sendNotification(String message) {
        restTemplate.postForLocation(slackWebhookUrl, new SlackMessageDto(message));
    }

    @Getter
    static class SlackMessageDto {
        private String text;

        public SlackMessageDto(String text) {
            this.text = text;
        }
    }
}
