package com.system.batch.sy_batch_system.config;

import com.system.batch.sy_batch_system.listener.MyAnnotationListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ListenerExampleConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MyAnnotationListener myListener; // 리스너 주입

    @Bean
    public Job listenerJob() {
        return new JobBuilder("listenerJob", jobRepository)
                .start(listenerStep())
                .listener(myListener)
                .build();
    }

    @Bean
    public Step listenerStep() {
        return new StepBuilder("listenerStep", jobRepository)
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
                .build();
    }
}
