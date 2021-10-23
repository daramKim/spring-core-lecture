package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

//    private final MemberRepository memberRepository;
//    private final DiscountPolicy discountPolicy;


    // 3. 필드 주입
    // 이름 그대로 필드에 바로 주입하는 방법이다.
    // 특징
    // 1. 코드가 간결해서 많은 개발자들을 유횩하지만 외부에서 변경이 불가능해서 테스트하기 힘들다는 치명적인 단점이 있다.
    // 2. DI 프레임워크가 없으면 아무것도 할 수 없다.
    // 3. 사용하지마라 그냥
    //   - 순수 자바 테스트 코드에는 당연히 @AutoWired가 동작하지 않는다. (@SpringBootTest 처럼 스프링 컨테이너를 테스트에 통합한 경우에만 가능함.)
    //   - 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별하게 사용 할 수는 있음.

    @Autowired private MemberRepository memberRepository;
    @Autowired private DiscountPolicy discountPolicy;

    // @Autowired 를 사용하면 생성자에서 여러 의존관계도 한번에 주입받을 수 있다.
    // 생성자가 딱 1개만 있으면 @Autowired 를 생략해도 자동주입이 된다. (물론 스프링 빈일 경우만)

    // ㄱ. 생성자 주입 방법(의존관계 주입 방법)
    // 특징
    // 1. 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
    // 2. 불편, 필수 의존관계에 사용
    // @Autowired
    /*public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        System.out.println("memberRepository = " + memberRepository);
        System.out.println("discountPolicy = " + discountPolicy);
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }*/

    // 2. 수정자 주입(setter 주입) 방법(의존관계 주입 방법)
    // 특징
    // 1. 선택, 변경 가능성이 있는 의존관계에 사용
    // 2. 자바빈 프로퍼티 규약(게터 세터)의 수정자 메서드 방식을 사용하는 방식이다.

    // @Autowired(required = false) 로 설정하면 무조건 주입하지 않을 수 있음.(선택 가능)
    // 프로그램이 작동하면서 외부에서 setMemberRepository() 호출하여 의존관계 변경이 가능함.
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }

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
