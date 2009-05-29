/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.spring;

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
        adviseBefore();
        assertEquals("before adviseBefore ", s_logString);
     }

    public void testAfterReturning() throws Exception {
        s_logString = "";
        adviseAfterReturning();
        assertEquals("adviseAfterReturning afterReturning ", s_logString);
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

    public long adviseAfterReturning() {
        log("adviseAfterReturning ");
        return 0x1L;
    }

    public long adviseBefore() {
        log("adviseBefore ");
        return 0x1L;
    }
}
