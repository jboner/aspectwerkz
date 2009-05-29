/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.customproceed;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class CustomProceedTest extends TestCase {
    private static String LOG = "";

    public static void log(String msg) {
        LOG += msg;
    }

    public void testIntArg() {
        LOG = "";
        setInt(-1);
        assertEquals("around1 -1 1 ", LOG);
    }

    public void testLongArg() {
        LOG = "";
        setLong(-2);
        assertEquals("around2 -2 2 ", LOG);
    }

    public void testStringArg() {
        LOG = "";
        setString("testing");
        assertEquals("around3 testing gnitset ", LOG);
    }

    public void testMiscArgs1() {
        LOG = "";
        setMisc1(-12345, "testing");
        assertEquals("around4 -12345 testing 12345 gnitset ", LOG);
    }

    public void testMiscArgs2() {
        LOG = "";
        int[][] arr = new int[1][1];
        arr[0][0] = -123;
        setMisc2(-12345, "testing", arr);
        assertEquals("around5 -12345 testing -123 12345 gnitset 123 ", LOG);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(CustomProceedTest.class);
    }

    public void setInt(int i) {
        log(new Integer(i).toString());
        log(" ");
    }

    public void setLong(long l) {
        log(new Long(l).toString());
        log(" ");
    }

    public void setString(String s) {
        log(s);
        log(" ");
    }

    public void setMisc1(long i, String s) {
        log(new Long(i).toString());
        log(" ");
        log(s);
        log(" ");
    }

    public void setMisc2(long i, String s, int[][] matrix) {
        log(new Long(i).toString());
        log(" ");
        log(s);
        log(" ");
        log(new Integer(matrix[0][0]).toString());
        log(" ");
    }
}
