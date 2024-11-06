package study.spring.introduction.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TimeTraceAop {
    @Around("execution(* study.spring.introduction.service..*(..))") // AOP 적용 대상을 서비스 레이어와 같은 특정 패키지로 제한(순환참조 방지)
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.getSignature());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.getSignature() + " " + timeMs + " ms");
        }
    }
}
