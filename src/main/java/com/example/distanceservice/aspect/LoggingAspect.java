package com.example.distanceservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
    private static final String CONTROLLER_PACKAGE = "com.example.distanceservice.controller.*.*(..)";

    @Before("execution(* " + CONTROLLER_PACKAGE + ")")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Entering method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* " + CONTROLLER_PACKAGE + ")", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.info("Method " + joinPoint.getSignature().getName() + " returned: " + result);
    }

    @AfterThrowing(pointcut = "execution(* " + CONTROLLER_PACKAGE + ")", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        LOGGER.error("Exception in method " + joinPoint.getSignature().getName() + ": " + exception.getMessage(), exception);
    }
}
