/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aspect;

import test.CallerSideAdviceTest;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perJVM
 */
public class CallerSideTestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression call(String test.CallerSideTestHelper.invokeMemberMethodPre()) &&
     * within(test.CallerSideAdviceTest)
     */
    Pointcut pc1;

    /**
     * @Expression call(String test.CallerSideTestHelper.invokeMemberMethodPost()) &&
     * within(test.CallerSideAdviceTest)
     */
    Pointcut pc2;

    /**
     * @Expression call(String test.CallerSideTestHelper.invokeMemberMethodPrePost()) &&
     * withincode(* test.CallerSideAdviceTest.test*(..))
     */
    Pointcut pc3;

    /**
     * @Expression call(String test.CallerSideTestHelper.invokeStaticMethodPre()) &&
     * within(test.CallerSideAdviceTest)
     */
    Pointcut pc4;

    /**
     * @Expression call(String test.CallerSideTestHelper.invokeStaticMethodPost()) &&
     * within(test.CallerSideAdviceTest)
     */
    Pointcut pc5;

    /**
     * @Expression call(String test.CallerSideTestHelper.invokeStaticMethodPrePost()) &&
     * withincode(* test.CallerSideAdviceTest.test*(..))
     */
    Pointcut pc6;

    /**
     * @Expression call(* test.CallerSideTestHelper.invokeMemberMethodAround*(..)) &&
     * within(test.CallerSideAdviceTest)
     */
    Pointcut pc7;

    /**
     * @Expression call(* test.CallerSideTestHelper.invokeStaticMethodAround*()) && withincode(*
     * test.CallerSideAdviceTest.test*(..))
     */
    Pointcut pc8;

    // ============ Advices ============

    /**
     * @Before pc1 || pc3 || pc4 || pc6
     */
    public void preAdvice1(final JoinPoint joinPoint) throws Throwable {
        CallerSideAdviceTest.log("pre1 ");
    }

    /**
     * @Before pc1 || pc3 || pc4 || pc6
     */
    public void preAdvice2(final JoinPoint joinPoint) throws Throwable {
        CallerSideAdviceTest.log("pre2 ");
    }

    /**
     * @After pc2 || pc3 || pc5 || pc6
     */
    public void postAdvice1(final JoinPoint joinPoint) throws Throwable {
        CallerSideAdviceTest.log("post1 ");
    }

    /**
     * @After pc2 || pc3 || pc5 || pc6
     */
    public void postAdvice2(final JoinPoint joinPoint) throws Throwable {
        CallerSideAdviceTest.log("post2 ");
    }

    /**
     * @Around pc8 || pc7
     */
    public Object around(final JoinPoint joinPoint) throws Throwable {
        CallerSideAdviceTest.log("before ");
        Object result = joinPoint.proceed();
        CallerSideAdviceTest.log("after ");
        return result;
    }
}