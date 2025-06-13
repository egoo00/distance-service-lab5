package com.example.distanceservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.distanceservice.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.example.distanceservice.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method " + joinPoint.getSignature().getName() + " returned: " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.distanceservice.controller.*.*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method " + joinPoint.getSignature().getName() + ": " + exception.getMessage(), exception);
    }
}