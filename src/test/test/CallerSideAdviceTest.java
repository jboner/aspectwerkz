/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class CallerSideAdviceTest extends TestCase {
    private static String s_logString = "";

    public CallerSideAdviceTest(String name) {
        super(name);
    }

    public void testAroundAdvicedMemberMethod() {
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            helper.invokeMemberMethodAround("a", "b");
            helper.invokeMemberMethodAround("a", "b", "c");
            assertEquals("before after before after ", s_logString);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // AW-163: same JIT JP reused
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            helper.invokeMemberMethodAround("a", "b", "c");
            assertEquals("before after ", s_logString);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testAroundAdvicedStaticMethod() {
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            helper.invokeStaticMethodAround();
            assertEquals("before after ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testAroundAdvicedStaticMethodPrimitiveType() {
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            int res = helper.invokeStaticMethodAroundPrimitiveType();
            assertEquals("before after ", s_logString);
            assertEquals(res, 3);
        } catch (Exception e) {
            fail();
        }
    }

    public void testAroundAdvicedMemberMethodPrimitiveType() {
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            int res = helper.invokeMemberMethodAroundPrimitiveType();
            assertEquals("before after ", s_logString);
            assertEquals(res, 2);
        } catch (Exception e) {
            fail();
        }
    }

    public void testAroundAdvicedMemberMethodVoidType() {
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            helper.invokeMemberMethodAroundVoidType();
            assertEquals("before after ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testPreAdvicedMemberMethod() {
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            helper.invokeMemberMethodPre();
            assertEquals("pre1 pre2 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testPostAdvicedMemberMethod() {
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            helper.invokeMemberMethodPost();
            assertEquals("post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testPrePostAdvicedMemberMethod() {
        s_logString = "";
        try {
            CallerSideTestHelper helper = new CallerSideTestHelper();
            helper.invokeMemberMethodPrePost();
            assertEquals("pre1 pre2 post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testPreAdvicedStaticMethod() {
        s_logString = "";
        try {
            CallerSideTestHelper.invokeStaticMethodPre();
            assertEquals("pre1 pre2 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testPostAdvicedStaticMethod() {
        s_logString = "";
        try {
            CallerSideTestHelper.invokeStaticMethodPost();
            assertEquals("post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testPrePostAdvicedStaticMethod() {
        s_logString = "";
        try {
            CallerSideTestHelper.invokeStaticMethodPrePost();
            assertEquals("pre1 pre2 post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(CallerSideAdviceTest.class);
    }

    // ==== methods to test ====
    public static void log(final String wasHere) {
        s_logString += wasHere;
    }

    public void setFieldPreAdviced() {
    }
}