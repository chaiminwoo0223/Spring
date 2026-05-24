package com.system.batch.sy_batch_system.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailProvider {
    public void send(String to, String subject, String message) {
        // 실제로는 여기서 SMTP로 메일 발송
        log.info("[메일 발송 성공] 받는사람: {}", to);
        log.info("제목: {}", subject);
        log.info("내용: {}", message);
    }
}
