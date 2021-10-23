package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

// 컴포넌트 스캔
/*
* 스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 "컴포넌트 스캔"이라는 기능을 제공한다.
* 컴포넌트 스캔을 사용하려면 Config(@Configuration 어노테이션 붙은) 파일에 @ComponentScan을 붙여주면 된다.
*
* @ComponentScan 은 @Component가 붙은 모든 클래스를 스프링 빈으로 등록한다.
* 이때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.
*  - 빈 이름 기본 전략: MemberServiceImpl → memberServiceImpl
*  - 빈 이름 직접 지정: 만약 스프링 빈의 이름을 직접 지정하고 싶으면 @Compoent("memberServiceRename") 이런식으로 이름을 부여하면 된다.
*
* 생성자에 @Autowired 를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다.
* 이때 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다.
*  - getBean(MemberRepository.class)와 동일하다고 이해하면 된다.
* 생성자에 파라미터가 많아도 다 찾아서 자동으로 주입한다
* */

// 컴포넌트 스캔의 탐색 위치
/*
* 우리는 컴포넌트 스캔의 대상을 지정해야 한다. 그렇지 않으면 모든 자바 클래스의 컴포넌트를 스캔할 것이다. 문법은 아래와 같다.
*  - basePackages : 탐색할 패키지의 시작 위치를 지정한다. 이 패키지를 포함해서 하위 패키지를 모두 탐색한다.
* @ComponentScan(
    basePackages = "hello.core"
  }
* 이런 형태로 지정이 가능하다.
* basePackages = {"hello.core", "hello.service"} 이렇게 여러 시작 위치를 지정할 수도 있다.
* basePackageClasses : 지정한 클래스의 패키지를 탐색 시작 위치로 지정한다.
* 만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다
* - 지금 AutoAppConfig에 @ComponentScan 이 붙었으니까. 이 클래스의 패키지인 'hello.core'의 하위 패키지를 모두 탐색한다.
*
* [ 권장하는 방법 ]
* 가장 많이 사용하는 방법은 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것이다.
* 최근 스프링 부트도 이 방법을 기본으로 제공한다.
*
* 예를 들어, 프로젝트 구조가 다음과 같으면
*
* com.hello
  com.hello.serivce
  com.hello.repository
*
* "com.hello"이 프로젝트 시작 루트이고, 여기에 AppConfig 같은 메인설정 정보를 두고
* @ComponentScan 애노테이션을 붙인뒤 , basePackages 지정은 생략한다.
* 이렇게 하면 com.hello 를 포함한 하위는 모두 자동으로 컴포넌트 스캔의 대상이 된다.
*
* 프로젝트 메인설정 정보는 프로젝트를 대표하는 정보이기 때문에 프로젝트 루트 위치에 두는 것이 좋다.
* 참고로 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 @SpringBootApplication 를 이 프로젝트 시작 루트 위치에 두는 것이 관례이다.
* (그리고 이 @SpringBootApplication 안에 바로 @ComponentScan 이 포함되어 있다. )
*
* */

// 컴포넌트 스캔의 기본스캔 대상
/*
* 컴포넌트 스캔은 @Component 뿐만 아니라 다음과 내용도 추가로 대상에 포함한다
* @Component : 컴포넌트 스캔에서 사용
  @Controlller : 스프링 MVC 컨트롤러에서 사용
   - 스프링 MVC 컨트롤러로 인식
  @Service : 스프링 비즈니스 로직에서 사용
   - @Service 는 특별한 처리를 하지 않는다. 대신 개발자들이 핵심 비즈니스 로직이 여기에 있겠구나 라고 비즈니스 계층을 인식하는데 도움이 된다.
  @Repository : 스프링 데이터 접근 계층에서 사용
   - 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환해준다.
  @Configuration : 스프링 설정 정보에서 사용
*
* 위의 항목들의 구현정보를 찾아 따라가보면 @Compoent 를 포함하고 있는 것을 알 수 있다.
*
* 💡 참고: 사실 애노테이션에는 상속관계라는 것이 없다. 그래서 애노테이션이 특정 애노테이션을 포함하고 있는 것을 인식할 수 있는 것은
*         자바 언어가 지원하는 기능이 아니고, "스프링"에서 지원하는 기능이다.
*
*
* */

@Configuration
@ComponentScan( // excludeFilters를 통해 component scan에서 제외시킬 항목을 추가한다. 이전에 설정해놓은 Configuration들은 제외하겠다는 의미임.
        excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

}