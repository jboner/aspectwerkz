/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.deployment;


import org.codehaus.aspectwerkz.definition.DeploymentScope;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class PreparePointcuts {
    /**
     * @Expression execution(void test.deployment.DeployerTest.deployUndeployUsingHandle())
     */
    DeploymentScope deployUndeployUsingHandle;

    /**
     * @Expression execution(void test.deployment.DeployerTest.deployUndeployUsingPreparedPointcut())
     */
    DeploymentScope deployUndeployUsingPreparedPointcut;

    /**
     * @Expression execution(void test.deployment.DeployerTest.deployUndeployUsingXmlDef())
     */
    DeploymentScope deployUndeployUsingXmlDef;
}