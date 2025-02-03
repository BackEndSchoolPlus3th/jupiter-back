package com.jupiter.wyl.domain.chatBot.dto.request;

import lombok.Data;

@Data
public class ChatRequest {
    private String userId;
    private String userMessage;
}