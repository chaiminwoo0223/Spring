package com.batch.settlement.config;

import com.batch.settlement.domain.Order;
import com.batch.settlement.domain.Settlement;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SettlementJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job settlementJob() {
        return new JobBuilder("settlementJob", jobRepository)
                .start(settlementStep())
                .build();
    }

    @Bean
    public Step settlementStep() {
        return new StepBuilder("settlementStep", jobRepository)
                .<Order, Settlement>chunk(1000)
                .transactionManager(transactionManager)
                .reader(orderReader(null))
                .processor(settlementProcessor())
                .writer(settlementWriter())
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Order> orderReader(@Value("#{jobParameters['targetDate']}") String targetDate) {
        log.info("[Reader] 정산 집계 대상 날짜: {}", targetDate);

        return new JpaPagingItemReaderBuilder<Order>()
                .name("orderReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(1000)
                .queryString("SELECT o FROM `Order` o WHERE o.orderDate = :targetDate ORDER BY o.id")
                .parameterValues(Collections.singletonMap("targetDate", LocalDate.parse(targetDate)))
                .build();
    }

    @Bean
    public ItemProcessor<Order, Settlement> settlementProcessor() {
        return item -> {
            int fee = (int) (item.getAmount() * 0.03); // 수수료 3% 계산
            int settleAmount = item.getAmount() - fee; // 정산 금액 계산

            return new Settlement(item.getId(), item.getStoreName(), settleAmount, LocalDate.now());
        };
    }

    @Bean
    public ItemWriter<Settlement> settlementWriter() {
        return new JpaItemWriterBuilder<Settlement>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
