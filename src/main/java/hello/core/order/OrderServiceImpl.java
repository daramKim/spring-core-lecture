package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // @Autowired 를 사용하면 생성자에서 여러 의존관계도 한번에 주입받을 수 있다.
    // 생성자가 딱 1개만 있으면 @Autowired 를 생략해도 자동주입이 된다. (물론 스프링 빈일 경우만)
    // 특징
    // 1. 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
    // 2. 불편, 필수 의존관계에 사용
    // @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        System.out.println("memberRepository = " + memberRepository);
        System.out.println("discountPolicy = " + discountPolicy);
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member findMember = memberRepository.findById(memberId);
        int discountAmt = discountPolicy.discount(findMember, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountAmt);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
