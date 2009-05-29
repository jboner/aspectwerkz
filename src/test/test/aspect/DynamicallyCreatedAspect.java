/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aspect;

import test.Loggable;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perJVM
 */
public class DynamicallyCreatedAspect {
    // ============ Pointcuts ============

    /**
     * @Expression execution(* test.DynamicDeploymentTest.createAspectTestMethod(..))
     */
    Pointcut pc1;

    // ============ Advices ============

    /**
     * @Around pc1
     */
    public Object advice1(final JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("beforeNew ");
        final Object result = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("afterNew ");
        return result;
    }
}