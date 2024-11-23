package study.spring.basic.order;

import study.spring.basic.discount.DiscountPolicy;
import study.spring.basic.discount.FixDiscountPolicy;
import study.spring.basic.member.Member;
import study.spring.basic.member.MemberRepository;
import study.spring.basic.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
