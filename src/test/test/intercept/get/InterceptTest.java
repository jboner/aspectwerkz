/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.intercept.get;

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
        int tmp1 = adviseWithAround;
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "get(* test.intercept.get.InterceptTest.adviseWithAround)",
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
        int tmp2 = adviseWithAround;
        assertEquals("around1_pre around1_post ", LOG);
    }


    public void testAddAndRemoveAround() {
        LOG = "";
        String tmp1 = adviseWithAround2;
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "get(* test.intercept.get.InterceptTest.adviseWithAround2)",
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
        String tmp2 = adviseWithAround2;
        assertEquals("around1_pre around1_post ", LOG);

        ((Advisable) this).aw_removeAdvice("get(* test.intercept.get.InterceptTest.adviseWithAround2)", AroundAdvice.class);

        LOG = "";
        String tmp3 = adviseWithAround2;
        assertEquals("", LOG);
    }

    public void testAddAroundStack() {
        LOG = "";
        int tmp1 = adviseWithAroundStack;
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "get(* test.intercept.get.InterceptTest.adviseWithAroundStack)",
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
        int tmp2 = adviseWithAroundStack;
        assertEquals("around2_pre around2_post ", LOG);

        ((Advisable) this).aw_addAdvice(
                "get(* test.intercept.get.InterceptTest.adviseWithAroundStack)",
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
        int tmp3 = adviseWithAroundStack;
        assertEquals("around2_pre around3_pre around3_post around2_post ", LOG);
    }

    public void testAddBefore() {
        LOG = "";
        Object tmp1 = adviseWithBefore;
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "get(* test.intercept.get.InterceptTest.adviseWithBefore)",
                new BeforeAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("before ");
                    }
                }
        );

        LOG = "";
        Object tmp2 = adviseWithBefore;
        assertEquals("before ", LOG);
    }

    public void testAddAfter() {
        LOG = "";
        boolean tmp1 = adviseWithAfter;
        assertEquals("", LOG);

        ((Advisable) this).aw_addAdvice(
                "get(* test.intercept.get.InterceptTest.adviseWithAfter)",
                new AfterAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("afterFinally ");
                    }
                }
        );

        LOG = "";
        boolean tmp2 = adviseWithAfter;
        assertEquals("afterFinally ", LOG);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(InterceptTest.class);
    }

    int adviseWithAround = -1;

    String adviseWithAround2 = "lala";

    int adviseWithAroundStack = 135;

    Object adviseWithBefore = new Boolean(true);

    boolean adviseWithAfter = false;
}
