/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.pointcutexpression;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class PointcutExpressionTest extends TestCase {
    public static String s_logString = "";

    public PointcutExpressionTest(String name) {
        super(name);
    }

    public void test_OR() {
        s_logString = "";
        B();
        assertEquals("before1 B after1 ", s_logString);
        s_logString = "";
        C();
        assertEquals("before1 C after1 ", s_logString);
    }

    public void test_AND_NEG() {
        s_logString = "";
        D();
        assertEquals("before1 D after1 ", s_logString);
        s_logString = "";
        E();
        assertEquals("E ", s_logString);
    }

    public void test_OR_AND() {
        s_logString = "";
        F();
        assertEquals("F ", s_logString);
        s_logString = "";
        G();
        assertEquals("G ", s_logString);
    }

    public void test_OR_AND_GENERIC() {
        s_logString = "";
        I();
        assertEquals("before1 I after1 ", s_logString);
        s_logString = "";
        J();
        assertEquals("before1 J after1 ", s_logString);
    }

    public void test_COMPLEX() {
        s_logString = "";
        K();
        assertEquals("K ", s_logString);
        s_logString = "";
        L();
        assertEquals("L ", s_logString);
        s_logString = "";
        M();
        assertEquals("M ", s_logString);
        s_logString = "";
        N();
        assertEquals("before1 N after1 ", s_logString);
    }

    public void test_SIMPLE() {
        s_logString = "";
        O();
        assertEquals("before1 O after1 ", s_logString);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(PointcutExpressionTest.class);
    }

    // ==== methods to test ====
    public static void log(final String wasHere) {
        s_logString += wasHere;
    }

    public void A() {
        log("A ");
    }

    public void B() {
        log("B ");
    }

    public void C() {
        log("C ");
    }

    public void D() {
        log("D ");
    }

    public void E() {
        log("E ");
    }

    public void F() {
        log("F ");
    }

    public void G() {
        log("G ");
    }

    public void H() {
        log("H ");
    }

    public void I() {
        log("I ");
    }

    public void J() {
        log("J ");
    }

    public void K() {
        log("K ");
    }

    public void L() {
        log("L ");
    }

    public void M() {
        log("M ");
    }

    public void N() {
        log("N ");
    }

    public void O() {
        log("O ");
    }
}