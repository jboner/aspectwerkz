/***************************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved. *
 * http://aspectwerkz.codehaus.org *
 * ---------------------------------------------------------------------------------- * The software
 * in this package is published under the terms of the LGPL license * a copy of which has been
 * included with this distribution in the license.txt file. *
 **************************************************************************************************/
package test.bindingsyntax;

import junit.framework.TestCase;

public class AdviceBindingTest extends TestCase {
    public static transient String flow = "";

    public AdviceBindingTest(String s) {
        super(s);
    }

    public String doA(String s) {
        return 'A' + s;
    }

    public String doRA(String s) {
        return 'A' + s;
    }

    public String doB(String s) {
        return 'B' + s;
    }

    public String doRB(String s) {
        return 'B' + s;
    }

    public String doC(String s) {
        return 'C' + s;
    }

    public String doRC(String s) {
        return 'C' + s;
    }

    public String doD(String s) {
        return 'D' + s;
    }

    public String doRD(String s) {
        return 'D' + s;
    }

    public static String doAA(String s) {
        return "AA" + s;
    }

    public static String doBB(String s) {
        return "BB" + s;
    }

    public static String doCC(String s) {
        return "CC" + s;
    }

    public String doDD(String s) {
        return "DD" + s;
    }

    public void testAdviceStack() {
        assertEquals("12Atest", doA("test"));
        assertEquals("12AAtest", doAA("test"));
        assertEquals("21Atest", doRA("test"));
    }

    public void testTwoAdice() {
        assertEquals("12Ctest", doC("test"));
        assertEquals("12CCtest", doCC("test"));
        assertEquals("21Ctest", doRC("test"));
    }

    /**
     * Note: precedence is not the same due to aspect precedence
     */
    public void testTwoAspect() {
        assertEquals("12Dtest", doD("test"));
        assertEquals("12DDtest", doDD("test"));
        assertEquals("21Dtest", doRD("test"));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AdviceBindingTest.class);
    }
}