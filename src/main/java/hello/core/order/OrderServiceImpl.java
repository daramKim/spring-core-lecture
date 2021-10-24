package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
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

    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    // 4. 일반 메서드 주입
    // 일반 메서드를 통해서 주입 받을 수 있다. (메서드명만 다르지 개념은 setter 주입과 같다고 보면 됌.)
    // 특징
    // 1. 한번에 여러 필드를 주입 받을 수 있다.
    // 2. 일반적으로 잘 사용 안함. (쓰지마라)

//    @Autowired
//    public void init(MemberRepository memberRepository, DiscountPolicy
//            discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }


    // [ @Autowired ]
    // ㄱ. @Autowired 를 사용하면 생성자에서 여러 의존관계도 한번에 주입받을 수 있다.
    // ㄴ. 생성자가 딱 1개만 있으면 @Autowired 를 생략해도 자동주입이 된다. (물론 스프링 빈일 경우만)
    // ㄷ. 옵션처리 가능
    //   - 주입할 스프링 빈이 없어도 동작해야 할 때가 있음.
    //   - 그런데 @AutoWired 가 있으면 자동 주입 대상이 되어서 없으면 오류가 발생한다.
    //   - default가 @Autowired(required = true)

    // ㄱ. 생성자 주입 방법(의존관계 주입 방법)
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
