package hello.core.discount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.stereotype.Component;

@Component
//@Qualifier("mainDiscountPolicy")
//@Primary
// 커스텀 어노테이션 활용
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountRate = 10;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return price * discountRate / 100 ;
        }else{
            return 0;
        }
    }
}

/*
* [ @Qualifier 와 @Primary 정리 ]
*
* [ @Qualifier 작동원리 ]
* 1. @Qualifier끼리 매칭
* 2. 빈 이름 매칭
* 3. NoSuchBeanDefinitionException 예외 발
*
* [ @Primary 작동원리 ]
* 1. 그냥 @Component 붙은 곳에 @Primary 만 붙여주면 끝. 해당 빈을 우선적으로 찾음.
*
* [ @Qualifier와 @Primary 중 뭐를 써야하나? ]
* @Qualifier 의 단점은 주입 받을 때 다음과 같이 모든 코드에 @Qualifier 를 붙여주어야 한다는 점이다.
* 반면에 @Primary 를 사용하면 이렇게 @Qualifier 를 붙일 필요가 없다.
*
* [ @Primary, @Qualifier 활용 ]
* 코드에서 자주 사용하는 메인 데이터베이스의 커넥션을 획득하는 스프링 빈이 있고, 코드에서 특별한
* 기능으로 가끔 사용하는 서브 데이터베이스의 커넥션을 획득하는 스프링 빈이 있다고 생각해보자. 메인
* 데이터베이스의 커넥션을 획득하는 스프링 빈은 @Primary 를 적용해서 조회하는 곳에서 @Qualifier
* 지정 없이 편리하게 조회하고, 서브 데이터베이스 커넥션 빈을 획득할 때는 @Qualifier 를 지정해서
* 명시적으로 획득 하는 방식으로 사용하면 코드를 깔끔하게 유지할 수 있다. 물론 이때 메인 데이터베이스의
* 스프링 빈을 등록할 때 @Qualifier 를 지정해주는 것은 상관없다.
*
* [ @Primary, @Qualifier 우선순위 ]
* @Primary 는 기본값 처럼 동작하는 것이고, @Qualifier 는 매우 상세하게 동작한다. 이런 경우 어떤 것이
* 우선권을 가져갈까? 스프링은 자동보다는 수동이, 넒은 범위의 선택권 보다는 좁은 범위의 선택권이 우선
* 순위가 높다. 따라서 여기서도 @Qualifier 가 우선권이 높다.
*
* */
