package com.system.batch.sy_batch_system.listener;

import com.system.batch.sy_batch_system.provider.EmailProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobReportListener {
    private final EmailProvider emailProvider;

    @BeforeJob
    public void before(JobExecution jobExecution) {
        log.info("배치를 시작합니다. (Job ID: {})", jobExecution.getJobInstanceId());
    }

    @AfterJob
    public void after(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            emailProvider.send(
                    "admin@naver.com",
                    "배치 실패 알림",
                    "Job ID " + jobExecution.getJobInstanceId() + "번이 실패했습니다."
            );
        } else {
            log.info("배치가 성공적으로 끝났습니다.");
        }
    }
}
