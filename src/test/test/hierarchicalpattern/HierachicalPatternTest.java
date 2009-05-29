/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.hierarchicalpattern;

import junit.framework.TestCase;
import test.Loggable;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class HierachicalPatternTest extends TestCase implements Loggable, DummyInterface1 {
    private String m_logString = "";

    public HierachicalPatternTest() {
    }

    public HierachicalPatternTest(String name) {
        super(name);
    }

    public void testDeclaringType1() {
        m_logString = "";
        declaringType1();
        assertEquals("before1 invocation after1 ", m_logString);
    }

    public void testDeclaringType2() {
        m_logString = "";
        declaringType2();
        assertEquals("before1 invocation after1 ", m_logString);
    }

    public void testReturnType1() {
        m_logString = "";
        returnType1();
        assertEquals("before1 invocation after1 ", m_logString);
    }

    public void testReturnType2() {
        m_logString = "";
        returnType2();
        assertEquals("before1 invocation after1 ", m_logString);
    }

    public void testParameterTypes() {
        m_logString = "";
        parameterTypes(null, null);
        assertEquals("before1 invocation after1 ", m_logString);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(HierachicalPatternTest.class);
    }

    // ==== methods to test ====
    public void log(final String wasHere) {
        m_logString += wasHere;
    }

    public void declaringType1() {
        log("invocation ");
    }

    public void declaringType2() {
        log("invocation ");
    }

    public HierachicalPatternTest returnType1() {
        log("invocation ");
        return null;
    }

    public DummyInterface1 returnType2() {
        log("invocation ");
        return null;
    }

    public void parameterTypes(HierachicalPatternTest d1, HierachicalPatternTest d2) {
        log("invocation ");
    }
}