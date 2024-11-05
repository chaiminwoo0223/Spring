package study.spring.introduction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 컴포넌트 스캔(@ComponentScan)
public class SpringIntroductionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntroductionApplication.class, args);
    }

}
