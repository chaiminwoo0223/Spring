package study.spring.introduction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.introduction.aop.TimeTraceAop;

@Configuration
public class SpringConfig {
    @Bean // Spring Container에 Spring Bean 등록(자바 코드)
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }
}
