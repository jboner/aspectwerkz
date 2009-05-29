/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import junit.framework.TestCase;

/**
 * Test for complex CFLOW
 * See AW-226
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class ComplexCFlowTest extends TestCase {

    private static String s_logString = "";

//    //FIXME: see the aspect, pc is deactivated - see AW-251
//    public void testComplexNotCFlow_1() {
//        s_logString = "";
//        method1();
//        assertEquals(s_logString, " method1 4-!2-!3-Advice method4");
//    }

    public void testComplexNotCFlow_2() {
        s_logString = "";
        method2();
        assertEquals(s_logString, " method2 method4");
    }

    public void testComplexNotCFlow_3() {
        s_logString = "";
        method3();
        assertEquals(s_logString, " method3 method4");
    }

    //--- Aspect

    public static class Aspect {

        /**
         * FIXME: this expression leads to match all at cflow early filtering.
         * <p/>
         * XXBefore execution(* test.ComplexCFlowTest.method4(..)) AND within(test.ComplexCFlowTest)
         * AND !cflow(call(* test.ComplexCFlowTest.method2(..)) AND within(test.ComplexCFlowTest))
         * AND !cflow(call(* test.ComplexCFlowTest.method3(..)) AND within(test.ComplexCFlowTest))
         */
        public void method4NotIn2Or3Advice(JoinPoint joinPoint) {
            s_logString += " 4-!2-!3-Advice";
        }
    }

    //--- JUnit

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ComplexCFlowTest.class);
    }

    //--- Method to test

    public static void method1() {
        s_logString += " method1";
        method4();
    }

    public static void method2() {
        s_logString += " method2";
        method4();
    }

    public static void method3() {
        s_logString += " method3";
        method4();
    }

    public static void method4() {
        s_logString += " method4";
    }


}
