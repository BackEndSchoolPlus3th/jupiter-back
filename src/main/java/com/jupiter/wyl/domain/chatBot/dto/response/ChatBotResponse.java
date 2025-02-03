package com.jupiter.wyl.domain.chatBot.dto.response;

import lombok.Data;

import java.util.List;


@Data
public class ChatBotResponse {

    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;

        @Data
        public static class Message {
            private String role;
            private String content;
        }
    }
}