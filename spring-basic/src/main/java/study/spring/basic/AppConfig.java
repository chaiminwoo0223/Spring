package study.spring.basic;

import study.spring.basic.discount.FixDiscountPolicy;
import study.spring.basic.member.MemberService;
import study.spring.basic.member.MemberServiceImpl;
import study.spring.basic.member.MemoryMemberRepository;
import study.spring.basic.order.OrderService;
import study.spring.basic.order.OrderServiceImpl;

public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    private static MemoryMemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    private static FixDiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }
}
