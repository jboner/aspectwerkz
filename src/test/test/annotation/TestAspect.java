/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.annotation;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perJVM
 */
public class TestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression call(@test.annotation.PrivateMethodAnnotationProxy * test.annotation.*.*(..)) &&
     * within(test.annotation.*)
     */
    Pointcut call_privateMethod;

    /**
     * @Expression execution(@test.annotation.PrivateMethodAnnotationProxy * test.annotation.*.*(..))
     */
    Pointcut execution_privateMethod;

    /**
     * @Expression call(@test.annotation.ProtectedMethodAnnotationProxy * test.annotation.*.*(..)) &&
     * within(test.annotation.*)
     */
    Pointcut call_protectedMethod;

    /**
     * @Expression execution(@test.annotation.ProtectedMethodAnnotationProxy * test.annotation.*.*(..))
     */
    Pointcut execution_protectedMethod;

    /**
     * @Expression call(@test.annotation.PackagePrivateMethodAnnotationProxy * test.annotation.*.*(..)) &&
     * within(test.annotation.*)
     */
    Pointcut call_packagePrivateMethod;

    /**
     * @Expression execution(@test.annotation.PackagePrivateMethodAnnotationProxy * test.annotation.*.*(..))
     */
    Pointcut execution_packagePrivateMethod;

    /**
     * @Expression call(@test.annotation.PublicMethodAnnotationProxy * test.annotation.*.*(..)) &&
     * within(test.annotation.*)
     */
    Pointcut call_publicMethod;

    /**
     * @Expression execution(@test.annotation.PublicMethodAnnotationProxy * test.annotation.*.*(..))
     */
    Pointcut execution_publicMethod;

    /**
     * @Expression execution(@test.annotation.PublicMethodAnnotationProxy2 * test.annotation.*.*(..))
     */
    Pointcut execution_publicMethod2;

    /**
     * @Expression get(@test.annotation.PrivateFieldAnnotationProxy * test.annotation.*.*) && within(test.annotation.*)
     */
    Pointcut get_privateField;

    /**
     * @Expression set(@test.annotation.PrivateFieldAnnotationProxy * test.annotation.*.*) && within(test.annotation.*)
     */
    Pointcut set_privateField;

    /**
     * @Expression get(@test.annotation.ProtectedFieldAnnotationProxy * test.annotation.*.*) && within(test.annotation.*)
     */
    Pointcut get_protectedField;

    /**
     * @Expression set(@test.annotation.ProtectedFieldAnnotationProxy * test.annotation.*.*) && within(test.annotation.*)
     */
    Pointcut set_protectedField;

    /**
     * @Expression get(@test.annotation.PackagePrivateFieldAnnotationProxy * test.annotation.*.*) && within(test.annotation.*)
     */
    Pointcut get_packagePrivateField;

    /**
     * @Expression set(@test.annotation.PackagePrivateFieldAnnotationProxy * test.annotation.*.*) && within(test.annotation.*)
     */
    Pointcut set_packagePrivateField;

    /**
     * @Expression get(@test.annotation.PublicFieldAnnotationProxy * test.annotation.*.*) && within(test.annotation.*)
     */
    Pointcut get_publicField;

    /**
     * @Expression set(@test.annotation.PublicFieldAnnotationProxy * test.annotation.*.*) && within(test.annotation.*)
     */
    Pointcut set_publicField;

    // ============ Advices ============

    /**
     * @Around call_privateMethod || call_protectedMethod || call_packagePrivateMethod ||
     * call_publicMethod
     */
    public Object advice_CALL(final JoinPoint joinPoint) throws Throwable {
        AnnotationTest.log("call ");
        Object result = joinPoint.proceed();
        AnnotationTest.log("call ");
        return result;
    }

    /**
     * @Around execution_privateMethod || execution_protectedMethod ||
     * execution_packagePrivateMethod || execution_publicMethod
     */
    public Object advice_EXECUTION(final JoinPoint joinPoint) throws Throwable {
        AnnotationTest.log("execution ");
        Object result = joinPoint.proceed();
        AnnotationTest.log("execution ");
        return result;
    }

    /**
     * @Around execution_publicMethod2
     */
    public Object advice_EXECUTION2(final JoinPoint joinPoint) throws Throwable {
        AnnotationTest.log("execution2 ");
        Object result = joinPoint.proceed();
        AnnotationTest.log("execution2 ");
        return result;
    }

    /**
     * @Around set_privateField || set_protectedField || set_packagePrivateField || set_publicField
     */
    public Object advice_SET(final JoinPoint joinPoint) throws Throwable {
        AnnotationTest.log("set ");
        Object result = joinPoint.proceed();
        AnnotationTest.log("set ");
        return result;
    }

    /**
     * @Around get_privateField || get_protectedField || get_packagePrivateField || get_publicField
     */
    public Object advice_GET(final JoinPoint joinPoint) throws Throwable {
        AnnotationTest.log("get ");
        Object result = joinPoint.proceed();
        AnnotationTest.log("get ");
        return result;
    }
}