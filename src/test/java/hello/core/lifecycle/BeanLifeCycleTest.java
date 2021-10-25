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

        /*
        * 설정 정보에 @Bean(initMethod = "init", destroyMethod = "close") 처럼 초기화, 소멸 메서드를 지정할 수 있다.
        * [ 특징 ]
        * ㄱ. 메서드 이름을 자유롭게 줄 수 있다.
        * ㄴ. 스프링 빈이 스프링 코드에 의존하지 않는다.
        * ㄷ. 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.
        *
        * [ destroyMethod() 메서드의 추론 기능 ]
        * @Bean의 destroyMethod 속성에는 아주 특별한 기능이 있다.
        * 대부분의 라이브러리는 close() , shutdown() 이라는 이름의 종료 메서드를 사용한다.
        * @Bean의 destroyMethod 는 기본값이 "(inferred)" (추론)으로 등록되어 있다.
        * 이 추론 기능은 close , shutdown 라는 이름의 메서드를 자동으로 호출해준다. 이름 그대로 종료 메서드를 추론해서 호출해준다.
        * 따라서 직접 스프링 빈으로 등록하면 종료 메서드는 따로 적어주지 않아도 잘 동작한다.
        * 추론 기능을 사용하기 싫으면 destroyMethod="" 처럼 빈 공백을 지정하면 된다.
        * */
        @Bean(initMethod = "init", destroyMethod = "close")
        public NetworkClient networkClient(){ // 인터페이스 없이 바로 구현체만 정의했음.
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
