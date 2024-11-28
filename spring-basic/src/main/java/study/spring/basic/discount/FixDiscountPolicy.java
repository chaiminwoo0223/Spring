package study.spring.basic.discount;

import org.springframework.stereotype.Component;
import study.spring.basic.member.Grade;
import study.spring.basic.member.Member;

@Component
public class FixDiscountPolicy implements DiscountPolicy {
    private int discountFixAmount = 1000; // 1000원 할인

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
