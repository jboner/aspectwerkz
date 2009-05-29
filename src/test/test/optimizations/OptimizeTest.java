/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.optimizations;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class OptimizeTest extends TestCase {

    private static String s_log = "";

    public static void log(String s) {
        s_log += s + " ";
    }

//    public void testNothing() {
//        s_log = "";
//        IOptimize target = new OptimizeNothing();
//        target.before();
//        target.around();
//        target.beforeAround();
//        target.before(0);
//        target.around(0);
//        assertEquals("before around before around before0 around0 ", s_log);
//    }
//
//    public void testStaticJoinPoint() {
//        s_log = "";
//        IOptimize target = new OptimizeStaticJoinPoint();
//        target.before();
//        target.around();
//        target.beforeAround();
//        target.before(0);
//        target.around(0);
//        assertEquals("beforeSJP-before aroundSJP-around beforeSJP-beforeAround aroundSJP-beforeAround beforeSJP0 aroundSJP0 ", s_log);
//    }
//
//    public void testJoinPoint() {
//        s_log = "";
//        IOptimize target = new OptimizeJoinPoint();
//        target.before();
//        target.around();
//        target.beforeAround();
//        target.before(0);
//        target.around(0);
//        assertEquals(
//                "beforeJP-before-OptimizeJoinPoint-OptimizeTest aroundJP-around-OptimizeJoinPoint-OptimizeTest beforeJP-beforeAround-OptimizeJoinPoint-OptimizeTest aroundJP-beforeAround-OptimizeJoinPoint-OptimizeTest beforeJP0 aroundJP0 ",
//                s_log);
//    }

    public void testRtti() {
        s_log = "";
        IOptimize target = new OptimizeRtti();
        target.before();
        target.around();
        target.beforeAround();
        target.before(0);
        target.around(0);
    }

    public static interface IOptimize {
        public void before();
        public void around();
        public void beforeAround();
        public void before(int arg);
        public void around(int arg);
    }

    public static class OptimizeNothing implements IOptimize {

        public void before() {
        }

        public void around() {
        }

        public void beforeAround() {
        }

        public void before(int arg) {
        }

        public void around(int arg) {
        }
    }

    public static class OptimizeStaticJoinPoint implements IOptimize {

        public void before() {
        }

        public void around() {
        }

        public void beforeAround() {
        }

        public void before(int arg) {
        }

        public void around(int arg) {
        }
    }

    public static class OptimizeJoinPoint implements IOptimize {

        public String toString() {return "OptimizeJoinPoint"; }

        public void before() {
        }

        public void around() {
        }

        public void beforeAround() {
        }

        public void before(int arg) {
        }

        public void around(int arg) {
        }
    }

    public static class OptimizeRtti implements IOptimize {

        public String toString() {return "OptimizeRtti"; }

        public void before() {
        }

        public void around() {
        }

        public void beforeAround() {
        }

        public void before(int arg) {
        }

        public void around(int arg) {
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(OptimizeTest.class);
    }

    public String toString() {return "OptimizeTest";}

}
