package com.jupiter.wyl.domain.chatBot.controller;

import com.jupiter.wyl.domain.chatBot.dto.request.ChatRequest;
import com.jupiter.wyl.domain.chatBot.entity.Message;
import com.jupiter.wyl.domain.chatBot.repository.ChatBotRepository;
import com.jupiter.wyl.domain.chatBot.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiV1ChatBotController {

    private final ChatBotService chatBotService;

    @Autowired
    public ApiV1ChatBotController(ChatBotService chatBotService, ChatBotRepository chatBotRepository) {
        this.chatBotService = chatBotService;
    }

    // "/app/ask"로 메시지 전송시 메소드 호출
    @MessageMapping("/ask")  // 클라이언트 요청
    @SendTo("/topic/response")
    public String askChatGpt(ChatRequest chatRequest) throws Exception {

        String userId = chatRequest.getUserId();
        String userMessage = chatRequest.getUserMessage();

        // 서버로부터 받은 메시지 처리 (예: chatGPT로 메시지 전송)
        String gptResponse = chatBotService.getChatGptResponse(userId, userMessage);

        // MongoDB에 사용자 메시지와 챗봇 응답 저장
        chatBotService.saveMessage(userId, userMessage, gptResponse);

        return gptResponse;  // GPT 응답을 클라이언트로 전송
    }

    // 이전 메시지 불러오기
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/chat/previousMessages")
    public ResponseEntity<List<Message>> getPreviousMessages(@RequestParam(name = "userId") String userId) {
        try {
            List<Message> messages = chatBotService.getPreviousMessages(userId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            // 예외가 발생한 경우
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}