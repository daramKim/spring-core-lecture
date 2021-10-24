package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {



    // @Autowired 옵션처리
    //  - @Autowired(required = false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됌
    //  - @Nullable(org.springframework.lang.Nullable) : 자동 주입할 대상이 없으면 "null"이 입력된다.
    //  - Optional(java.util.Optional) : 자동 주입할 대상이 없으면 "Optional.empty"가 입력된다.


    @Test
    void AutowiredOption(){

        // 값이 안들어오는것만 테스트
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
        // ac.getBean()
    }

    private static class TestBean {
        /* 스프링 bean 이 아닌 것을 Autowired 하면 당연히 주입이 안됌.

           Member 는 bean 이 아님.
           [에러메시지]
            - Unsatisfied dependency expressed through method 'setNoBean1' parameter 0
           @Autowired 기본 옵션이 required = true 이기 때문에 에러가 난다.

           1. required = false 셋팅을 하면 에러나지 않음. 그러나, 실행자체가 안됌.
           2. @Nullable 셋팅을 하면 실행은 되나, null로 들어옴.
           3. Optional<> 셋팅을 하면 실행은 되나, Optional.empty로 들어옴.

           💡 @Nullable, Optional은 스프링 모든 곳에서 적용가능하다.
               예를 들면 생성자 자동 주입에서 "특정 필드"에만 사용해도 된다.
        */

        @Autowired(required = false)
        public void setNoBean1(Member noBean1){
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired
        public void setNoBean2(@Nullable Member noBean1){
            System.out.println("noBean2 = " + noBean1);
        }

        @Autowired
        public void setNoBean3(Optional<Member> noBean1){
            System.out.println("noBean3 = " + noBean1);
        }
    }
}
