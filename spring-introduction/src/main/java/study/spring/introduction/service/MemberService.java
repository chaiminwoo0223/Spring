package study.spring.introduction.service;

import study.spring.introduction.domain.Member;
import study.spring.introduction.repository.MemberRepository;
import study.spring.introduction.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    
    // 회원가입
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        return memberRepository.save(member).getId();
    }

    // 전체 회원 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 존재하는 회원입니다.");
                });
    }
}
