package com.jupiter.wyl.domain.chatBot.repository;

import com.jupiter.wyl.domain.chatBot.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatBotRepository extends MongoRepository<Message, String> {
    // userId로 대화 내역 조회
    List<Message> findByUserId(String userId);
}