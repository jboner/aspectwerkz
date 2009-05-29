/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aspect;

import test.Loggable;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perJVM
 * @TODO: need nested pointcuts, need to be able to specify one singe pointcut name for the advice
 * to be able to easily refer to it when modifying the advices at runtime. this the handle is
 * the pointcut expression bound to the advice and this handle then need to be simplified
 * (one single name that can be reused).
 */
public class DynamicDeploymentTestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression execution(void test.DynamicDeploymentTest.reorderAdvicesTestMethod())
     */
    Pointcut pc1;

    /**
     * @Expression execution(void test.DynamicDeploymentTest.removeAdviceTestMethod())
     */
    Pointcut pc2;

    /**
     * @Expression execution(void test.DynamicDeploymentTest.addAdviceTestMethod())
     */
    Pointcut pc3;

    /**
     * @Expression execution(void test.DynamicDeploymentTest.createAspectTestMethod())
     */
    Pointcut pc4;

    // ============ Advices ============

    /**
     * @Around pc1 || pc2 || pc3
     */
    public Object advice1(final JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 ");
        final Object result = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 ");
        return result;
    }

    /**
     * @Around pc1 || pc2 || pc4
     */
    public Object advice2(final JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before2 ");
        final Object result = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after2 ");
        return result;
    }
}