package study.spring.basic.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import study.spring.basic.member.Grade;
import study.spring.basic.member.Member;
import study.spring.basic.member.MemberService;
import study.spring.basic.member.MemberServiceImpl;

public class OrderServiceTest {
    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

    @Test
    void createOrder() {
        // given
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        // when
        Order order = orderService.createOrder(memberId, "itemA", 10000);

        // then
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(10000);
    }
}
