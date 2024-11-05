package study.spring.introduction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.spring.introduction.domain.Member;
import study.spring.introduction.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service // Spring Container에 Spring Bean 등록(@Component)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    
    // 회원가입
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        return memberRepository.save(member).getId();
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
