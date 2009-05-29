/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.staticfield;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect
 */
public class TestAspect {
    /**
     * @Expression set(* test.staticfield.StaticFieldAdviceTest.s_field*)
     */
    Pointcut pcSet;

    /**
     * @Expression set(* test.staticfield.StaticFieldAdviceTest.m_field*)
     */
    Pointcut pcSetMember;

    /**
     * @Expression set(* test.staticfield.CollectionFieldTest.s_field)
     */
    Pointcut pcSetColl;

    /**
     * @Expression set(* test.staticfield.CollectionFieldTest.m_field)
     */
    Pointcut pcSetMemberColl;

    /**
     * @Expression get(* test.staticfield.CollectionFieldTest.s_field)
     */
    Pointcut pcGetColl;

    /**
     * @Expression get(* test.staticfield.CollectionFieldTest.m_field)
     */
    Pointcut pcGetMemberColl;

    /**
     * @Expression within(test.staticfield.*)
     */
    Pointcut filter;

    /**
     * @Before pcSet && filter
     */
    public void preStaticField(final JoinPoint joinPoint) throws Throwable {
        CollectionFieldTest.s_log += "MyPreAdvice1 ";
    }

    /**
     * @Before pcSetMember && filter
     */
    public void preMemberField1(final JoinPoint joinPoint) throws Throwable {
        CollectionFieldTest.s_log += "MyPreAdvice2 ";
    }

    /**
     * @Before pcSetColl && filter
     */
    public void preStaticField2(final JoinPoint joinPoint) throws Throwable {
        CollectionFieldTest.s_log += "MyPreAdvice1 ";
    }

    /**
     * @Before pcSetMemberColl && filter
     */
    public void preMemberField2(final JoinPoint joinPoint) throws Throwable {
        CollectionFieldTest.s_log += "MyPreAdvice2 ";
    }

    /**
     * @After pcGetColl && filter
     */
    public void postStaticField(final JoinPoint joinPoint) throws Throwable {
        CollectionFieldTest.s_log += "MyPostAdvice1 ";
    }

    /**
     * @After pcGetMemberColl && filter
     */
    public void postMemberField(final JoinPoint joinPoint) throws Throwable {
        CollectionFieldTest.s_log += "MyPostAdvice2 ";
    }
}