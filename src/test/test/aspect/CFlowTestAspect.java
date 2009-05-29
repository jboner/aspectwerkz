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
 */
public class CFlowTestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression cflow(call(* test.CFlowTest.step1()) AND within(test.CFlowTest))
     */
    Pointcut pc1;

    /**
     * @Expression cflow(call(* test.CFlowTest.step1_A()) AND within(test.CFlowTest))
     */
    Pointcut pc1_A;

    /**
     * @Expression cflow(call(* test.CFlowTest.step1_B()) AND within(test.CFlowTest))
     */
    Pointcut pc1_B;

    /**
     * @Expression execution(* test.CFlowTest.step2())
     */
    Pointcut pc2;

    /**
     * @Expression execution(* test.CFlowTest.step2_B())
     */
    Pointcut pc2_B;

    // ============ Advices ============

    /**
     * @Around pc2 AND pc1
     */
    public Object execute(final JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("advice-before ");
        final Object result = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("advice-after ");
        return result;
    }

    /**
     * @Around pc2_B AND pc1_B AND pc1_A
     */
    public Object execute2(final JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("advice-before2 ");
        final Object result = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("advice-after2 ");
        return result;
    }

    /**
     * @Around execution(* test.CFlowTest.step2Anonymous()) AND cflow(call(*
     * test.CFlowTest.step1Anonymous()) AND within(test.CFlowTest))
     */
    public Object executeAnonymous(final JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("advice-beforeAnonymous ");
        final Object result = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("advice-afterAnonymous ");
        return result;
    }

    /**
     * FIXME: this expression leads to match all at cflow early filtering.
     * <p/>
     * X@Around execution(* test.CFlowTest.step2_C()) AND !cflow(call(* test.CFlowTest.step1_C()) AND
     * within(test.CFlowTest))
     */
    public Object executeC(final JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("advice-beforeC ");
        final Object result = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("advice-afterC ");
        return result;
    }

    /**
     * @After execution(* test.CFlowTest.cflowOnMyself()) && cflow(execution(* test.CFlowTest.cflowOnMyself()))
     */
    public void afterMySelf(JoinPoint joinPoint) {
        ((Loggable) joinPoint.getTarget()).log("advice-cflowOnMyself ");
    }
}