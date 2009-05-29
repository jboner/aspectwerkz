/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aopalliance;

import junit.framework.TestCase;

/**
 * TODO test constructor and field interception
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Test extends TestCase {
    
    private static String s_logString = "";

    public Test(String name) {
        super(name);
    }

    public void testExecution() throws Exception {
        s_logString = "";
        execution();
        assertEquals("before-intercept execution test.aopalliance.Test execution() after-intercept ", s_logString);
     }

    public void testCall() throws Exception {
        s_logString = "";
        call();
        assertEquals("before-intercept call test.aopalliance.Test call() after-intercept ", s_logString);
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

    public long execution() {
        log("execution() ");
        return 0x1L;
    }

    public long call() {
        log("call() ");
        return 0x1L;
    }
}
