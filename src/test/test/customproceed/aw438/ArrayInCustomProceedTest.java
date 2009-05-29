/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.customproceed.aw438;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class ArrayInCustomProceedTest extends TestCase {
    private static String LOG = "";

    public static void log(String msg) {
        LOG += msg;
    }

    public void target(Integer i, String[] ss) {
        log("target");
    }

    public void testTarget() {
        LOG = "";
        target(new Integer(1), new String[]{"a", "b"});
        assertEquals("AOP target", LOG);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ArrayInCustomProceedTest.class);
    }

    public static class Aspect {

        public static interface MyJoinPoint extends JoinPoint {
            Object proceed(Integer i, String[] objs);
        }

        public Object addRequestTag(MyJoinPoint jp, Integer i, String[] objs) throws Throwable {
            log("AOP ");
            return jp.proceed(i, objs);
        }

    }

}
