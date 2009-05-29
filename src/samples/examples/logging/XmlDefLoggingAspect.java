/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.logging;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MemberSignature;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class XmlDefLoggingAspect {

    private int m_level = 0;

    public Object logMethod(JoinPoint joinPoint) throws Throwable {
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

    public void logBefore(final JoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "BEFORE: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    public void logAfterReturning(final JoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "AFTER RETURNING: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    public void logAfterThrowingRE(final JoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "AFTER THROWING RE: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    public void logAfterThrowingIAE(final JoinPoint joinPoint) throws Throwable {
        MemberSignature signature = (MemberSignature) joinPoint.getSignature();
        System.out.println(
                "AFTER THROWING IAE: "
                + joinPoint.getCalleeClass().getName()
                + "::"
                + signature.getName()
        );
    }

    public void logAfterFinally(final JoinPoint joinPoint) throws Throwable {
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