/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.abstractclass;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect
 */
public class TestAspect {

    static String s_log;

    /**
     * @Expression execution(* test.abstractclass.AbstractTarget.*(..))
     */
    Pointcut pc;

    /**
     * @Around pc
     */
    public Object advice(final JoinPoint joinPoint) throws Throwable {
        s_log += joinPoint.getSignature().getName();//method name
        return joinPoint.proceed();
    }

    /**
     * @Around execution(* test.abstractclass.AbstractTarget+.*(..))
     */
    public Object adviceOnAbstractMethod(final JoinPoint joinPoint) throws Throwable {
        s_log += joinPoint.getSignature().getName()+"XX";//method name + XX
        return joinPoint.proceed();
    }
}