/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.deployment;

import org.codehaus.aspectwerkz.definition.DeploymentScope;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.annotation.Expression;
import org.codehaus.aspectwerkz.annotation.Before;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class HotdeployableAspect {

    static int HIT = 0;

    @Expression("execution(void test.deployment.HotDeployedTest.target())")
    Pointcut target;

    @Before("target")
    void before() {
        HIT++;
    }

}
