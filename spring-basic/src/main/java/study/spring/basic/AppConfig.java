package study.spring.basic;

import study.spring.basic.discount.FixDiscountPolicy;
import study.spring.basic.member.MemberService;
import study.spring.basic.member.MemberServiceImpl;
import study.spring.basic.member.MemoryMemberRepository;
import study.spring.basic.order.OrderService;
import study.spring.basic.order.OrderServiceImpl;

public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository()); // 의존관계 주입(생성자 주입)
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy()); // 의존관계 주입(생성자 주입)
    }
}
