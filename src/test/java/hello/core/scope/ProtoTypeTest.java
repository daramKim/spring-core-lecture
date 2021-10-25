package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ProtoTypeTest {

    @Test
    void  protoTypeBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ProtoTypenBean.class);
        System.out.println("find prototypeBean1");
        ProtoTypenBean bean1 = ac.getBean(ProtoTypenBean.class);
        System.out.println("find prototypeBean2");
        ProtoTypenBean bean2 = ac.getBean(ProtoTypenBean.class);
        System.out.println("bean1 = " + bean1);
        System.out.println("bean2 = " + bean2);
        Assertions.assertThat(bean1).isNotSameAs(bean2);

        ac.close();
    }

    @Scope("prototype")
    static class ProtoTypenBean {

        @PostConstruct
        public void init(){
            System.out.println("ProtoTypenBean.init");
        }

        @PreDestroy
        public void destory(){
            System.out.println("ProtoTypenBean.destory");
        }
    }
}
/*
* 싱글톤 빈은 스프링 컨테이너 생성 시점에 초기화 메서드가 실행 되지만, 프로토타입 스코프의 빈은 스프링 컨테이너에서 빈을 조회할 때 생성되고, 초기화 메서드도 실행된다.
* 프로토타입 빈을 2번 조회했으므로 완전히 다른 스프링 빈이 생성되고, 초기화도 2번 실행된 것을 확인할 수 있다.
* 싱글톤 빈은 스프링 컨테이너가 관리하기 때문에 스프링 컨테이너가 종료될 때 빈의 종료 메서드가 실행되지만,
* 프로토타입 빈은 스프링 컨테이너가 생성과 의존관계 주입 그리고 초기화 까지만 관여하고, 더는 관리하지 않는다.
* 따라서 프로토타입 빈은 스프링 컨테이너가 종료될 때 @PreDestroy 같은 종료 메서드가 전혀 실행되지 않는다.
*
* [ 프로토타입 빈의 특징 정리 ]
* 스프링 컨테이너에 요청할 때 마다 새로 생성된다. 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입 그리고 초기화까지만 관여한다.
* 종료 메서드가 호출되지 않는다. 그래서 프로토타입 빈은 프로토타입드 빈을 조회한 클라이언트가 관리해야 한다. 종료 메서에 대한 호출도 클라이언트가 직접 해야한다.
*
*
* */