package study.spring.introduction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.introduction.aop.TimeTraceAop;

@Configuration
public class SpringConfig {
    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }
}
