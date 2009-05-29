/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.rtti;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class RttiTest extends TestCase {

    public void testTarget() {
        RttiTarget.LOG = new StringBuffer("");
        RttiTarget rttiTarget = new RttiTarget();
        rttiTarget.doSomething(1);
        // will have a nested call to another instance of target, with arg0 = 2

        assertEquals(
                "+Target-1.1 Target-1.1 +Target-2.2 Target-2.2 -Target-2.2 -Target-1.1 ", RttiTarget.LOG.toString()
        );

        RttiTarget.LOG = new StringBuffer();
        rttiTarget = new RttiTarget();
        rttiTarget.doSomething(3);
        assertEquals("+Target-3.3 Target-3.3 -Target-3.3 ", RttiTarget.LOG.toString());
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(RttiTest.class);
    }


}
