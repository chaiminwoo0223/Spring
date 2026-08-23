package com.jscode.chat.controller;

import com.jscode.chat.service.ChatService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.DefaultChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
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
                .content(); // 받아온 응답 중 메타데이터는 버리고, 순수 content만 추출!
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

    private static Prompt createPrompt(PromptBody promptBody) {
        // 1. 메시지들을 차곡차곡 담을 빈 리스트 생성하기
        List<Message> messages = new ArrayList<>();

        // 2. systemPrompt가 입력으로 들어왔다면 리스트에 넣기
        if (promptBody.systemPrompt() != null && !promptBody.systemPrompt().isBlank()) {
            messages.add(new SystemMessage(promptBody.systemPrompt()));
        }

        // 3. userPrompt는 필수 값이니 무조건 리스트에 넣기
        messages.add(new UserMessage(promptBody.userPrompt()));

        // 4. 리스트에 담긴 메시지들로 프롬프트 조립하기
        Prompt.Builder promptBuilder = Prompt.builder().messages(messages);

        // 5. 프론트엔드에서 보낸 chatOptions가 있다면 적용하기
        if (promptBody.chatOptions() != null) {
            promptBuilder.chatOptions(promptBody.chatOptions());
        }

        return promptBuilder.build();
    }

    public record PromptBody(
            @Nullable String conversationId,
            @Nullable String userPrompt,
            @Nullable String systemPrompt,
            DefaultChatOptions chatOptions
    ) {}
}
