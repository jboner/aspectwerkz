/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.performance;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perJVM
 */
public class PerJVMPerformanceAspect {
    /**
     * Around execution(void test.performance.PerformanceTest.methodAdvisedMethodPerJVM())
     *
     * @Around call(void test.performance.PerformanceTest.methodAdvisedMethodPerJVM())
     * &&
     * within(test.performance.*)
     */
    public Object advice1(final StaticJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * Around call(void test.performance.PerformanceTest.methodAdvisedMethodPerJVM()) &&
     * within(test.performance.*)
     */
    public Object advice2(final JoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * Around call(void test.performance.PerformanceTest.methodAdvisedMethodPerJVM()) &&
     * within(test.performance.*)
     */
    public Object advice3(final JoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * Around call(void test.performance.PerformanceTest.methodAdvisedMethodPerJVM()) &&
     * within(test.performance.*)
     */
    public Object advice4(final JoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * Around call(void test.performance.PerformanceTest.methodAdvisedMethodPerJVM()) &&
     * within(test.performance.*)
     */
    public Object advice5(final JoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * @Mixin within(test.performance.PerformanceTest)
     */
    public static class PerJVMImpl implements PerJVM {
        public void runPerJVM() {
        }
    }
}