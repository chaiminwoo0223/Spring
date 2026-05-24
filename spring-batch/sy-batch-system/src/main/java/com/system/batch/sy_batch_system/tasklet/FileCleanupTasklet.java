package com.system.batch.sy_batch_system.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

import java.io.File;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class FileCleanupTasklet implements Tasklet {
    private final String rootPath;
    private final int retentionDays;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate cutoffDate = LocalDate.now().minusDays(retentionDays);
        File folder = new File(rootPath);
        File[] files = folder.listFiles();

        if (files == null) return RepeatStatus.FINISHED;

        for (File file : files) {
            String name = file.getName();

            // 1. 로그 파일 형식인지 간단히 체크 (예: .log 포함 여부)
            if (name.endsWith(".log") && name.length() >= 10) {
                // 2. 파일명에서 날짜 부분만 추출
                try {
                    String dateStr = name.substring(name.lastIndexOf('_') + 1, name.lastIndexOf('.')); // access_2026-01-26.log -> "2026-01-26"
                    LocalDate fileDate = LocalDate.parse(dateStr); // "2026-01-26" -> LocalDate

                    if (fileDate.isBefore(cutoffDate)) {
                        if (file.delete()) {
                            log.info("삭제된 로그: {}", name);
                        } else {
                            log.warn("로그 삭제 실패: {}", name);
                        }
                    }
                } catch (Exception e) {
                    log.warn("날짜 파싱 실패, 스킵: {}", name);
                }
            }
        }
        return RepeatStatus.FINISHED;
    }
}
