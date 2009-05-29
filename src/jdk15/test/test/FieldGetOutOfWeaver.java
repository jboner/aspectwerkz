/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;

import java.io.PrintStream;

import org.codehaus.aspectwerkz.annotation.Before;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class FieldGetOutOfWeaver extends TestCase {

    static String s_log = "";

    public void testSystemGet() {
        s_log = "";
        PrintStream out = System.out;
        out = Foo.out;// match as well
        assertEquals("advice advice ", s_log);
    }

    public void testSystemGetOutsideCode() {
        s_log = "";
        PrintStream out = System.out;
        out = Foo.out;
        assertEquals("", s_log);
    }

    public void testSystemGetTyped() {
        s_log = "";
        PrintStream out = System.out;
        out = Foo.out;
        assertEquals("adviceTyped ", s_log);
    }

    public void testSystemGetPatternedTyped() {
        s_log = "";
        PrintStream out = System.out;
        out = Foo.out;
        assertEquals("advicePatternedTyped ", s_log);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(FieldGetOutOfWeaver.class);
    }

    public static class Foo {
        public static PrintStream out;
    }

    public static class Aspect {

        @Before("get(* out) && withincode(* test.FieldGetOutOfWeaver.testSystemGet(..))")
        void before(StaticJoinPoint sjp) {
            FieldGetOutOfWeaver.s_log += "advice ";
        }

        @Before("get(* java.lang.System.out) && withincode(* test.FieldGetOutOfWeaver.testSystemGetTyped(..))")
        void beforeTyped() {
            FieldGetOutOfWeaver.s_log += "adviceTyped ";
        }

        @Before("get(* java.lang.*.out) && withincode(* test.FieldGetOutOfWeaver.testSystemGetPatternedTyped(..))")
        void beforePatternedTyped() {
            FieldGetOutOfWeaver.s_log += "advicePatternedTyped ";
        }
    }
}
