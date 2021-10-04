package hello.core.beanfind;

import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextSameBeanFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);


    @Test
    @DisplayName("타입으로 조회시 같은타입이 둘 이상 있으면 중복에러가 발생한다.")
    void findByBeanDupl(){
        // MemoryMemberRepository bean = ac.getBean(MemoryMemberRepository.class);
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(MemoryMemberRepository.class));
    }

    @Test
    @DisplayName("타입으로 조회시 같은타입이 둘 이상 있으면, 빈 이름을 지정하면 된다.")
    void findByBeanDupl_nameChoice(){
         MemoryMemberRepository bean = ac.getBean("memberRepository1",MemoryMemberRepository.class);
            assertThat(bean).isInstanceOf(MemberRepository.class);
            assertThat(bean).isInstanceOf(MemoryMemberRepository.class);
    }

    @Test
    @DisplayName("같은 타입 빈 모두 검색하기")
    void findAllSameTypeBean(){
        Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " || name = " + beansOfType.get(key));
        }

        System.out.println("beansOfType = " + beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);

    }


    static class SameBeanConfig {

        @Bean
        public MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }
}

