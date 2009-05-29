/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aspect;

import test.FieldAdviceTest;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.FieldRtti;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perJVM
 */
public class FieldTestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression set(* test.FieldAdviceTest.m_setFieldPreAdvice*)
     */
    Pointcut pc1;

    /**
     * @Expression set(int test.FieldAdviceTest.m_setFieldPreAdvi*)
     */
    Pointcut pc2;

    /**
     * @Expression set(* test.FieldAdviceTest.m_setFie*dPostAdviced)
     */
    Pointcut pc3;

    /**
     * @Expression set(* test.FieldAdviceTest.m_se*FieldPostAdviced)
     */
    Pointcut pc4;

    /**
     * @Expression set(* test.FieldAdviceTest.m_setFieldPrePostAdviced)
     */
    Pointcut pc5;

    /**
     * @Expression get(* test.FieldAdviceTest.m_getFieldPreAdvic*)
     */
    Pointcut pc6;

    /**
     * @Expression get(* test.FieldAdviceTest.m_getFieldPreAdvice*)
     */
    Pointcut pc7;

    /**
     * @Expression get(* test.FieldAdviceTest.m_getFieldPostAdviced)
     */
    Pointcut pc8;

    /**
     * @Expression get(* test.FieldAdviceTest.m_getFieldPrePostAdviced)
     */
    Pointcut pc9;

    /**
     * @Expression set(* test.FieldAdviceTest.s_setStaticFieldPreAdvic*)
     */
    Pointcut pc10;

    /**
     * @Expression set(* test.FieldAdviceTest.s_setStaticFieldPreAdvice*)
     */
    Pointcut pc11;

    /**
     * @Expression set(* test.FieldAdviceTest.s_setStaticFieldPostAdviced)
     */
    Pointcut pc12;

    /**
     * @Expression set(* test.FieldAdviceTest.s_setStaticFieldPrePostAdviced)
     */
    Pointcut pc13;

    /**
     * @Expression get(* test.FieldAdviceTest.s_getStaticFieldPreAdvice*)
     */
    Pointcut pc14;

    /**
     * @Expression get(* test.FieldAdviceTest.s_getStaticFieldPreAdvic*)
     */
    Pointcut pc15;

    /**
     * @Expression get(* test.FieldAdviceTest.s_getStaticFieldPostAdviced)
     */
    Pointcut pc16;

    /**
     * @Expression get(* test.FieldAdviceTest.s_getStaticFieldPrePostAdviced)
     */
    Pointcut pc17;

    /**
     * @Expression set(* test.FieldAdviceTest.m_setFieldAroundAdviced)
     */
    Pointcut pc18;

    /**
     * @Expression set(* test.FieldAdviceTest.s_setStaticFieldAroundAdviced)
     */
    Pointcut pc19;

    /**
     * @Expression get(* test.FieldAdviceTest.m_getFieldAroundAdviced)
     */
    Pointcut pc20;

    /**
     * @Expression get(* test.FieldAdviceTest.s_getStaticFieldAroundAdviced)
     */
    Pointcut pc21;

    /**
     * @Expression set(* test.FieldAdviceTest.m_setFieldAroundAdviced*WithNullAdvice)
     */
    Pointcut pc22;

    /**
     * @Expression get(* test.FieldAdviceTest.m_getFieldAroundAdvicedWithNullAdvice)
     */
    Pointcut pc23;

    /**
     * @Expression set(* test.FieldAdviceTest.m_setFieldAroundAdvicedObjectWithAPI)
     */
    Pointcut pc24;

    /**
     * @Expression set(* test.FieldAdviceTest.m_setFieldAroundAdvicedWithAPI)
     */
    Pointcut pc25;

    /**
     * @Expression within(test.FieldAdviceTest)
     */
    Pointcut filter;

    // ============ Advices ============

    /**
     * @Before filter && (pc2 || pc5 || pc10 || pc13 || pc6 || pc9 || pc14 || pc17)
     */
    public void preAdvice1(final JoinPoint joinPoint) throws Throwable {
        FieldAdviceTest.log("pre1 ");
    }

    /**
     * @Before filter && (pc1 || pc5 || pc11 || pc13 || pc7 || pc9 || pc15 || pc17)
     */
    public void preAdvice2(final JoinPoint joinPoint) throws Throwable {
        FieldAdviceTest.log("pre2 ");
    }

    /**
     * @After filter && (pc4 || pc5 || pc12 || pc13 || pc8 || pc9 || pc16 || pc17)
     */
    public void postAdvice1(final JoinPoint joinPoint) throws Throwable {
        FieldAdviceTest.log("post1 ");
    }

    /**
     * @After filter && (pc3 || pc5 || pc12 || pc13 || pc8 || pc9 || pc16 || pc17)
     */
    public void postAdvice2(final JoinPoint joinPoint) throws Throwable {
        FieldAdviceTest.log("post2 ");
    }

    /**
     * @Around filter && (pc18 || pc19 || pc20 || pc21)
     */
    public Object around(final JoinPoint joinPoint) throws Throwable {
        FieldAdviceTest.log("before ");
        final Object result = joinPoint.proceed();
        FieldAdviceTest.log("after ");
        return result;
    }

    /**
     * @Around filter && (pc22 || pc23)
     */
    public Object aroundNullAdvice(final JoinPoint joinPoint) throws Throwable {
        FieldAdviceTest.log("before ");
        final Object result = joinPoint.proceed();
        FieldAdviceTest.log("after ");
        return null;
    }

    /**
     * @Before get(java.io.PrintStream out) && withincode(* test.FieldAdviceTest.testPublicFieldOutOfWeaverScope())
     */
    public void beforePublicFieldOutOfWeaverScope() {
        FieldAdviceTest.log("adviceOnPublicField ");
    }



    //TODO - activate when proceed(args) will be supported

//    /**
//     * @Around pc24
//     */
//    public Object aroundAdviceAltering(final JoinPoint joinPoint) throws Throwable {
//        FieldAdviceTest.log("before ");
//        FieldRtti rtti = (FieldRtti) joinPoint.getRtti();
//        rtti.setFieldValue(new String("byAdvice"));
//        joinPoint.proceed();
//        FieldAdviceTest.log("after ");
//        return null;
//    }
//
//    /**
//     * @Around pc25
//     */
//    public Object aroundAdviceAlteringPrimitive(final JoinPoint joinPoint) throws Throwable {
//        FieldAdviceTest.log("before ");
//        FieldRtti rtti = (FieldRtti) joinPoint.getRtti();
//        rtti.setFieldValue(new Integer(3));
//        joinPoint.proceed();
//        FieldAdviceTest.log("after ");
//        return null;
//    }
}