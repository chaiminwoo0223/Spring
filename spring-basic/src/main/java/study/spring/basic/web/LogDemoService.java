package study.spring.basic.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.spring.basic.common.MyLogger;

@Service
@RequiredArgsConstructor
public class LogDemoService {
    private final MyLogger myLogger;

    public void logic(String id) {
        myLogger.log("service id = " + id);
    }
}
