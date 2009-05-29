/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.callAndExecution;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class CallExecutionTest extends TestCase implements Intf {
    private static String s_logString = "";

    public CallExecutionTest() {
    }

    public CallExecutionTest(String name) {
        super(name);
    }

    public void testPrivateMethod() {
        s_logString = "";
        privateMethod();
        assertEquals("call1 execution1 invocation execution2 call2 ", s_logString);
    }

    public void testPublicMethod() {
        s_logString = "";
        publicMethod();
        assertEquals("call1 execution1 invocation execution2 call2 ", s_logString);
    }

    public void testIntfMethod() {
        //AW-253
        s_logString = "";
        Intf me = new CallExecutionTest();
        me.called();
        me.called(1);
        assertEquals("call1 execution1 invocation execution2 call2 ", s_logString);

        s_logString = "";
        CallExecutionTest me2 = new CallExecutionTest();
        me2.called();
        me2.called(1);
        assertEquals("call1 execution1 invocation execution2 call2 ", s_logString);
    }

    public void testAbstractMethod() {
        //AW-253
        s_logString = "";
        Abstract me = new Abstract.AbstractImpl();
        me.called();
        assertEquals("call1 execution1 invocation execution2 call2 ", s_logString);

        s_logString = "";
        Abstract.AbstractImpl me2 = new Abstract.AbstractImpl();
        me2.called();
        assertEquals("call1 execution1 invocation execution2 call2 ", s_logString);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(CallExecutionTest.class);
    }

    // ==== methods to test ====
    public static void log(final String wasHere) {
        s_logString += wasHere;
    }

    private void privateMethod() {
        log("invocation ");
    }

    public void publicMethod() {
        log("invocation ");
    }

    public void called() {
        //AW-253 interface declared method
        log("invocation ");
    }

    public void called(int i) {
        //AW-253 interface declared method
        // not used, but force the compiler to do invokeinterface
    }
}