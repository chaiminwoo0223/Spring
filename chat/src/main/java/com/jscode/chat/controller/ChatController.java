package com.jscode.chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai")
    public String generate(String userPrompt) {
        return this.chatClient.prompt()
                .user(userPrompt)
                .call()
                .content(); // 받아온 응답 중 메타데이터는 버리고, 순수 content만 추출!
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(String userPrompt) {
        return this.chatClient.prompt()
                .user(userPrompt)
                .stream()
                .content();
    }
}
