package hello.core.web;

import hello.core.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/*
* 기본적으로 request scope Bean 은 클라이언트의 request에 따라 스프링에서 생성을 처리하도록 설계되어 있다. 그러나, LogDemoController 에서는
* Component Scan 타임(즉, 스프링 컨테이너 생성 시점에)에 곧바로 request scope Bean(MyLogger)의 생성을 처리하도록 요구하고 있으므로
* 스프링 컨테이너에서는 MyLogger 가 아직 생성되지 않았으므로, 오류가 나게된다. 이를 해결하기 위해서 ObjectProvider를 적용하면
* LogDemoController에서 Autowired 되는 시점에 MyLogger 를 ObjectProvider 에 한 뎁스 wrapping 하여 준다.
*  - ObjectProvider 덕분에 ObjectProvider.getObject() 를 호출하는 시점까지 request scope 빈의 생성을 지연할 수 있다.
*  - ObjectProvider.getObject() 를 호출하시는 시점에는 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상 처리된다.
* */

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
//    private final ObjectProvider<MyLogger> myLoggerProvider;


    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
//        MyLogger myLogger = myLoggerProvider.getObject();

        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        // myLogger = class hello.core.MyLogger$$EnhancerBySpringCGLIB$$b463731f
        System.out.println("myLogger = " + myLogger.getClass());

        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");
        return "OK";
    }
}
