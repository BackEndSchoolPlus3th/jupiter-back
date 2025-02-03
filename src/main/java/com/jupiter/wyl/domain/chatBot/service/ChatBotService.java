package com.jupiter.wyl.domain.chatBot.service;

import com.jupiter.wyl.domain.chatBot.dto.request.ChatBotRequest;
import com.jupiter.wyl.domain.chatBot.dto.response.ChatBotResponse;
import com.jupiter.wyl.domain.chatBot.entity.Message;
import com.jupiter.wyl.domain.chatBot.repository.ChatBotRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatBotService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ChatBotRepository chatBotRepository;

    public ChatBotService(RestTemplate restTemplate, ChatBotRepository chatBotRepository) {
        this.restTemplate = restTemplate;
        this.chatBotRepository = chatBotRepository;
    }

    // 사용자 메시지와 이전 대화 내용 전송
    public List<Message> getPreviousMessages(String userId) {
        // MongoDB에서 해당 userId에 대한 이전 대화 가져오기
        return chatBotRepository.findByUserId(userId);
    }

    // MongoDB에 채팅 메시지 저장
    public void saveMessage(String userId, String userMessage, String botResponse) {
        Message message = new Message();
        message.setUserId(userId);
        message.setUserMessage(userMessage);
        message.setBotResponse(botResponse);
        message.setTimestamp(LocalDateTime.now());

        chatBotRepository.save(message);
    }

    public String getChatGptResponse(String userId, String userMessage) {
        String url = "https://api.openai.com/v1/chat/completions";

        // MongoDB에서 이전 대화 가져오기
        List<Message> previousMessages = chatBotRepository.findByUserId(userId);

        ChatBotRequest request = new ChatBotRequest();
        request.setModel("gpt-3.5-turbo");

        // 이전 메시지들을 OpenAIRequest.Message 객체로 변환
        List<ChatBotRequest.Message> openAiMessages = previousMessages.stream()
                .map(msg -> new ChatBotRequest.Message("user", msg.getUserMessage()))
                .collect(Collectors.toList());

        // 새로운 사용자 메시지 추가
        ChatBotRequest.Message userMessageObj = new ChatBotRequest.Message("user", userMessage);
        openAiMessages.add(userMessageObj);

        request.setMessages(openAiMessages);

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<ChatBotRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatBotResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, ChatBotResponse.class);

        if (response.getBody() != null && !response.getBody().getChoices().isEmpty()) {
            return response.getBody().getChoices().get(0).getMessage().getContent();
        }
        return "Sorry, I couldn't get a response from ChatGPT.";
    }
}