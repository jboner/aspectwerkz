/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.staticfield;

import junit.framework.TestCase;

/**
 * Test case for AW-92 for static field
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class StaticFieldAdviceTest extends TestCase {
    public static int s_fieldA = 0;

    public static int s_fieldB = 0;

    public int m_fieldA = 0;

    public int m_fieldB = 0;

    public void testStaticFieldAccessedOutsideStaticCtx() {
        assertEquals(1, accessStaticFieldA());
    }

    public void testStaticFieldAccessedInsideStaticCtx() {
        assertEquals(1, StaticFieldAdviceTest.accessStaticFieldB());
    }

    public void testFieldAccessedOutsideStaticCtx() {
        assertEquals(1, accessFieldA());
    }

    public void testFieldAccessedInsideStaticCtx() {
        assertEquals(1, StaticFieldAdviceTest.accessFieldB(this));
    }

    // -- methods --
    private int accessStaticFieldA() {
        //static field access in member method
        s_fieldA = 1;
        int value = s_fieldA;
        return value;
    }

    private static int accessStaticFieldB() {
        //static field access in static method
        s_fieldB = 1;
        int value = s_fieldB;
        return value;
    }

    private int accessFieldA() {
        //static field access in member method
        m_fieldA = 1;
        int value = m_fieldA;
        return value;
    }

    private static int accessFieldB(StaticFieldAdviceTest myself) {
        //field access in static method
        myself.m_fieldB = 1;
        int value = myself.m_fieldB;
        return value;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(StaticFieldAdviceTest.class);
    }
}