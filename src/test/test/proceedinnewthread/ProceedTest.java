/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.proceedinnewthread;

import junit.framework.TestCase;


/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ProceedTest extends TestCase {

    public static String LOG = "";

    public void test1() {
        LOG = "";
        adviseMe1();
        assertEquals("advice1Pre adviseMe1 advice1Post ", LOG);
    }

    public void test2() {
        LOG = "";
        adviseMe2();
        assertEquals("advice1Pre advice2Pre adviseMe2 advice2Post advice1Post ", LOG);
    }

    public void test3() {
        LOG = "";
        adviseMe3();
        assertEquals("advice1Pre advice2Pre advice3Pre adviseMe3 advice3Post advice2Post advice1Post ", LOG);
    }

    public void adviseMe1() {
        LOG += "adviseMe1 ";
    }

    public void adviseMe2() {
        LOG += "adviseMe2 ";
    }

    public void adviseMe3() {
        LOG += "adviseMe3 ";
    }

    // -- JUnit
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ProceedTest.class);
    }
}
