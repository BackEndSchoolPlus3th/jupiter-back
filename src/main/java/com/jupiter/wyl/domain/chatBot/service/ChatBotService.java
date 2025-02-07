package com.jupiter.wyl.domain.chatBot.service;

import com.jupiter.wyl.domain.chatBot.dto.request.ChatBotRequest;
import com.jupiter.wyl.domain.chatBot.dto.response.ChatBotResponse;
import com.jupiter.wyl.domain.chatBot.entity.Message;
import com.jupiter.wyl.domain.chatBot.repository.ChatBotRepository;
import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.service.MovieMainService;
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

    private final MovieMainService movieMainService;
    private final RestTemplate restTemplate;
    private final ChatBotRepository chatBotRepository;

    public ChatBotService(MovieMainService movieMainService, RestTemplate restTemplate, ChatBotRepository chatBotRepository) {
        this.movieMainService = movieMainService;
        this.restTemplate = restTemplate;
        this.chatBotRepository = chatBotRepository;
    }

    public String getChatGptMovieResponse(String userId, String userMessage) {
        String responseMessage = "";

        // 영화 관련 질문을 판단하는 간단한 조건 예시
        if (userMessage.contains("영화") || userMessage.contains("추천")) {
            System.out.println("영화 관련 질문입니다.");
            responseMessage = handleMovieRequest(userMessage);
        } else {
            System.out.println("openai 질문입니다.");
            responseMessage = getChatGptResponse(userId, userMessage);
        }

        return responseMessage;
    }

    private String handleMovieRequest(String userMessage) {
        String genre = determineGenre(userMessage);
        System.out.println("선택된 장르: " + genre);

        // 영화 데이터를 가져오기
        List<MovieMainDto> movies = movieMainService.getMoviesByGenre(genre);

        if (movies.isEmpty()) {
            return "죄송합니다, 해당 장르의 영화 정보를 찾을 수 없습니다.";
        }

        List<MovieMainDto> top3Movies = movies.stream()
                .limit(3)  // 3개만 추천
                .collect(Collectors.toList());

        // JSON 형식으로 영화 추천 리스트 생성
        StringBuilder movieListMessage = new StringBuilder();
        movieListMessage.append("추천 영화 리스트:<br>");

        for (int i = 0; i < top3Movies.size(); i++) {
            MovieMainDto movie = top3Movies.get(i);
            String posterPath = movie.getPosterPath();
            String movieId = movie.getId().toString();

            // 포스터 URL이 null일 경우 기본 이미지로 대체
            String posterUrl = (posterPath != null) ? "https://image.tmdb.org/t/p/w500" + posterPath : "https://via.placeholder.com/500x750?text=No+Image";

            // JSON 형식으로 이미지와 정보를 포함
            movieListMessage.append("- ").append(movie.getTitle())
                    .append("<br>줄거리: ").append(movie.getOverview())
                    .append("<br><a href=\"/detail/").append(movieId).append("\" target=\"_blank\">")
                    .append("<img src=\"").append(posterUrl).append("\" alt=\"").append(movie.getTitle()).append("\" />")
                    .append("</a>");

            // 마지막 항목이 아니면 줄바꿈 추가
            if (i < top3Movies.size() - 1) {
                movieListMessage.append("<br><br>");
            }
        }

        return movieListMessage.toString();
    }

    // 장르를 판단하는 메서드
    private String determineGenre(String userMessage) {
        if (userMessage.contains("액션")) {
            return "28";
        } else if (userMessage.contains("코미디")) {
            return "35";
        } else {
            return "0";
        }
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
        request.setModel("gpt-4o-mini");

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