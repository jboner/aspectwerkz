/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.intercept.execution;

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
        LOG = "";
        adviseWithAround();
        assertEquals("adviseWithAround ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAround(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around1_pre_execution ");
                        Object result = jp.proceed();
                        InterceptTest.log("around1_post_execution ");
                        return result;
                    }
                }
        );

        LOG = "";
        adviseWithAround();
        assertEquals("around1_pre_execution adviseWithAround around1_post_execution ", LOG);
    }


    public void testAddAndRemoveAround() {
        LOG = "";
        adviseWithAround2();
        assertEquals("adviseWithAround2 ", LOG);

        final AroundAdvice advice = new AroundAdvice() {
            public Object invoke(JoinPoint jp) throws Throwable {
                InterceptTest.log("around1_pre_execution ");
                Object result = jp.proceed();
                InterceptTest.log("around1_post_execution ");
                return result;
            }
        };
        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAround2(..))",
                advice
        );

        LOG = "";
        adviseWithAround2();
        assertEquals("around1_pre_execution adviseWithAround2 around1_post_execution ", LOG);

        ((Advisable) this).aw_removeAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAround2(..))",
                advice.getClass()
        );

        LOG = "";
        adviseWithAround2();
        assertEquals("adviseWithAround2 ", LOG);
    }

    public void testAddAroundStack() {
        LOG = "";
        adviseWithAroundStack();
        assertEquals("adviseWithAroundStack ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAroundStack(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around2_pre_execution ");
                        Object result = jp.proceed();
                        InterceptTest.log("around2_post_execution ");
                        return result;
                    }
                }
        );

        LOG = "";
        adviseWithAroundStack();
        assertEquals("around2_pre_execution adviseWithAroundStack around2_post_execution ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAroundStack(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around3_pre_execution ");
                        Object result = jp.proceed();
                        InterceptTest.log("around3_post_execution ");
                        return result;
                    }
                }
        );

        LOG = "";
        adviseWithAroundStack();
        assertEquals(
                "around2_pre_execution around3_pre_execution adviseWithAroundStack around3_post_execution around2_post_execution ",
                LOG
        );
    }

    public void testAddBefore() {
        LOG = "";
        adviseWithBefore();
        assertEquals("adviseWithBefore ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithBefore(..))",
                new BeforeAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("before ");
                    }
                }
        );

        LOG = "";
        adviseWithBefore();
        assertEquals("before adviseWithBefore ", LOG);
    }

    public void testAddAfter() {
        LOG = "";
        adviseWithAfter();
        assertEquals("adviseWithAfter ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAfter(..))",
                new AfterAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("afterFinally ");
                    }
                }
        );

        LOG = "";
        adviseWithAfter();
        assertEquals("adviseWithAfter afterFinally ", LOG);
    }

    public void testAddAfterReturning() {
        LOG = "";
        adviseWithAfterReturning();
        assertEquals("adviseWithAfterReturning ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAfterReturning(..))",
                new AfterReturningAdvice() {
                    public void invoke(JoinPoint jp, Object returnValue) throws Throwable {
                        InterceptTest.log("afterReturning ");
                        InterceptTest.log((String) returnValue);
                        InterceptTest.log(" ");
                    }
                }
        );

        LOG = "";
        adviseWithAfterReturning();
        assertEquals("adviseWithAfterReturning afterReturning returnValue ", LOG);
    }

    public void testAddAfterReturningPrimitive() {
        LOG = "";
        adviseWithAfterReturningPrimitive();
        assertEquals("adviseWithAfterReturningPrimitive ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAfterReturningPrimitive(..))",
                new AfterReturningAdvice() {
                    public void invoke(JoinPoint jp, Object returnValue) throws Throwable {
                        InterceptTest.log("afterReturning ");
                        InterceptTest.log(((Integer) returnValue).toString());
                        InterceptTest.log(" ");
                    }
                }
        );

        LOG = "";
        adviseWithAfterReturningPrimitive();
        assertEquals("adviseWithAfterReturningPrimitive afterReturning -1 ", LOG);
    }

    public void testAddAfterThrowing() {
        LOG = "";
        try {
            adviseWithAfterThrowing();
        } catch (RuntimeException e) {
        }
        assertEquals("adviseWithAfterThrowing ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.adviseWithAfterThrowing(..))",
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
            adviseWithAfterThrowing();
        } catch (RuntimeException e) {
        }
        assertEquals("adviseWithAfterThrowing afterThrowing noop ", LOG);
    }

    public void testAddAfterAndAfterThrowing() {
        LOG = "";
        try {
            addAfterAndAfterThrowing();
        } catch (RuntimeException e) {
        }
        assertEquals("addAfterAndAfterThrowing ", LOG);

        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.addAfterAndAfterThrowing(..))",
                new AfterAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("after ");
                    }
                }
        );
        ((Advisable) this).aw_addAdvice(
                "execution(* test.intercept.execution.InterceptTest.addAfterAndAfterThrowing(..))",
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
            addAfterAndAfterThrowing();
        } catch (RuntimeException e) {
        }
        assertEquals("addAfterAndAfterThrowing afterThrowing noop after ", LOG);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(InterceptTest.class);
    }

    public void adviseWithAround() {
        log("adviseWithAround ");
    }

    public void adviseWithAround2() {
        log("adviseWithAround2 ");
    }

    public void adviseWithAroundStack() {
        log("adviseWithAroundStack ");
    }

    public void adviseWithBefore() {
        log("adviseWithBefore ");
    }

    public void adviseWithAfter() {
        log("adviseWithAfter ");
    }

    public Object adviseWithAfterReturning() {
        log("adviseWithAfterReturning ");
        return "returnValue";
    }

    public int adviseWithAfterReturningPrimitive() {
        log("adviseWithAfterReturningPrimitive ");
        return -1;
    }

    public void adviseWithAfterThrowing() {
        log("adviseWithAfterThrowing ");
        throw new RuntimeException("noop");
    }

    public void addAfterAndAfterThrowing() {
        log("addAfterAndAfterThrowing ");
        throw new RuntimeException("noop");
    }
}
