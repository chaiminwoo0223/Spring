package com.system.batch.sy_batch_system.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyAnnotationListener {
    @BeforeJob
    public void announceStart(JobExecution jobExecution) {
        log.info("[Job 시작] id: {}", jobExecution.getJobInstanceId());
    }

    @AfterJob
    public void announceEnd(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.warn("[Job 실패] 비상! 관리자에게 연락하세요.");
        } else {
            log.info("[Job 종료] 정상적으로 배치가 수행되었습니다.");
        }
    }
}
