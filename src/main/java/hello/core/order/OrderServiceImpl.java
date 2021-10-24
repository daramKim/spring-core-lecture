package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component


// 롬복 라이브러리가 제공하는 @RequiredArgsConstructor 기능을 사용하면 final이 붙은 필드를 모아서
// 생성자를 자동으로 만들어준다. (코드에는 보이지 않지만 실제 호출 가능하다. Structure Navigator로 확인 가능.)

/*
    [정리]
    최근에는 생성자를 딱 1개 두고, @Autowired 를 생략하는 방법을 주로 사용한다. 여기에 Lombok
    라이브러리의 @RequiredArgsConstructor 함께 사용하면 기능은 다 제공하면서, 코드는 깔끔하게
    사용할 수 있다.
*/
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;


    // 3. 필드 주입
    // 이름 그대로 필드에 바로 주입하는 방법이다.
    // 특징
    // 1. 코드가 간결해서 많은 개발자들을 유횩하지만 외부에서 변경이 불가능해서 테스트하기 힘들다는 치명적인 단점이 있다.
    // 2. DI 프레임워크가 없으면 아무것도 할 수 없다.
    // 3. 사용하지마라 그냥
    //   - 순수 자바 테스트 코드에는 당연히 @AutoWired가 동작하지 않는다. (@SpringBootTest 처럼 스프링 컨테이너를 테스트에 통합한 경우에만 가능함.)
    //   - 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별하게 사용 할 수는 있음.

//    private MemberRepository memberRepository;
//    private DiscountPolicy discountPolicy;

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
    // ㄹ. 컴포넌트 스캔은 기본적으로 타입(DiscountPolicy 과 같은)으로 조회되는데, 타입에 대한 구현체(@Component 어노테이션 매겨놓아서 스프링 Bean으로 등록된 것들 예를 들면 FixDiscountPolicy, RateDiscountPolicy..)
    //     들이 2개 이상이 조회되는 경우(중복 조회시) 구체적으로 지정하고 싶은 bean을 파라미터명으로 지정하여 선택가능(@Autowired 의 기능)
    //      - 첫글자는 소문자로 변경 해야함

    // ㄱ. 생성자 주입 방법(의존관계 주입 방법)
    // 특징
    // 1. 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
    // 2. 불편, 필수 의존관계에 사용
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
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


/*
* 결론.
*
* 생성자 주입을 사용해라.
* 과거에는 수정자 주입과 필드 주입을 많이 사용했지만, 최근에는 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장한다. 그 이유는 아래와 같다.
*
* [불변]
*   ㄱ. 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없다. 오히려 대부분의 의존관계는 애플리케이션 종료 전까지 변하면 안된다.(불변해야 한다.)
*   ㄴ. 수정자 주입을 사용하면, setXxx 메서드를 public으로 열어두어야 한다.
*   ㄷ. 누군가 실수로 변경할 수 도 있고, 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아니다.
*   ㄹ. 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 따라서 불변하게 설계할 수 있다.
*
* [누락]
*   ㄱ. 프레임워크 없이 순수한 자바코드를 단위 테스트 하는 경우(아래 코드처럼)에 OrderServiceImpl 클래스에 [필드 주입 또는 세터 주입]을 사용한 경우에
*      실행이 그냥 되고, "NPE"가 발생한다. → 돌려보기 전까지 오류를 알 수 없다.
*      그러나, [생성자 주입]을 사용한 경우엔 데이터를 누락했을 때 "컴파일 오류"가 발생한다. 그리고 IDE에서 바로 어떤 값을 필수로 주입해야 하는지 알 수 있다.
*
*     @Test
      void createOrder() {
       OrderServiceImpl orderService = new OrderServiceImpl();
       orderService.createOrder(1L, "itemA", 10000);
      }

*   ㄴ. final 키워드 사용
*        - 생성자 주입을 사용하면 필드에 "final" 키워드를 사용할 수 있다. 그래서 생성자에 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다.
*        - 필드 주입, 세터 주입 방식은 모두 생성자 이후에 호출되므로, 필드에 final 키워드를 사용할 수 없다. 오직 생성자 주입 방식만 final 키워드를 사용할 수 있다.
*
* [정리]
*   ㄱ. 생성자 주입 방식을 선택하는 이유는 여러가지가 있지만, 프레임워크에 의존하지 않고, 순수한 자바 언어의 특징을 잘 살리는 방법이기도 하다.
*   ㄴ. 기본으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 세터 주입 방식을 옵션으로 부여하면 된다. 생성자 주입과 세터 주입을 섞어서 사용할 수 있다.
*   ㄷ. 항상 생성자 주입을 선택해라. 가끔 옵션이 필요하면 세터 주입을 선택해라. 필드 주입을 사용하지 않는게 좋다.
*
* */
