/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.bugs.interfacesubtypebug;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.annotation.Expression;
import org.codehaus.aspectwerkz.annotation.Around;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class TestAspect {
    // ============ Pointcuts ============

    @Expression("execution(* test.bugs.interfacesubtypebug.Intf#.*())")
    Pointcut interfacePC;

    // ============ Advices ============

    @Around("interfacePC")
    public Object advice1(final JoinPoint joinPoint) throws Throwable {
        InterfaceSubtypeBug.LOG += "interface ";
        Object result = joinPoint.proceed();
        InterfaceSubtypeBug.LOG += "interface ";
        return result;
    }
}