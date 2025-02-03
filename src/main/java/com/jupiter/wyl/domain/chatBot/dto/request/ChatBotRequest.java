package com.jupiter.wyl.domain.chatBot.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ChatBotRequest {

    private String model;  // 모델 이름
    private List<Message> messages;  // 사용자 메시지 리스트

    @Data // Message 클래스 정의
    public static class Message {
        private String role;  // 사용자 또는 시스템
        private String content;  // 메시지 내용

        // 두 개의 인자를 받는 생성자 추가
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

}