/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class CustomProceedChangeTargetTest extends TestCase {

    static int s_instance = 0;
    int m_me;

    public CustomProceedChangeTargetTest() {
        m_me = ++s_instance;
    }

    public void testPassOtherTarget() {
        s_instance = 0;
        CustomProceedChangeTargetTest one = new CustomProceedChangeTargetTest();//1

        // as an around
        int meOfOne = one.getMe(1);//advised, new instance[2] + 1 -> 3
        assertFalse(meOfOne==one.m_me);
        assertTrue(meOfOne==3);

        String meOfOneAsString = one.getMeAsString(1);//advised, new instance[3] + 1 -> 4
        assertFalse(meOfOneAsString.equals(""+(one.m_me+1)));
        assertTrue("4".equals(meOfOneAsString));
    }

    public void testChangeArg() {
        Foo foo = new Foo();
        assertEquals(1, foo.id);

        // pass it thru, the advice will instantiate a new Foo
        int id = changeArg(foo);
        assertEquals(2, id);
    }

    public int getMe(int i) {
        return m_me + i;
    }

    public String getMeAsString(int i) {
        return "" + (m_me + i);
    }

    public int changeArg(Foo foo) {
        return foo.id;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(CustomProceedChangeTargetTest.class);
    }

    public static class Aspect {

        public static interface CustomJp extends JoinPoint {
            int proceed(CustomProceedChangeTargetTest callee, int arg);
        }

        @Around("execution(int test.CustomProceedChangeTargetTest.getMe(int)) && args(arg) && target(t)")
        public Object around1(CustomJp jp, CustomProceedChangeTargetTest t, int arg) throws Throwable {
            int meOfOther = jp.proceed(new CustomProceedChangeTargetTest(), arg);
            return new Integer(meOfOther);
        }

        public static interface CustomJp2 extends JoinPoint {
            String proceed(CustomProceedChangeTargetTest callee, int arg);
        }

        @Around("execution(String test.CustomProceedChangeTargetTest.getMeAsString(int)) && args(arg) && target(t)")
        public Object around2(CustomJp2 jp, CustomProceedChangeTargetTest t, int arg) throws Throwable {
            String meOfOther = jp.proceed(new CustomProceedChangeTargetTest(), arg);
            return meOfOther;
        }
    }

    public static class Aspect2 {

        public static interface CustomJp3 extends JoinPoint {
            Object proceed(Foo foo);
        }

        // bound in aop.xml
        //@Around("execution(int test.CustomProceedChangeTargetTest.changeArg(test.CustomProceedChangeTargetTest$Foo)) && args(foo)")
        public Object around3(CustomJp3 jp, Foo foo) {
            Foo anotherFoo = new Foo();
            return jp.proceed(anotherFoo);
        }
    }

    static class Foo {
        static int ID = 0;
        int id;
        public Foo() {
            id = ++ID;
        }
    }
}
