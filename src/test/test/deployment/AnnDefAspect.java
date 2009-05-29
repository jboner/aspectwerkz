/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.deployment;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AnnDefAspect {
    /**
     * @Around execution(void test.deployment.DeployerTest.deployUndeployUsingHandle()) ||
     * execution(void test.deployment.DeployerTest.deployUndeployUsingPreparedPointcut())
     */
    public Object advice(final JoinPoint joinPoint) throws Throwable {
        DeployerTest.log("before ");
        Object result = joinPoint.proceed();
        DeployerTest.log("after ");
        return result;
    }
}
