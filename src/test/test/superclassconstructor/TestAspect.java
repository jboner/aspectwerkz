/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.superclassconstructor;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect
 */
public class TestAspect {
    /**
     * @Expression execution(void test.superclassconstructor.C+.set())
     */
    Pointcut pc;

    /**
     * @Before pc
     */
    public void advice(final JoinPoint joinPoint) throws Throwable {
    }
}