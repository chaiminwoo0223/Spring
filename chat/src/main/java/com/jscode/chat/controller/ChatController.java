package com.jscode.chat.controller;

import com.jscode.chat.service.ChatService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions; // DefaultChatOptions 대신 인터페이스 import
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatClient chatClient;
    private final ChatService chatService;

    public ChatController(ChatClient.Builder chatClientBuilder, ChatService chatService) {
        this.chatClient = chatClientBuilder.build();
        this.chatService = chatService;
    }

    @GetMapping("/ai")
    public String generate(String userPrompt) {
        return this.chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestBody @Valid PromptBody promptBody) {
        Prompt prompt = createPrompt(promptBody);
        return chatService.stream(prompt, promptBody.conversationId());
    }

    @PostMapping(value = "/call", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatResponse call(@RequestBody @Valid PromptBody promptBody) {
        Prompt prompt = createPrompt(promptBody);
        return chatService.call(prompt, promptBody.conversationId());
    }

    @PostMapping(value = "/cs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatService.CsEvaluation cs(@RequestBody @Valid PromptBody promptBody) {
        return chatService.csEvaluation(createPrompt(promptBody), promptBody.conversationId());
    }

    // 요청 전용 DTO. 여기엔 Spring AI 내부 타입을 절대 넣지 않는다.
    public record ChatOptionsRequest(
            @Nullable String model,
            @Nullable Double frequencyPenalty,
            @Nullable Integer maxTokens,
            @Nullable Double presencePenalty,
            @Nullable List<String> stopSequences,
            @Nullable Double temperature,
            @Nullable Integer topK,
            @Nullable Double topP
    ) {
        ChatOptions toChatOptions() {
            return ChatOptions.builder()
                    .model(model)
                    .frequencyPenalty(frequencyPenalty)
                    .maxTokens(maxTokens)
                    .presencePenalty(presencePenalty)
                    .stopSequences(stopSequences)
                    .temperature(temperature)
                    .topK(topK)
                    .topP(topP)
                    .build();
        }
    }

    public record PromptBody(
            @Nullable String conversationId,
            @Nullable String userPrompt,
            @Nullable String systemPrompt,
            @Nullable ChatOptionsRequest chatOptions
    ) {}

    private static Prompt createPrompt(PromptBody promptBody) {
        List<Message> messages = new ArrayList<>();

        if (promptBody.systemPrompt() != null && !promptBody.systemPrompt().isBlank()) {
            messages.add(new SystemMessage(promptBody.systemPrompt()));
        }

        messages.add(new UserMessage(promptBody.userPrompt()));

        Prompt.Builder promptBuilder = Prompt.builder().messages(messages);

        if (promptBody.chatOptions() != null) {
            promptBuilder.chatOptions(promptBody.chatOptions().toChatOptions());
        }

        return promptBuilder.build();
    }
}
