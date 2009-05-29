/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aspect;

import test.StaticMethodAdviceTest;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.codehaus.aspectwerkz.joinpoint.Rtti;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perJVM
 */
public class StaticMethodTestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression call(* test.StaticMethodAdviceTest.get*(..)) && within(test.StaticMethodAdviceTest)
     */
    Pointcut static_pc1;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.*Param*(..))
     */
    Pointcut static_pc2;

    /**
     * @Expression call(void test.StaticMethodAdviceTest.methodAdvicedMethod(..)) && within(test.StaticMethodAdviceTest)
     */
    Pointcut static_pc4;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.methodAdvicedMethod(..))
     */
    Pointcut static_pc5;

    /**
     * @Expression call(* test.StaticMethodAdviceTest.methodAdvicedMethodNewThread(..)) && within(test.StaticMethodAdviceTest)
     */
    Pointcut static_pc6;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.multipleMethodAdvicedMethod(..))
     */
    Pointcut static_pc7;

    /**
     * @Expression call(* test.StaticMethodAdviceTest.multipleChainedMethodAdvicedMethod(..)) && within(test.StaticMethodAdviceTest)
     */
    Pointcut static_pc8;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.joinPointMetaData(..))
     */
    Pointcut static_pc9;

    /**
     * @Expression call(void test.StaticMethodAdviceTest.multiplePointcutsMethod(..)) && within(test.StaticMethodAdviceTest)
     */
    Pointcut static_pc10;

    /**
     * @Expression execution(void test.StaticMethodAdviceTest.multiplePointcutsMethod(..))
     */
    Pointcut static_pc11;

    /**
     * @Expression call(* test.StaticMethodAdviceTest.takesArrayAsArgument(String[])) && within(test.StaticMethodAdviceTest)
     */
    Pointcut static_pc12;

    /**
     * @Expression execution(long test.StaticMethodAdviceTest.getPrimitiveAndNullFromAdvice())
     */
    Pointcut static_pc13;

    // ============ Advices ============

    /**
     * @Before static_pc1 || static_pc2 || static_pc5 || static_pc8 || static_pc12
     */
    public void before(final JoinPoint joinPoint) throws Throwable {
    }

    /**
     * @AfterFinally static_pc1 || static_pc2 || static_pc5 || static_pc8 || static_pc12
     */
    public void afterFinally(final JoinPoint joinPoint) throws Throwable {
    }

    /**
     * @Around static_pc1 || static_pc2 || static_pc5 || static_pc8 || static_pc12
     */
    public Object advice1(final JoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * @Around static_pc4 || static_pc7 || static_pc8 || static_pc10
     */
    public Object advice2(final JoinPoint joinPoint) throws Throwable {
        StaticMethodAdviceTest.log("before1 ");
        final Object result = joinPoint.proceed();
        StaticMethodAdviceTest.log("after1 ");
        return result;
    }

    /**
     * @Around static_pc7 || static_pc8 || static_pc11
     */
    public Object advice3(final JoinPoint joinPoint) throws Throwable {
        StaticMethodAdviceTest.log("before2 ");
        final Object result = joinPoint.proceed();
        StaticMethodAdviceTest.log("after2 ");
        return result;
    }

    /**
     * @Around static_pc9
     */
    public Object advice4(final JoinPoint joinPoint) throws Throwable {
        final Object result = joinPoint.proceed();
        MethodRtti mrtti = (MethodRtti) joinPoint.getRtti();
        String metadata = joinPoint.getCalleeClass().getName()
                          + mrtti.getMethod().getName()
                          + mrtti.getParameterValues()[0]
                          + mrtti.getParameterTypes()[0].getName()
                          + mrtti.getReturnType().getName()
                          + mrtti.getReturnValue();
        return metadata;
    }

    /**
     * @Around static_pc6
     */
    public Object advice5(final JoinPoint joinPoint) throws Throwable {
        StaticMethodAdviceTest.log("before ");
        final Object result = joinPoint.proceed();
        StaticMethodAdviceTest.log("after ");
        return result;
    }

    /**
     * @Around static_pc13
     */
    public Object advice7(final JoinPoint joinPoint) throws Throwable {
        return null;
    }
}