/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.reflection;

import junit.framework.TestCase;

/**
 * The advice used here reverse the sign of the integer returned by the incr(..) methods. Each
 * incr(..) method return the argument incremented of 1 (or -1 if arg is negative). Child is
 * overriding a method defined in Super but still does call it. Child is used with a dual pointcut,
 * defined both in super method and overrided method. Child2 is used in the case of a single
 * pointcut defined in super method. <p/>For AW-90 same tests are done for static methods
 */
public class ReflectionTest extends TestCase {
    public void testSinglePointcutOnSuperClassWithOverridedMethodNonDelegating() {
        OtherChild2 c = new OtherChild2();
        assertEquals(2, c.incr(1));

        // advice non applied since method is overrided and poincut is NOT hierarchical
        Super2 s = new Super2();
        assertEquals(-2, s.incr(1));

        // advice bounded
    }

    public void testStaticSinglePointcutOnSuperClassWithOverridedMethodNonDelegating() {
        assertEquals(2, OtherChild2.incrStatic(1));

        // advice non applied since method is overrided and poincut is NOT hierarchical
        assertEquals(-2, Super2.incrStatic(1));

        // advice bounded
    }

    public void testSinglePointcutOnSuperClassWithOverridedMethodDelegating() {
        Child2 c = new Child2();
        assertEquals(-3, c.incr(1));
    }

    public void testStaticSinglePointcutOnSuperClassWithOverridedMethodDelegating() {
        assertEquals(-3, Child2.incrStatic(1));
    }

    public void testDualPointcutWithOverridedMethodNonDelegating() {
        OtherChild c = new OtherChild();
        assertEquals(-2, c.incr(1));
    }

    public void testStaticDualPointcutWithOverridedMethodNonDelegating() {
        assertEquals(-2, OtherChild.incrStatic(1));
    }

    public void testDualPointcutWithOverridedMethodDelegating() {
        Child c = new Child();
        assertEquals(+3, c.incr(1));
    }

    public void testStaticDualPointcutWithOverridedMethodDelegating() {
        assertEquals(+3, Child.incrStatic(1));
    }

    public void testDollar() {
        Child c = new Child();
        assertEquals(-1, c.do$2(1));
    }

    public void testReflectionCall() {
        Child c = new Child();
        assertEquals(+3, c.reflectionCallIncr(1));
    }


    // -- JUnit
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ReflectionTest.class);
    }
}