package test.spring.aspect;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import test.spring.Test;

public class MyAfterReturningAdvice implements AfterReturningAdvice {
    public void afterReturning(Object object, Method m, Object[] args, Object target) throws Throwable {
        Test.log("afterReturning ");
    }
}
