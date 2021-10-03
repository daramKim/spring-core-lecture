package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.memoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    MemberRepository memberRepository = new memoryMemberRepository();
    DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member findMember = memberRepository.findById(memberId);
        int discountAmt = discountPolicy.discount(findMember, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountAmt);
    }
}
