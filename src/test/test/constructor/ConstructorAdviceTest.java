/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.constructor;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ConstructorAdviceTest extends TestCase {
    private static String s_logCall = "";

    private static String s_logExecution = "";

    public ConstructorAdviceTest() {
    }

    public ConstructorAdviceTest(String name) {
        super(name);
    }

    public void testCallAroundAdvice() {
        s_logCall = "";
        TestAroundAdvice test = new TestAroundAdvice(1L, new Object(), new String[]{});
        assertEquals("beforeCall init afterCall ", s_logCall);
        assertNotNull(test);
    }

    public void testCallBeforeAdvice() {
        s_logCall = "";
        TestBeforeAdvice test = new TestBeforeAdvice();
        assertEquals("preCall init ", s_logCall);
        assertNotNull(test);
    }

    public void testCallAfterAdvice() {
        s_logCall = "";
        TestAfterAdvice test = new TestAfterAdvice("test");
        assertEquals("test postCall ", s_logCall);
        assertNotNull(test);
    }

    public void testCallBeforeAfterAdvice() {
        s_logCall = "";
        TestBeforeAfterAdvice test = new TestBeforeAfterAdvice(
                new String[]{
                    "test"
                }
        );
        assertEquals("preCall test postCall ", s_logCall);
        assertNotNull(test);
    }

    public void testCallReturnFalseType() {
        s_logCall = "";
        TestReturnFalseType test = null;
        try {
            test = new TestReturnFalseType();
        } catch (ClassCastException e) {
            return;
        }
        fail("this point should not have been reached a class cast exception should have been thrown");
    }

    public void testExecutionAroundAdvice() {
        s_logExecution = "";
        TestAroundAdvice test = new TestAroundAdvice(1L, new Object(), new String[]{});
        assertEquals("beforeExecution init afterExecution ", s_logExecution);
        assertNotNull(test);
        assertTrue(test instanceof TestAroundAdvice);
    }

    public void testExecutionBeforeAdvice() {
        s_logExecution = "";
        TestBeforeAdvice test = new TestBeforeAdvice();
        assertEquals("preExecution init ", s_logExecution);
        assertNotNull(test);
        assertTrue(test instanceof TestBeforeAdvice);
    }

    public void testExecutionAfterAdvice() {
        s_logExecution = "";
        TestAfterAdvice test = new TestAfterAdvice("test");
        assertEquals("init postExecution ", s_logExecution);
        assertNotNull(test);
        assertTrue(test instanceof TestAfterAdvice);
    }

    public void testExecutionBeforeAfterAdvice() {
        s_logExecution = "";
        TestBeforeAfterAdvice test = new TestBeforeAfterAdvice(new String[]{"test"});
        assertEquals("preExecution init postExecution ", s_logExecution);
        assertNotNull(test);
        assertTrue(test instanceof TestBeforeAfterAdvice);
    }

    public void testExecutionReturnFalseType() {
        s_logExecution = "";
        TestReturnFalseType test = null;
        test = new TestReturnFalseType();
        if (!test.m_updatedByAdvice) {
            fail("should have been updated by advice");
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ConstructorAdviceTest.class);
    }

    public static void logCall(final String wasHere) {
        s_logCall += wasHere;
    }

    public static void logExecution(final String wasHere) {
        s_logExecution += wasHere;
    }
}