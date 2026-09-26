package com.jscode.tool.config;

import ch.qos.logback.classic.LoggerContext;
import com.jscode.tool.service.ToolService;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class ToolChatConfig {

    @Bean
    public SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return SimpleLoggerAdvisor.builder().build();
    }

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().maxMessages(10).build();
    }

    @Bean
    public MessageChatMemoryAdvisor messageChatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

    @ConditionalOnProperty(prefix = "app.cli", name = "enabled", havingValue = "true")
    @Bean
    // 스프링 부트 서버가 완전히 켜지기 전에 단 한 번 자동으로 실행
    public CommandLineRunner cli(@Value("${spring.application.name}") String applicationName,
                                 ToolService toolService,
                                 @Value("${app.cli.filter-expression:}") String filterExpression) {
        return _ -> {
            // 1. 스프링 기본 로그 끄기 (채팅에 방해되지 않도록)
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.getLogger("ROOT").detachAppender("CONSOLE");

            System.out.println("=======================================");
            System.out.println("🤖 [" + applicationName + "] CLI 챗봇을 시작합니다!");
            System.out.println("   (종료하려면 'exit' 또는 'quit' 입력)");
            System.out.println("=======================================");

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("\nUSER: ");
                    String userMessage = scanner.nextLine();

                    // 2. 대화 종료 조건 (무한 루프 탈출)
                    if (userMessage.equalsIgnoreCase("exit") || userMessage.equalsIgnoreCase("quit")) {
                        System.out.println("대화를 종료합니다. 안녕히 계세요!");
                        break;
                    }

                    System.out.print("ASSISTANT: ");
                    Iterable<String> chatStream = toolService.stream("cli", new Prompt(userMessage)).toIterable();

                    for (String token : chatStream) {
                        System.out.print(token);
                    }
                    System.out.println();
                }
            }
        };
    }
}

