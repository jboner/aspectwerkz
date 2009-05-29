/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.afterxxx;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class TestBinding extends TestCase {

    private static String s_log;

    public void testReturnInt() {
        s_log = "";
        returnInt(1);
        assertEquals("afterReturningInt 2", s_log);
    }

    public void testReturnString() {
        s_log = "";
        returnString("hello");
        assertEquals("afterReturningString hellohello", s_log);
    }

    public void testThrowing() {
        s_log = "";
        try {
            throwChecked();
        } catch (Throwable t) {
            //System.out.println(s_log);
            assertEquals(
                    "afterThrowingExact java.lang.ClassNotFoundException afterThrowingParentClass java.lang.ClassNotFoundException",
                    s_log
            );
            return;
        }
        fail("should have encounter an exception");
    }

    //-- Test methods
    public int returnInt(int i) {
        return 2 * i;
    }

    public String returnString(String s) {
        return s + s;
    }

    public void throwChecked() throws ClassNotFoundException {
        throw new ClassNotFoundException("checked exception");
    }

    //-- JUnit
    public static void log(String msg) {
        s_log += msg;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(TestBinding.class);
    }
}
