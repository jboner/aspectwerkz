/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.afterxxx;

import junit.framework.TestCase;

public class Test extends TestCase {
    private static String s_log;

    public static void log(String msg) {
        s_log += msg;
    }

    public void testAll() {
        s_log = "";
        all();
        assertEquals("logAround ", s_log);
    }

    public void testAroundFinally() {
        s_log = "";
        aroundFinally();
        assertEquals("logAround logAfterFinally ", s_log);
    }

    public void testAroundFinallyReturning() {
        s_log = "";
        aroundFinallyReturning();
        assertEquals("logAround logAfterReturning logAfterFinally ", s_log);
    }

    public void testAroundReturning() {
        s_log = "";
        aroundReturning();
        assertEquals("logAround logAfterReturningString logAfterReturning ", s_log);
    }

    public void testAroundFinallyReturningThrowing() {
        s_log = "";
        try {
            aroundFinallyReturningThrowing();
        } catch (UnsupportedOperationException e) {
        }
        assertEquals("logAround logAfterThrowingRTE logAfterFinally ", s_log);
    }

    public void testAroundReturningThrowing() {
        s_log = "";
        try {
            aroundReturningThrowing();
        } catch (UnsupportedOperationException e) {
        }
        assertEquals("logAround logAfterThrowingRTE ", s_log);
    }

    public void testFinally() {
        s_log = "";
        _finally();
        assertEquals("logAfterFinally ", s_log);
    }

    public void testFinallyReturning() {
        s_log = "";
        finallyReturning();
        assertEquals("logAfterReturningString logAfterReturning logAfter logAfterFinally ", s_log);
    }

    public void testFinallyReturningThrowing() {
        s_log = "";
        try {
            finallyReturningThrowing();
        } catch (UnsupportedOperationException e) {
        }
        assertEquals("logAfterThrowingRTE logAfterFinally ", s_log);
    }

    public void testReturning() {
        s_log = "";
        returning();
        assertEquals("logAfterReturningString logAfterReturning ", s_log);
    }

    public void testReturningThrowing() {
        s_log = "";
        try {
            returningThrowing();
        } catch (Exception e) {
        }
        assertEquals("", s_log);
    }

    public Test(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(Test.class);
    }

    void all() {
    }

    void aroundFinally() {
    }

    static Object aroundFinallyReturning() {
        return null;
    }

    Object aroundReturning() {
        return "aroundReturning";
    }

    static Object aroundFinallyReturningThrowing() {
        throw new UnsupportedOperationException();
    }

    Object aroundReturningThrowing() {
        throw new UnsupportedOperationException();
    }

    void _finally() {
    }

    static Object finallyReturning() {
        return "finallyReturning";
    }

    static Object finallyReturningThrowing() {
        throw new UnsupportedOperationException();
    }

    Object returningThrowing() throws Exception {
        throw new Exception();
    }

    Object returning() {
        return "returning";
    }
}