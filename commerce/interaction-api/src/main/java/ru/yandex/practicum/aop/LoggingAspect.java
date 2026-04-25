package ru.yandex.practicum.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(ru.yandex.practicum.aop.Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        log.info("Entering method : {}", signature);
        log.info("Request parameters: {}", joinPoint.getArgs());

        Object result = joinPoint.proceed();

        log.info("Exiting method: {} - Response: {}", signature, result);

        return result;
    }
}
