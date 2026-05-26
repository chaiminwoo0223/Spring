package com.batch.settlement.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class JobLoggerListener {
    private static final String JOB_START_LOG_FORMAT = "'{}' 배치를 시작합니다.";
    private static final String JOB_END_LOG_FORMAT = "'{}' 배치가 종료되었습니다. (상태 : {})";

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        log.info("=================================");
        log.info(JOB_START_LOG_FORMAT, jobExecution.getJobInstance().getJobName());
        log.info("=================================");
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        log.info("=================================");
        log.info(JOB_END_LOG_FORMAT, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

        if (jobExecution.getStartTime() != null && jobExecution.getEndTime() != null) {
            long duration = Duration.between(
                    jobExecution.getStartTime(),
                    jobExecution.getEndTime()
            ).toMillis();
            log.info("총 소요 시간: {} ms", duration);
        }

        log.info("=================================");

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("배치가 실패했습니다!");
        }
    }
}
