package com.system.batch.sy_batch_system.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CafeJobTasklet implements Tasklet {
    private int cakeCount = 0;
    private final int ORDER_TARGET = 10;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        cakeCount++;
        log.info("케이크를 만들고 있습니다. ({}/{})", cakeCount, ORDER_TARGET);

        if (cakeCount >= ORDER_TARGET) {
            log.info("목표한 모든 케이크를 만들었습니다. 가게를 오픈하세요!");
            cakeCount = 0; // 재실행시 카운트 초기화
            return RepeatStatus.FINISHED; // 작업 완료
        }

        return RepeatStatus.CONTINUABLE; // 계속 작업
    }
}
