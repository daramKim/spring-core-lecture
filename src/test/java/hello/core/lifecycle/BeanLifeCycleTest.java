package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/*
* [스프링 빈의 이벤트 라이프사이클]
* 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 빈 초기화 콜백 -> 빈 사용 -> 빈 소멸전 콜백 -> 스프링 종료
*
* 스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공한다.
* 또한, 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 준다. 따라서 안전하게 종료 작업을 진행할 수 있다
*
*
* 스프링은 크게 3가지 방법으로 [빈 생명주기 콜백]을 지원한다.
* 1. 인터페이스(InitializingBean, DisposableBean)
* 2. 설정 정보에 초기화 메서드, 종료 메서드 지정
* 3. @PostConstruct, @PreDestroy 애노테이션 지원
* */
public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest(){
        // ApplicationContext 에는 close() 가 없다. 하위로 가면서 구체화되어 정의되어 있음.
        // 상속 구조도..
        // AnnotationConfigApplicationContext -> ConfigurableApplicationContext -> ApplicationContext
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {

        @Bean // Bean 수동설정
        public NetworkClient networkClient(){ // 인터페이스 없이 바로 구현체만 정의했음.
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }



}
