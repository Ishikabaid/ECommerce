package com.sistore.productservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class Logging {

    @Before("execution(public org.springframework.http.ResponseEntity com.sistore.productservice.controller.ProductController.saveProduct(..))")
    public void logProductRequest(){
        System.out.println("Logging before save Product method----------------------------");
    }

    @Around("@within(org.springframework.stereotype.Service)")
    public Object logMethodCompletion(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Method Execution started successfully---------" + LocalDateTime.now() + " ,Executing method: {}",joinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            log.error("Exception occurred: " + ex.getMessage());
            throw ex;
        }
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("Method {} executed in {} ms", joinPoint.getSignature(), executionTime);
        return result;
    }

    @After("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void logExecutionTime(){
        System.out.println("product deleted successfully----------------");
    }

    @Around("@annotation(com.sistore.productservice.logging.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Method Execution started successfully---------");
        LocalDateTime start = LocalDateTime.now();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            System.out.println("Exception occurred: " + ex.getMessage());
            throw ex;
        }
        System.out.println("Method execution ended successfully---------" );
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start,end);
        System.out.println("Total execution time taken --------" + duration.toMillis() + " ms.");
        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.ecommerce.productservice.service.*.*(..))", throwing = "ex")
    public void logException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage());
    }

}
