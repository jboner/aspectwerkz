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
 * test "pc AND (cf OR cf2)"
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér</a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class CFlowTest extends TestCase implements Loggable {
    private String m_logString = "";

    public CFlowTest(String name) {
        super(name);
    }

    public void testCallWithinCFlow() {
        m_logString = "";
        step1(); //will have cflow and will call step2()
        assertEquals("step1 advice-before step2 advice-after ", m_logString);
    }

    public void testCallWithinCFlowAnonymous() {
        m_logString = "";
        step1Anonymous(); //will have cflow and will call step2()
        assertEquals(
                "step1Anonymous advice-beforeAnonymous step2Anonymous advice-afterAnonymous ",
                m_logString
        );
    }

    public void testCallWithinCFlowWithinCflow() {
        m_logString = "";
        step1_A(); //will have cflow and will call step1_B that will call step2_B()
        assertEquals("step1_A step1_B advice-before2 step2_B advice-after2 ", m_logString);
    }

    public void testCallOutsideCFlow() {
        m_logString = "";
        step2();
        assertEquals("step2 ", m_logString);
    }

    public void testCallWithinCFlow_B() {
        m_logString = "";
        step1_B(); //will have cflow and will call step2_B() but is NOT in step1_A cflow
        assertEquals("step1_B step2_B ", m_logString);
    }

    public void testCallOutsideCFlowAnonymous() {
        m_logString = "";
        step2Anonymous();
        assertEquals("step2Anonymous ", m_logString);
    }

    public void testCflowOnMySelfForPrecedence() {
        m_logString = "";
        cflowOnMyself();
        assertEquals("cflowOnMyself advice-cflowOnMyself ", m_logString);
    }

//    //FIXME: see the aspect, pc is deactivated - see AW-251
//    public void testCallWithinNotCFlow_C() {
//        m_logString = "";
//        step1_C(); //will have "NOT cflow" and will call step2_C
//        assertEquals("step1_C step2_C ", m_logString);
//        m_logString = "";
//        step2_C(); //should be advised since not in step1_C cflow
//        assertEquals("advice-beforeC step2_C advice-afterC ", m_logString);
//    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(CFlowTest.class);
    }

    // ==== methods to test ====
    public void log(final String wasHere) {
        m_logString += wasHere;
    }

    public void step1() {
        log("step1 ");
        step2();
    }

    public void step1Anonymous() {
        log("step1Anonymous ");
        step2Anonymous();
    }

    public void step1_B() {
        log("step1_B ");
        step2_B();
    }

    public void step1_A() {
        log("step1_A ");
        step1_B();
    }

    public void step2() {
        log("step2 ");
    }

    public void step2Anonymous() {
        log("step2Anonymous ");
    }

    public void step2_B() {
        log("step2_B ");
    }

    public void step1_C() {
        log("step1_C ");
        step2_C();
    }

    public void step2_C() {
        log("step2_C ");
    }

    public void cflowOnMyself() {
        log("cflowOnMyself ");
    }
}