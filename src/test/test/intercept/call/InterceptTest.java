/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.intercept.call;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.intercept.BeforeAdvice;
import org.codehaus.aspectwerkz.intercept.Advisable;
import org.codehaus.aspectwerkz.intercept.AroundAdvice;
import org.codehaus.aspectwerkz.intercept.AfterAdvice;
import org.codehaus.aspectwerkz.intercept.AfterReturningAdvice;
import org.codehaus.aspectwerkz.intercept.AfterThrowingAdvice;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class InterceptTest extends TestCase {
    private static String LOG = "";

    public static void log(String msg) {
        LOG += msg;
    }

    public void testIsAdvisable() {
        assertTrue(this instanceof Advisable);
    }

    public void testAddAround() {
        Callee callee = new Callee();

        LOG = "";
        callee.adviseWithAround();
        assertEquals("adviseWithAround ", LOG);

        ((Advisable) this).aw_addAdvice(
                "call(* test.intercept.call.Callee.adviseWithAround(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around1_pre_call ");
                        Object result = jp.proceed();
                        InterceptTest.log("around1_post_call ");
                        return result;
                    }
                }
        );

        LOG = "";
        callee.adviseWithAround();
        assertEquals("around1_pre_call adviseWithAround around1_post_call ", LOG);
    }


    public void testAddAndRemoveAround() {
        Callee callee = new Callee();

        LOG = "";
        callee.adviseWithAround2();
        assertEquals("adviseWithAround2 ", LOG);

        ((Advisable) this).aw_addAdvice(
                "call(* test.intercept.call.Callee.adviseWithAround2(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around1_pre_call ");
                        Object result = jp.proceed();
                        InterceptTest.log("around1_post_call ");
                        return result;
                    }
                }
        );

        LOG = "";
        callee.adviseWithAround2();
        assertEquals("around1_pre_call adviseWithAround2 around1_post_call ", LOG);

        ((Advisable) this).aw_removeAdvice("call(* test.intercept.call.Callee.adviseWithAround2(..))", AroundAdvice.class);

        LOG = "";
        callee.adviseWithAround2();
        assertEquals("adviseWithAround2 ", LOG);
    }

    public void testAddAroundStack() {
        Callee callee = new Callee();

        LOG = "";
        callee.adviseWithAroundStack();
        assertEquals("adviseWithAroundStack ", LOG);

        ((Advisable) this).aw_addAdvice(
                "call(* test.intercept.call.Callee.adviseWithAroundStack(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around2_pre_call ");
                        Object result = jp.proceed();
                        InterceptTest.log("around2_post_call ");
                        return result;
                    }
                }
        );

        LOG = "";
        callee.adviseWithAroundStack();
        assertEquals("around2_pre_call adviseWithAroundStack around2_post_call ", LOG);

        ((Advisable) this).aw_addAdvice(
                "call(* test.intercept.call.Callee.adviseWithAroundStack(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around3_pre_call ");
                        Object result = jp.proceed();
                        InterceptTest.log("around3_post_call ");
                        return result;
                    }
                }
        );

        LOG = "";
        callee.adviseWithAroundStack();
        assertEquals(
                "around2_pre_call around3_pre_call adviseWithAroundStack around3_post_call around2_post_call ", LOG
        );
    }

    public void testAddBefore() {
        Callee callee = new Callee();

        LOG = "";
        callee.adviseWithBefore();
        assertEquals("adviseWithBefore ", LOG);

        ((Advisable) this).aw_addAdvice(
                "call(* test.intercept.call.Callee.adviseWithBefore(..))",
                new BeforeAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("before ");
                    }
                }
        );

        LOG = "";
        callee.adviseWithBefore();
        assertEquals("before adviseWithBefore ", LOG);
    }

    public void testAddAfter() {
        Callee callee = new Callee();

        LOG = "";
        callee.adviseWithAfter();
        assertEquals("adviseWithAfter ", LOG);

        ((Advisable) this).aw_addAdvice(
                "call(* test.intercept.call.Callee.adviseWithAfter(..))",
                new AfterAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("afterFinally ");
                    }
                }
        );

        LOG = "";
        callee.adviseWithAfter();
        assertEquals("adviseWithAfter afterFinally ", LOG);
    }

    public void testAddAfterReturning() {
        Callee callee = new Callee();

        LOG = "";
        callee.adviseWithAfterReturning();
        assertEquals("adviseWithAfterReturning ", LOG);

        ((Advisable) this).aw_addAdvice(
                "call(* test.intercept.call.Callee.adviseWithAfterReturning(..))",
                new AfterReturningAdvice() {
                    public void invoke(JoinPoint jp, Object returnValue) throws Throwable {
                        InterceptTest.log("afterReturning ");
                        InterceptTest.log((String) returnValue);
                        InterceptTest.log(" ");
                    }
                }
        );

        LOG = "";
        callee.adviseWithAfterReturning();
        assertEquals("adviseWithAfterReturning afterReturning returnValue ", LOG);
    }

    public void testAddAfterThrowing() {
        Callee callee = new Callee();

        LOG = "";
        try {
            callee.adviseWithAfterThrowing();
        } catch (RuntimeException e) {
        }
        assertEquals("adviseWithAfterThrowing ", LOG);

        ((Advisable) this).aw_addAdvice(
                "call(* test.intercept.call.Callee.adviseWithAfterThrowing(..))",
                new AfterThrowingAdvice() {
                    public void invoke(JoinPoint jp, Throwable exception) throws Throwable {
                        InterceptTest.log("afterThrowing ");
                        InterceptTest.log(exception.getMessage());
                        InterceptTest.log(" ");
                    }
                }
        );

        LOG = "";
        try {
            callee.adviseWithAfterThrowing();
        } catch (RuntimeException e) {
        }
        assertEquals("adviseWithAfterThrowing afterThrowing noop ", LOG);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(InterceptTest.class);
    }
}
