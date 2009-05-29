/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.logging;

import org.codehaus.aspectwerkz.joinpoint.MemberSignature;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public abstract class AbstractLoggingAspect {

    private int m_level = 0;

    /**
     * @Around methodsToLog
     */
    public Object logMethod(StaticJoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        indent();
        System.out.println(
                "--> "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
        m_level++;
        final Object result = joinPoint.proceed();
        m_level--;
        indent();
        System.out.println(
                "<-- "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
        return result;
    }

    /**
     * @Before methodsToLog
     */
    public void logBefore(final StaticJoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "BEFORE: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    /**
     * @AfterReturning(type="java.lang.String", pointcut="methodsToLog")
     */
    public void logAfterReturning(final StaticJoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "AFTER RETURNING: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    /**
     * @AfterThrowing(type="java.lang.RuntimeException", pointcut="methodsToLog")
     */
    public void logAfterThrowingRE(final StaticJoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "AFTER THROWING RE: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    /**
     * @AfterThrowing(type="java.lang.IllegalArgumentException", pointcut="methodsToLog")
     */
    public void logAfterThrowingIAE(final StaticJoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "AFTER THROWING IAE: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    /**
     * @AfterFinally methodsToLog
     */
    public void logAfterFinally(final StaticJoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "AFTER FINALLY: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    private void indent() {
        for (int i = 0; i < m_level; i++) {
            System.out.print("  ");
        }
    }
}