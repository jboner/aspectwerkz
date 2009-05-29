/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aj;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Test extends TestCase {
    
    private static String s_logString = "";

    public Test(String name) {
        super(name);
    }

   public void testBefore() throws Exception {
       s_logString = "";
       before();
       assertEquals("before before() ", s_logString);
    }

    public void testAfterFinally() throws Exception {
        s_logString = "";
        afterFinally();
        assertEquals("afterFinally() after-finally ", s_logString);
     }

    public void testAfterReturning() throws Exception {
        s_logString = "";
        afterReturning();
        assertEquals("afterReturning() after-returning ", s_logString);
     }

    public void testAfterThrowing() throws Exception {
        s_logString = "";
        try {
            afterThrowing();
        } catch (RuntimeException e) {
            assertEquals("afterThrowing() after-throwing ", s_logString);
            return;
        }
        fail("RuntimeException should have been catched");
     }

    public void testAround() throws Exception {
        s_logString = "";
        around();
        assertEquals("before-around around() after-around ", s_logString);
     }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(Test.class);
    }

    public static void log(final String wasHere) {
        s_logString += wasHere;
    }

    public long around() {
        log("around() ");
        return 0x1L;
    }

    public int before() {
        log("before() ");
        return -0x1;
    }

    public String afterThrowing() {
        log("afterThrowing() ");
        throw new RuntimeException();
    }

    public Object afterReturning() {
        log("afterReturning() ");
        return "string";
    }

    public void afterFinally() {
        log("afterFinally() ");
    }
}
