/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Set;

import org.codehaus.aspectwerkz.annotation.Before;
import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class InterfaceDefinedMethodTest extends TestCase {

    public static String s_log = "";

    public InterfaceDefinedMethodTest(String s) {
        super(s);
    }

    public InterfaceDefinedMethodTest() {
        SortedSet ss = new TreeSet();
        ss.add("foo"); // Warning, add is in super interface
        ss.first(); // Ok, first is in SortedSet

        try {
            Set s = ss;
            s.add("bar"); // Ok, add is in Set
            throw new NullPointerException("fake");
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * When visiting the bytecode of this method, the classInfo must lookup in the class + intf
     * hierarchy
     */
    public void testInterfaceDefinedMethod() {
        s_log = "";
        SortedSet ss = new TreeSet();
        ss.add("foo"); // Warning, add is in super interface
        ss.first(); // Ok, first is in SortedSet

        try {
            Set s = ss;
            s.add("bar"); // Ok, add is in Set
            throw new NullPointerException("fake");
        } catch (NullPointerException npe) {
            ;
        }
        assertEquals("advice advice advice advice advice advice advice ", s_log);
    }

    public void testWithinCtor() {
        s_log = "";
        InterfaceDefinedMethodTest me = new InterfaceDefinedMethodTest();
        assertEquals("around around around around around around around ", s_log);
    }

    public void testWithinNot() {
        s_log = "";
        withinNot();
        assertEquals("withincode withincode withincode ", s_log);
    }

    private void withinNot() {
        InterfaceDefinedMethodTest me = new InterfaceDefinedMethodTest("ignore");
        subWithinNot();
    }

    private void subWithinNot() {
        ;
    }


    public static class Aspect {

        @Before("withincode(* test.InterfaceDefinedMethodTest.testInterfaceDefinedMethod(..))")
        public void before(StaticJoinPoint sjp) {
            s_log += "advice ";
        }

        @Around("withincode(test.InterfaceDefinedMethodTest.new())")
        public Object around(StaticJoinPoint sjp) throws Throwable {
            s_log += "around ";
            return sjp.proceed();
        }

        @Before("cflow(within(test.InterfaceDefinedMethodTest) && call(* test.InterfaceDefinedMethodTest.withinNot()))" +
                "&& !withincode(* test.InterfaceDefinedMethodTest.withinNot())" +
                "&& within(test.InterfaceDefinedMethodTest)")
        public void neverCalled(StaticJoinPoint sjp) {
            s_log += "withincode ";
            //System.out.println(sjp.getType() + " " + sjp.getSignature());
        }
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(InterfaceDefinedMethodTest.class);
    }
}
