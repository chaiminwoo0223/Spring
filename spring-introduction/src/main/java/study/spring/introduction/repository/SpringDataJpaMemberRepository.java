package study.spring.introduction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring.introduction.domain.Member;

import java.util.Optional;

// Spring Data JPA는 JpaRepository를 상속하는 인터페이스에 대해 자동으로 구현체를 생성
// 이 과정에서 별도의 @Repository 어노테이션이 없어도 Spring Bean 등록
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    Optional<Member> findByName(String name);
}
