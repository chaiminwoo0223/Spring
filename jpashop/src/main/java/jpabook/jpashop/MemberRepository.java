package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

// DB와 상호 작용하는데 필요한 메서드를 정의하는 인터페이스 or 클래스
@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId(); // cmd와 query를 분리한다.(원칙)
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}