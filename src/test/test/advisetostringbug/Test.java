/**
 * ***********************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * ************************************************************************************
 */
package test.advisetostringbug;

import junit.framework.TestCase;


public class Test extends TestCase {

    public void test1() {
        A a = new AImpl();
        B b = (B) a;
        b.toString(true, "");
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
}