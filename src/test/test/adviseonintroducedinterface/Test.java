/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.adviseonintroducedinterface;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér</a>
 */
public class Test extends TestCase {
    private static String s_logString = "";

    public void testIntroducedMarkerInterface() {
        s_logString = "";
        Target t = new Target();
        t.m1();
        assertEquals("before m1 ", s_logString);
    }

    public void testIntroducedImplementation() {
        s_logString = "";
        Target t = new Target();
        ((Intf2) t).m2();
        assertEquals("before m2 ", s_logString);
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
}
