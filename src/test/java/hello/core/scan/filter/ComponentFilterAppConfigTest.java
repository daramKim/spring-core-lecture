package hello.core.scan.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;


/*
* FilterType은 5가지 옵션이 있다.
*  - ANNOTATION: 기본값, 애노테이션을 인식해서 동작한다.
*     ex) org.example.SomeAnnotation
*  - ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식해서 동작한다.(클래스를 지정)
*     ex) org.example.SomeClass
*  - ASPECTJ: AspectJ 패턴 사용
*     ex) org.example..*Service+
*  - REGEX: 정규 표현식
*     ex) org\.example\.Default.*
*  - CUSTOM: TypeFilter 이라는 인터페이스를 구현해서 처리
*     ex) org.example.MyTypeFilter
*
* 참고: @Component 를 사용하면 충분하기 때문에, includeFilters 를 사용할 일은 거의 없다.
* excludeFilters는 여러가지 이유로 간혹 사용할 때가 있지만 많지는 않다. 특히 최근 스프링부트는 컴포넌트 스캔을 기본으로 제공하는데,
* 옵션을 변경하면서 사용하기 보다는 스프링의 기본 설정에 최대한 맞추어 사용하는 것이 권장사항이다.
* */
public class ComponentFilterAppConfigTest {

    @Test
    void filterScan(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);

        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isInstanceOf(BeanA.class);
        assertThat(beanA).isNotNull();

        Assertions.assertThrows(
                NoSuchBeanDefinitionException.class,
                (() -> ac.getBean("beanB", BeanB.class))
        );

    }


    @Configuration
    @ComponentScan(
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
    static class ComponentFilterAppConfig {
    }
}
