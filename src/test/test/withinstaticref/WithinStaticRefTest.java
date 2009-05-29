/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.withinstaticref;

import junit.framework.TestCase;

/**
 * Test against an Eclipse compiled xxx.class that uses DUP_X1
 *
 * @see src/testdata/test.withinstaticref.TargetA
 */
public class WithinStaticRefTest extends TestCase {
    public void testWithinAspect() {
        WithinAspect.s_count = 0;
        TargetA.suite();
        assertEquals(WithinAspect.s_count, 4);
        // and no verify error
    }
    
    // -- JUnit
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(WithinStaticRefTest.class);
    }
}
