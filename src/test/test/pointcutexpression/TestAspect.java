/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.pointcutexpression;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect
 */
public class TestAspect {
    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.A())
     */
    Pointcut A;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.B())
     */
    Pointcut B;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.C())
     */
    Pointcut C;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.D())
     */
    Pointcut D;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.E())
     */
    Pointcut E;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.F())
     */
    Pointcut F;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.G())
     */
    Pointcut G;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.H())
     */
    Pointcut H;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.I())
     */
    Pointcut I;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.J())
     */
    Pointcut J;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.K())
     */
    Pointcut K;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.L())
     */
    Pointcut L;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.M())
     */
    Pointcut M;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.N())
     */
    Pointcut N;

    /**
     * @Expression execution(void test.pointcutexpression.PointcutExpressionTest.O())
     */
    Pointcut O;

    /**
     * @Around B || C
     */
    public Object advice1(final JoinPoint joinPoint) throws Throwable {
        PointcutExpressionTest.log("before1 ");
        final Object result = joinPoint.proceed();
        PointcutExpressionTest.log("after1 ");
        return result;
    }

    /**
     * @Around D && !E
     */
    public Object advice2(final JoinPoint joinPoint) throws Throwable {
        PointcutExpressionTest.log("before1 ");
        final Object result = joinPoint.proceed();
        PointcutExpressionTest.log("after1 ");
        return result;
    }

    /**
     * @Around "(F || G) && H"
     */
    public Object advice3(final JoinPoint joinPoint) throws Throwable {
        PointcutExpressionTest.log("before1 ");
        final Object result = joinPoint.proceed();
        PointcutExpressionTest.log("after1 ");
        return result;
    }

    /**
     * @Around "(I || J)"
     */
    public Object advice4(final JoinPoint joinPoint) throws Throwable {
        PointcutExpressionTest.log("before1 ");
        final Object result = joinPoint.proceed();
        PointcutExpressionTest.log("after1 ");
        return result;
    }

    /**
     * @Around !K && !(L || M) && N
     */
    public Object advice5(final JoinPoint joinPoint) throws Throwable {
        PointcutExpressionTest.log("before1 ");
        final Object result = joinPoint.proceed();
        PointcutExpressionTest.log("after1 ");
        return result;
    }

    /**
     * @Around O
     */
    public Object advice6(final JoinPoint joinPoint) throws Throwable {
        PointcutExpressionTest.log("before1 ");
        final Object result = joinPoint.proceed();
        PointcutExpressionTest.log("after1 ");
        return result;
    }
}