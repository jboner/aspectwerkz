/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.annotation.AfterReturning;
import org.codehaus.aspectwerkz.annotation.AfterThrowing;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AfterReturningThrowingTest extends TestCase {

    static StringBuffer s_log = new StringBuffer();
    static void log(String s) {
        s_log.append(s).append(" ");
    }

    public int credit() {
        log("credit");
        return 0;
    }

    public int debit(boolean fail) throws NoMoreCreditException {
        log("debit");
        if (fail) {
            throw new NoMoreCreditException();
        }
        return 0;
    }

    public void greet() {
       if (1==1) {
           throw new RuntimeException("Just kidding!");
       }
    }

    public void testGreet() {
        s_log = new StringBuffer();
        try {
            greet();
        } catch (Exception e) {
            ;
        } finally {
            assertEquals("beforeGreeting afterGreetingException ", s_log.toString());
        }
    }

    public void testAfterRet() {
        s_log = new StringBuffer();
        credit();
        try {
            debit(false);
        } catch (NoMoreCreditException e) {
            fail(e.toString());
        }
        assertEquals("credit AOP.credit debit AOP.debit ", s_log.toString());
    }

    public void testAfterThrow() {
        s_log = new StringBuffer();
        try {
            debit(true);
            fail("should throw");
        } catch (NoMoreCreditException e) {
            ;
        }
        assertEquals("debit AOP.debit ", s_log.toString());
    }

    private static class NoMoreCreditException extends Exception {}

    public static class Aspect {

        @AfterReturning("execution(* test.AfterReturningThrowingTest.credit()) || execution(* test.AfterReturningThrowingTest.debit(..))")
        public void afterReturning(JoinPoint jp) {
            log("AOP."+jp.getSignature().getName());
        }

        @AfterThrowing(pointcut = "execution(* test.AfterReturningThrowingTest.debit(..))",
                       type = "test.AfterReturningThrowingTest$NoMoreCreditException")
        public void afterThrowing(JoinPoint jp) {
            log("AOP."+jp.getSignature().getName());
        }
    }

    public static class XmlAspect {

        public void beforeGreeting(JoinPoint joinPoint) {
            log("beforeGreeting");
        }

        public void afterGreeting(JoinPoint joinPoint) {
            log("afterGreeting");
        }

        public void afterGreeting(JoinPoint joinPoint, java.lang.Exception ex) {
            log("afterGreetingException");
        }
    }



    //--- JUnit

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AfterReturningThrowingTest.class);
    }

}
