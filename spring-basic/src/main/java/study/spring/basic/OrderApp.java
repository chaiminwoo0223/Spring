package study.spring.basic;

import study.spring.basic.member.Grade;
import study.spring.basic.member.Member;
import study.spring.basic.member.MemberService;
import study.spring.basic.member.MemberServiceImpl;
import study.spring.basic.order.Order;
import study.spring.basic.order.OrderService;
import study.spring.basic.order.OrderServiceImpl;

public class OrderApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);
        System.out.println("order = " + order);
        System.out.println("order.calculatePrice() = " + order.calculatePrice());
    }
}
