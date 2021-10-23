package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

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
@Configuration
@ComponentScan( // excludeFilters를 통해 component scan에서 제외시킬 항목을 추가한다. 이전에 설정해놓은 Configuration들은 제외하겠다는 의미임.
        excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

}