package hello.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
/*
* javax 로 시작하는 패키지명은 자바 진영에서 공식적으로 지원한다는 의미임. 즉, 스프링이 아니더라도 작동한다는 의미임.
* [ @PostConstruct, @PreDestroy 애노테이션 특징 ]
*  - 최신 스프링에서 가장 권장하는 방법이다.
*  - 애노테이션 하나만 붙이면 되므로 매우 편리하다.
*  - 패키지를 잘 보면 javax.annotation.PostConstruct 이다. 스프링에 종속적인 기술이 아니라 "JSR-250" 라는 자바 표준이다. 따라서 스프링이 아닌 다른 컨테이너에서도 동작한다.
*  - 컴포넌트 스캔과 잘 어울린다. (메서드 지정방식은 @bean에 써야하지만 그럴 필요가 없음.)
*  - 유일한 단점은 외부 라이브러리에는 적용하지 못한다는 것이다. 외부 라이브러리를 초기화, 종료해야 하면 @Bean의 기능을 사용하자.
*
* [ 정리 ]
* @PostConstruct, @PreDestroy 애노테이션을 사용하자.
* 코드를 고칠 수 없는 외부 라이브러리를 초기화, 종료해야 하면 @Bean 의 initMethod , destroyMethod 를 사용하자.
*
* */
public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url){
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect(){
        System.out.println("connect: " + url);
    }

    public void call(String message){
        System.out.println("call: " + url + ", message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect(){
        System.out.println("close: "+ url);
    }

    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
/*
* InitializingBean 은 afterPropertiesSet() 메서드로 초기화를 지원한다.
* DisposableBean 은 destroy() 메서드로 소멸을 지원한다.
*
* 출력 결과를 보면 connect(), call() 메서드가 주입 완료 후에 적절하게 호출 된 것을 확인할 수 있다.
* 그리고 스프링 컨테이너의 종료가 호출되자 소멸 메서드인 disconnect() 가 호출 된 것도 확인할 수 있다.
*
* [ 초기화(InitializingBean), 소멸(DisposableBean) 인터페이스 단점 ]
*  - 이 인터페이스는 스프링 전용 인터페이스다. 해당 코드가 스프링 전용 인터페이스에 의존한다.
*  - 초기화, 소멸 메서드의 이름을 변경할 수 없다.
*  - 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
*  - 인터페이스를 사용하는 초기화, 종료 방법은 스프링 초창기에 나온 방법들이고, 지금은 다음의 더 나은 방법들이 있어서 거의 사용하지 않는다.
* */