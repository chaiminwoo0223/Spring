package study.spring.basic.order;

import org.junit.jupiter.api.Test;
import study.spring.basic.discount.FixDiscountPolicy;
import study.spring.basic.member.Grade;
import study.spring.basic.member.Member;
import study.spring.basic.member.MemoryMemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceImplTest {
    @Test
    void createOrder() {
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "name", Grade.VIP));
        OrderServiceImpl orderService = new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);

        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}