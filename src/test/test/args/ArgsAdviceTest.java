/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.args;

import test.Loggable;
import junit.framework.TestCase;

/**
 * Test for args() syntax and pointcut / advice with signatures.
 * Some tests to cover XML syntax.
 * TODO: test for CALL pc and ctor exe/call jp
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class ArgsAdviceTest extends TestCase implements Loggable {

    private String m_logString = "";
    private static String s_logString = "";

    // used for ctor call and static field set, else we use jp.getTarget()
    public static void logStatic(String s) {
        s_logString += s;
    }

    // execution(* m(..)) && args(i)
    public void testSingleAndDotDot() {
        m_logString = "";
        singleAndDotDot(1);
        assertEquals("before 1 invocation ", m_logString);
    }

    // all bounded :(long l, String s, int[][] matrix)
    public void testWithArray() {
        m_logString = "";
        int[][] iis = new int[][]{{1, 2}, {3}};
        withArray(1L, "h", iis);
        assertEquals("before 1 h 1-2-3- invocation ", m_logString);
    }

    //args(String, String, long)
    public void testMatchAll() {
        m_logString = "";
        matchAll("a0", "a1", 2);
        assertEquals("before before1 invocation after1 after ", m_logString);
        m_logString = "";
        matchAllXML("a0", "a1", 2);
        assertEquals("before before1 invocation after1 after ", m_logString);
    }

    //args(..)
    public void testMatchAllWithWildcard() {
        m_logString = "";
        matchAllWithWildcard("a0", "a1", 2);
        assertEquals("before before1 invocation after1 after ", m_logString);
    }

    //args(s, ..)
    public void testGetFirst() {
        m_logString = "";
        getFirst("a0", "a1", 2);
        assertEquals("before a0 before1 a0 invocation after1 a0 after a0 ", m_logString);
        m_logString = "";
        getFirstXML("a0", "a1", 2);
        assertEquals("before a0 before1 a0 invocation after1 a0 after a0 ", m_logString);

    }

    //args(s, ..) as anonymous pointcut
    public void testGetFirstAnonymous() {
        m_logString = "";
        getFirstAnonymous("a0", "a1", 2);
        assertEquals("before a0 before1 a0 invocation after1 a0 after a0 ", m_logString);
        //TODO (low prio): anonymous pc with args() is not supported in XML - see notes in aop.xml
//        m_logString = "";
//        getFirstAnonymousXML("a0", "a1", 2);
//        assertEquals("before a0 before1 a0 invocation after1 a0 after a0 ", m_logString);
    }

    //args(String, s, long) and increment it
    public void testChangeArg() {
        m_logString = "";
        changeArg("a0", new StringBuffer("a1"), 2);
        // beware: using String won't work as for regular Java behavior
        assertEquals("before a1x before1 a1xx invocation after1 a1xxx after a1xxxx ", m_logString);
    }

    // args(s0, s1, long), with Pc signature reversed
    public void testOrderChangedInPointcutSignature() {
        m_logString = "";
        orderChangedInPointcutSignature("a0", "a1", 2);
        assertEquals("before a1 a0 before1 a1 a0 invocation after1 a1 a0 after a1 a0 ", m_logString);
    }

    // args(s0, s1, long), with Advice signature reversed
    public void testOrderChangedInAdviceSignature() {
        m_logString = "";
        orderChangedInAdviceSignature("a0", "a1", 2);
        assertEquals("before a1 a0 before1 a1 a0 invocation after1 a1 a0 after a1 a0 ", m_logString);
    }

    // args(s0, s1, long), with Pointcut and Advice signature reversed
    public void testOrderChangedInPointcutAndAdviceSignature() {
        m_logString = "";
        orderChangedInPointcutAndAdviceSignature("a0", "a1", 2);
        assertEquals("before a0 a1 before1 a0 a1 invocation after1 a0 a1 after a0 a1 ", m_logString);
        m_logString = "";
        orderChangedInPointcutAndAdviceSignatureXML("a0", "a1", null);
        assertEquals("before a0 a1 before1 a0 a1 invocation after1 a0 a1 after a0 a1 ", m_logString);
    }

    //-- method call pointcuts

    //args(l<long>, s<String[]>)
    public void testCallGetFirstAndSecond() {
        m_logString = "";
        callGetFirstAndSecond(1L, new String[]{"s0", "s1"});
        assertEquals("before 1 s0,s1 before1 1 s0,s1 invocation after1 1 s0,s1 after 1 s0,s1 ", m_logString);
        m_logString = "";
        callGetFirstAndSecondXML(1L, new String[]{"s0", "s1"}, null);
        assertEquals("before 1 s0,s1 before1 1 s0,s1 invocation after1 1 s0,s1 after 1 s0,s1 ", m_logString);
    }

    //-- ctor execution
    //args(s)
    public void testCtorExecutionGetFirst() {
        //FIXME
        // looks like a bug for ctor executiona and inner class inheritance
        // see CtorLoggable and CtorExecution<init>, that has the call to CtorLoggable<init> corrupted
        m_logString = "";
        CtorExecution target = new CtorExecution("s");
        assertEquals("before s before1 s invocation after1 s after s ", m_logString);
        m_logString = "";
        CtorExecutionXML target2 = new CtorExecutionXML("s");
        assertEquals("before s before1 s invocation after1 s after s ", m_logString);
    }

    //-- ctor call
    //args(s)
    public void testCtorCallGetFirst() {
        s_logString = "";
        CtorCall target = new CtorCall("s");
        assertEquals("before s before1 s invocation after1 s after s ", s_logString);
        s_logString = "";
        CtorCallXML target2 = new CtorCallXML("s");
        assertEquals("before s before1 s invocation after1 s after s ", s_logString);
    }

    //-- field set
    private String m_field;
    private static String s_field;

    public String getField() {
        return m_field;
    }

    public static String getStaticField() {
        return s_field;
    }

    //arg(s)
    public void testFieldSetArg() {
        try {
            m_logString = "";
            m_field = "s";
            assertEquals("before null,s before1 null,s after1 s,changed after s,s ", m_logString);
            s_logString = "";
            s_field = "s";
            assertEquals("before null,s before1 null,s after1 s,changed after s,s ", s_logString);
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    //-- Implementation methods
    public void log(String s) {
        m_logString += s;
    }

    public void singleAndDotDot(int i) {
        log("invocation ");
    }

    public void withArray(long l, String s, int[][] matrix) {
        log("invocation ");
    }

    public void matchAll(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void matchAllXML(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void matchAllWithWildcard(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void getFirst(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void getFirstXML(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void getFirstAnonymous(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void getFirstAnonymousXML(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void changeArg(String a0, StringBuffer a1, long a2) {
        log("invocation ");
    }

    public void orderChangedInPointcutSignature(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void orderChangedInAdviceSignature(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void orderChangedInPointcutAndAdviceSignature(String a0, String a1, long a2) {
        log("invocation ");
    }

    public void orderChangedInPointcutAndAdviceSignatureXML(String a0, String a1, Object[] a2) {
        log("invocation ");
    }

    //-- method call
    public void callGetFirstAndSecond(long l, String[] s) {
        log("invocation ");
    }

    public void callGetFirstAndSecondXML(long l, String[] s, String[] ignore) {
        log("invocation ");
    }

    class CtorLoggable implements Loggable {
        public CtorLoggable() {
        }

        public void log(String s) {
            m_logString += s;
        }
    }

    //-- ctor execution
    class CtorExecution extends CtorLoggable {
        public CtorExecution(String s) {
            this.log("invocation ");
        }
    }

    class CtorExecutionXML extends CtorLoggable {
        public CtorExecutionXML(String s) {
            this.log("invocation ");
        }
    }

    //-- ctor call
    class CtorCall extends CtorLoggable {
        public CtorCall(String s) {
            logStatic("invocation ");
        }
    }

    class CtorCallXML extends CtorLoggable {
        public CtorCallXML(String s) {
            logStatic("invocation ");
        }
    }


    //-- JUnit
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ArgsAdviceTest.class);
    }

}
