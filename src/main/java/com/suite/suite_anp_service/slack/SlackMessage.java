package com.suite.suite_anp_service.slack;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class SlackMessage {

    private final RestTemplate restTemplate;

    private final String slackWebhookUrl;

    public SlackMessage(RestTemplate restTemplate, String slackWebhookUrl) {
        this.restTemplate = restTemplate;
        this.slackWebhookUrl = slackWebhookUrl;
    }

    public void sendNotification(String message) {
        restTemplate.postForLocation(slackWebhookUrl, new SlackMessageDto(message));
    }

    @Getter
    static class SlackMessageDto {
        private final List<SlackBlock> blocks;

        public SlackMessageDto(String text) {
            this.blocks = new ArrayList<>();
            this.blocks.add(new SlackBlock(new SlackTextBlock(text)));
        }
    }

    @Getter
    static class SlackBlock {
        private String type;
        private SlackTextBlock text;

        public SlackBlock(SlackTextBlock slackTextBlock) {
            this.type = "section";
            this.text = slackTextBlock;
        }
    }

    @Getter
    static class SlackTextBlock {
        private String type;
        private String text;

        public SlackTextBlock(String text) {
            this.type = "mrkdwn";
            this.text = text;
        }
    }
}
