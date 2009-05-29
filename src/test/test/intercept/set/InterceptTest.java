/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.intercept.set;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.intercept.BeforeAdvice;
import org.codehaus.aspectwerkz.intercept.Advisable;
import org.codehaus.aspectwerkz.intercept.AroundAdvice;
import org.codehaus.aspectwerkz.intercept.AfterAdvice;

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
        adviseWithAround = 1;
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "set(* test.intercept.set.InterceptTest.adviseWithAround)",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around1_pre ");
                        Object result = jp.proceed();
                        InterceptTest.log("around1_post ");
                        return result;
                    }
                }
        );

        LOG = "";
        adviseWithAround = 1;
        assertEquals("around1_pre around1_post ", LOG);
    }


    public void testAddAndRemoveAround() {
        LOG = "";
        adviseWithAround2 = "test";
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "set(* test.intercept.set.InterceptTest.adviseWithAround2)",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around1_pre ");
                        Object result = jp.proceed();
                        InterceptTest.log("around1_post ");
                        return result;
                    }
                }
        );

        LOG = "";
        adviseWithAround2 = "test";
        assertEquals("around1_pre around1_post ", LOG);

        ((Advisable) this).aw_removeAdvice("set(* test.intercept.set.InterceptTest.adviseWithAround2)", AroundAdvice.class);

        LOG = "";
        adviseWithAround2 = "test";
        assertEquals("", LOG);
    }

    public void testAddAroundStack() {
        LOG = "";
        adviseWithAroundStack = 2;
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "set(* test.intercept.set.InterceptTest.adviseWithAroundStack)",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around2_pre ");
                        Object result = jp.proceed();
                        InterceptTest.log("around2_post ");
                        return result;
                    }
                }
        );

        LOG = "";
        adviseWithAroundStack = 8;
        assertEquals("around2_pre around2_post ", LOG);

        ((Advisable) this).aw_addAdvice(
                "set(* test.intercept.set.InterceptTest.adviseWithAroundStack)",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("around3_pre ");
                        Object result = jp.proceed();
                        InterceptTest.log("around3_post ");
                        return result;
                    }
                }
        );

        LOG = "";
        adviseWithAroundStack = 4;
        assertEquals("around2_pre around3_pre around3_post around2_post ", LOG);
    }

    public void testAddBefore() {
        LOG = "";
        adviseWithBefore = new Object();
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "set(* test.intercept.set.InterceptTest.adviseWithBefore)",
                new BeforeAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("before ");
                    }
                }
        );

        LOG = "";
        adviseWithBefore = new Integer(1);
        assertEquals("before ", LOG);
    }

    public void testAddAfter() {
        LOG = "";
        adviseWithAfter = false;
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "set(* test.intercept.set.InterceptTest.adviseWithAfter)",
                new AfterAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("afterFinally ");
                    }
                }
        );

        LOG = "";
        adviseWithAfter = true;
        assertEquals("afterFinally ", LOG);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(InterceptTest.class);
    }

    int adviseWithAround;

    String adviseWithAround2;

    int adviseWithAroundStack;

    Object adviseWithBefore;

    boolean adviseWithAfter;
}
