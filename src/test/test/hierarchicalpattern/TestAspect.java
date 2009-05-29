/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.hierarchicalpattern;

import test.Loggable;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect
 */
public class TestAspect {
    /**
     * @Expression execution(* test.hierarchicalpattern.DummyInterface1+.declaringType1(..))
     */
    Pointcut pc1;

    /**
     * @Expression execution(* test.hierarchicalpattern.DummyInterface2+.declaringType2(..))
     */
    Pointcut pc2;

    /**
     * @Expression execution(test.hierarchicalpattern.DummyInterface2+
     * test.hierarchicalpattern.HierachicalPatternTest.returnType*(..))
     */
    Pointcut pc3;

    /**
     * @Expression execution(*
            * test.hierarchicalpattern.HierachicalPatternTest.parameterTypes(test.hierarchicalpattern.DummyInterface1+,
            * test.hierarchicalpattern.DummyInterface2+))
     */
    Pointcut pc4;

    /**
     * @Around pc1 || pc2 || pc3 || pc4
     */
    public Object advice(final JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 ");
        final Object result = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 ");
        return result;
    }
}