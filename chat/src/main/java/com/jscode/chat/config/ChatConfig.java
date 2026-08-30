package com.jscode.chat.config;

import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Optional;

@Configuration
public class ChatConfig {

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
    public DocumentPostProcessor printDocumentsPostProcessor() {
        return (_, documents) -> {
            System.out.println("\n[ Search Results ]");
            System.out.println("===============================================");

            if (documents == null || documents.isEmpty()) {
                System.out.println("  No search results found.");
                System.out.println("===============================================");
                return documents;
            }

            for (int i = 0; i < documents.size(); i++) {
                Document document = documents.get(i);
                System.out.printf("▶ %d Document, Score: %.2f%n", i + 1, document.getScore());
                System.out.println("-----------------------------------------------");
                Optional.ofNullable(document.getText()).stream()
                        .map(text -> text.split("\n")).flatMap(Arrays::stream)
                        .forEach(line -> System.out.printf("%s%n", line));
                System.out.println("===============================================");
            }
            System.out.print("\n[ RAG 사용 응답 ]\n\n");
            return documents;
        };
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder,
            @Autowired(required = false) DocumentPostProcessor documentPostProcessor) {

        // 1. 문서 검색기 도구 설정
        VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.3)
                .topK(3)
                .build();

        // 2. 다른 구성 요소들 생성
        ContextualQueryAugmenter queryAugmenter = ContextualQueryAugmenter.builder()
                .allowEmptyContext(true)
                .build();

        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();

        TranslationQueryTransformer queryTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .targetLanguage("korean")
                .build();

        // 3. 최종 합체 (RetrievalAugmentationAdvisor 빌더에서 후처리기 등록)
        RetrievalAugmentationAdvisor.Builder builder = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(queryAugmenter)
                .queryExpander(queryExpander)
                .queryTransformers(queryTransformer);

        // 후처리기가 존재한다면 advisor 빌더에 추가
        if (documentPostProcessor != null) {
            builder.documentPostProcessors(documentPostProcessor);
        }

        return builder.build();
    }

    @Bean
    public Rest5Client rest5Client() {
        return Rest5Client.builder(new HttpHost("http", "localhost", 9200)).build();
    }
}
