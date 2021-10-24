package hello.core.autowired;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AllBeanTest {

    @Test
    void findAllBean(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class ,DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        int rateDiscountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");
        assertThat(rateDiscountPrice).isEqualTo(2000);
    }

    private static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        // @Autowired
        private DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, price);
        }
    }
}

/*
* 스프링 빈 자동등록 vs 수동등록
*
* 스프링이 나오고 시간이 갈수록 점점 자동을 선호하는 추세라고 한다.
*
* [업무로직 빈]: 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는
* 리포지토리등이 모두 업무 로직이다. 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경된다.
* [기술지원 빈]: 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용된다. 데이터베이스 연결이나,
* 공통 로그 처리 처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들이다.
*
* 기술지원 로직은 업무 로직과 비교해서 그 수가 매우 적고 애플리케이션에 광범위하게 영향을 미치기 때문에
* 기술지원 객체는 수동 빈으로 등록해서 딱! 설정 정보에 바로 나타나게 하는 것이 유지보수 하기 좋다.
*
* 그에 반해, 업무로직 객체는 숫자도 매우 많고, 한번 개발해야 하면 컨트롤러, 서비스, 리포지토리 처럼 어느정도 유사한 패턴이 있다.
* 이런 경우 자동 기능을 적극 사용하는 것이 좋다. 보통 문제가 발생해도 어떤 곳에서 문제가 발생했는지 명확하게 파악하기 쉽기 때문이다.
*
* 💡 업무로직(비즈니스 로직) 중에 다형성이 필요한 경우(위의 테스트코드 처럼 전략패턴을 구현하는 등의 이유로)가 있다.
*    이런 경우 수동 빈으로 등록하여 명시적으로 설정정보를 표기해주는 것이 좋다. 그렇게 하지 않고 자동등록을 이용하면 특정 패키지에 같이 묶어두는게 좋다!
*    핵심은 딱 보고 이해가 되어야 한다는 것이다! ✅
*
*
* [정리]
* ㄱ. 편리한 자동 기능을 기본으로 사용하자
* ㄴ. 직접 등록하는 기술 지원 객체는 수동 등록
* ㄷ. 다형성을 적극 활용하는 비즈니스 로직은 수동 등록을 고민해보자
*
* */