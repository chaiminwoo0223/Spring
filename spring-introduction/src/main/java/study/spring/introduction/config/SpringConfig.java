package study.spring.introduction.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.introduction.repository.JpaMemeberRepository;
import study.spring.introduction.repository.MemberRepository;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {
    private final EntityManager em;

    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcMemberRepository(dataSource);
        return new JpaMemeberRepository(em);
    }
}
