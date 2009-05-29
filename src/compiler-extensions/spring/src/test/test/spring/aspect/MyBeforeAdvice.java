package test.spring.aspect;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import test.spring.Test;

public class MyBeforeAdvice implements MethodBeforeAdvice {
    public void _before() throws Throwable {
        Test.log("before ");
    }
    public void before(Method m, Object[] args, Object target) throws Throwable {
        Test.log("before ");
    }
}