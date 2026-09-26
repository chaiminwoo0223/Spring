package com.jscode.tool.controller;

import com.jscode.tool.service.ToolService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.DefaultChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tool")
public class ToolController {
    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    public record PromptBody(
            @NotEmpty @Schema(description = "대화 식별자", example = "jscode-1234") String conversationId,
            @NotEmpty @Schema(description = "사용자 입력 프롬프트", example = "안녕하세요, 제주도 날씨 어때요?") String userPrompt,
            @Nullable @Schema(description = "시스템 프롬프트(선택)", example = "You are a helpful assistant.") String systemPrompt,
            @Nullable @Schema(description = "채팅 옵션(선택)", implementation = DefaultChatOptions.class) DefaultChatOptions chatOptions
    ) {}

    @PostMapping(value = "/call", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatResponse call(@RequestBody @Valid PromptBody promptBody) {
        Prompt prompt = createPrompt(promptBody);
        return toolService.call(promptBody.conversationId, prompt);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestBody @Valid PromptBody promptBody) {
        Prompt prompt = createPrompt(promptBody);
        return toolService.stream(promptBody.conversationId, prompt);
    }

    private static Prompt createPrompt(PromptBody promptBody) {
        List<Message> messages = new ArrayList<>();

        if(promptBody.systemPrompt() != null && !promptBody.systemPrompt().isBlank()) {
            messages.add(new SystemMessage(promptBody.systemPrompt()));
        }
        messages.add(new UserMessage(promptBody.userPrompt()));

        Prompt.Builder promptBuilder = Prompt.builder().messages(messages);

        if(promptBody.chatOptions() != null) {
            promptBuilder.chatOptions(promptBody.chatOptions());
        }
        return promptBuilder.build();
    }
}
