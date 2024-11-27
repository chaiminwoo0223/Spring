package study.spring.basic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.spring.basic.discount.DiscountPolicy;
import study.spring.basic.discount.RateDiscountPolicy;
import study.spring.basic.member.MemberRepository;
import study.spring.basic.member.MemberService;
import study.spring.basic.member.MemberServiceImpl;
import study.spring.basic.member.MemoryMemberRepository;
import study.spring.basic.order.OrderService;
import study.spring.basic.order.OrderServiceImpl;

@Configuration
public class AppConfig {
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
