package study.spring.basic.member;

public interface MemberRepository {
    void save(Member member);

    Member findById(Long memberId);
}
