/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.intercept.handler;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.intercept.BeforeAdvice;
import org.codehaus.aspectwerkz.intercept.Advisable;

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

    public void testAddBefore() {
        LOG = "";
        adviseWithBefore();
        assertEquals("adviseWithBefore ", LOG);

        ((Advisable) this).aw_addAdvice(
                "handler(java.lang.IllegalArgumentException)",
                new BeforeAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        InterceptTest.log("before_catch_block ");
                    }
                }
        );

        LOG = "";
        adviseWithBefore();
        assertEquals("before_catch_block adviseWithBefore ", LOG);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(InterceptTest.class);
    }

    public void adviseWithBefore() {
        try {
            throw new IllegalArgumentException("noop");
        } catch (IllegalArgumentException e) {
            log("adviseWithBefore ");
        }
    }
}
