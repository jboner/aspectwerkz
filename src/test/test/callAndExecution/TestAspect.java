/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.callAndExecution;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class TestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression call(void test.callAndExecution.CallExecutionTest.privateMethod()) &&
     * within(test.callAndExecution.*)
     */
    Pointcut call1;

    /**
     * @Expression call(void test.callAndExecution.CallExecutionTest.publicMethod()) &&
     * within(test.callAndExecution.*)
     */
    Pointcut call2;

    /**
     * @Expression call(void test.callAndExecution.Intf+.called()) &&
     * within(test.callAndExecution.*)
     */
    Pointcut callIntf;

    /**
     * @Expression call(void test.callAndExecution.Abstract+.called()) &&
     * within(test.callAndExecution.*)
     */
    Pointcut callAbstract;

    /**
     * @Expression execution(void test.callAndExecution.CallExecutionTest.privateMethod())
     */
    Pointcut execution1;

    /**
     * @Expression execution(void test.callAndExecution.CallExecutionTest.publicMethod())
     */
    Pointcut execution2;

    /**
     * @Expression execution(void test.callAndExecution.Intf+.called())
     */
    Pointcut executionIntf;

    /**
     * @Expression execution(void test.callAndExecution.Abstract+.called())
     */
    Pointcut executionAbstract;

    // ============ Advices ============

    /**
     * @Around call1 || call2 || callIntf || callAbstract
     */
    public Object advice1(final JoinPoint joinPoint) throws Throwable {
        CallExecutionTest.log("call1 ");
        Object result = joinPoint.proceed();
        CallExecutionTest.log("call2 ");
        return result;
    }

    /**
     * @Around execution1 || execution2 || executionIntf || executionAbstract
     */
    public Object advice2(final JoinPoint joinPoint) throws Throwable {
        CallExecutionTest.log("execution1 ");
        Object result = joinPoint.proceed();
        CallExecutionTest.log("execution2 ");
        return result;
    }
}