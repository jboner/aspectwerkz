/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.AspectContext;
import org.codehaus.aspectwerkz.aspect.management.Aspects;
import org.codehaus.aspectwerkz.aspect.management.NoAspectBoundException;
import org.codehaus.aspectwerkz.annotation.Before;
import org.codehaus.aspectwerkz.annotation.Aspect;

import java.io.PrintStream;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class QNameTest extends TestCase {

    static StringBuffer s_log = new StringBuffer();
    static void log(String s) {
        s_log.append(s).append(" ");
    }

    void doStuff() {
        log("doStuff");
    }

    void doGC1() {
        log("doGC1");
        System.gc();
        System.gc();
        doGC2();
    }

    void doGC2() {
        log("doGC2");
    }

    void doPerJVM() {
        log("doPerJVM");
        PrintStream fieldGet = System.out;
    }

    void doPerClass() {
        log("doPerClass");
        PrintStream fieldGet = System.out;
    }

    void doPerInstance() {
        log("doPerInstance");
        PrintStream fieldGet = System.out;
    }

    public void testQNames() {
        s_log = new StringBuffer();
        doStuff();
        // note: aspect instantiation happens first due to perJVM and JP clinit
        assertEquals("1 jdk5test/Aspect_1 2 jdk5test/Aspect_2 before-1 before-2 doStuff ", s_log.toString());

        TestAspect a = (TestAspect)Aspects.aspectOf("jdk5test/Aspect_1");
        assertEquals("1", a.p);

        TestAspect b = (TestAspect)Aspects.aspectOf("jdk5test/Aspect_2");
        assertEquals("2", b.p);

        // in that case there is several aspects for Aspect.class
        // so fails
        try {
            TestAspect c = (TestAspect)Aspects.aspectOf(TestAspect.class);
            fail("should fail");
        } catch (NoAspectBoundException t) {
            ;
        }
    }

    public void testPerX() {
        s_log = new StringBuffer();
        doPerJVM();
        assertEquals("doPerJVM before ", s_log.toString());

        s_log = new StringBuffer();
        doPerClass();
        assertEquals("doPerClass before ", s_log.toString());

        s_log = new StringBuffer();
        doPerInstance();
        assertEquals("doPerInstance before ", s_log.toString());
    }

    public void testPerJVMAndGC() {
        s_log = new StringBuffer();
        doGC1();
        assertEquals("AspectGC before1 doGC1 before2 doGC2 ", s_log.toString());
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(QNameTest.class);
    }

    public static class TestAspect {

        String p;

        public TestAspect(AspectContext ctx) {
            p = ctx.getParameter("p");
            log(p);
            log(ctx.getAspectDefinition().getQualifiedName());
        }

        @Before("execution(* test.QNameTest.doStuff())")
        public void before() {
            log("before-"+p);
        }
    }

    public static class AspectJVM {
        @Before("withincode(* test.QNameTest.doPerJVM()) && get(* java.lang.System.out)")
        public void before() {
            log("before");
        }
    }

    @Aspect("perClass")
    public static class AspectClass {
        @Before("withincode(* test.QNameTest.doPerClass()) && get(* java.lang.System.out)")
        public void before() {
            log("before");
        }
    }

    @Aspect("perInstance")
    public static class AspectInstance {
        @Before("withincode(* test.QNameTest.doPerInstance()) && get(* java.lang.System.out)")
        public void before() {
            log("before");
        }
    }

    public static class AspectGC {

        public AspectGC() {
            log("AspectGC");
        }

        @Before("execution(* test.QNameTest.doGC1())")
        public void before1() {
            log("before1");
        }
        @Before("execution(* test.QNameTest.doGC2())")
        public void before2() {
            log("before2");
        }
    }

}
