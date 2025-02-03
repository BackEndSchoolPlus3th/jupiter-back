package com.jupiter.wyl.domain.chatBot.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Message {

    @Id
    private String id;  // MongoDB에서 자동 생성되는 ID
    private String userId;  // 사용자의 ID
    private String userMessage;  // 사용자 메시지
    private String botResponse;  // 챗봇의 응답
    private LocalDateTime timestamp;  // 메시지 전송 시간
}