package study.spring.introduction.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.introduction.repository.JdbcMemberRepository;
import study.spring.introduction.repository.MemberRepository;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {
    private final DataSource dataSource;

    @Bean
    public MemberRepository memberRepository() {
//      return new MemoryMemberRepository();
        return new JdbcMemberRepository(dataSource);
    }
}
