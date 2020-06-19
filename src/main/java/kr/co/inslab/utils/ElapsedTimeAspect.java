package kr.co.inslab.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
public class ElapsedTimeAspect {
    private final Logger logger = LoggerFactory.getLogger(ElapsedTimeAspect.class);

    @Around("execution(* kr.co.inslab..*.*(..))")
    public Object printElapsedTime(final ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();

        Object obj = joinPoint.proceed();

        long elapsedTime = System.currentTimeMillis() - start;
        logger.debug("Elapsed Time: "+elapsedTime+" millis"+", Class:"+typeName+", Method:"+name);


        return obj;
    }

}

