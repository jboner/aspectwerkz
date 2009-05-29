/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.deployment;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.transform.inlining.deployer.Deployer;
import org.codehaus.aspectwerkz.transform.inlining.deployer.DeploymentHandle;
import org.codehaus.aspectwerkz.definition.DeploymentScope;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;

/**
 * TODO add more tests, tests that can make things break, evil tests
 * <p/>
 * TODO tests for all pointcut types
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class DeployerTest extends TestCase {
    private static String s_logString = "";

    public DeployerTest(String name) {
        super(name);
    }

    public void testDeployUndeployUsingPreparedPointcut() {
        s_logString = "";

        deployUndeployUsingPreparedPointcut();
        assertEquals("deployUndeployUsingPreparedPointcut ", s_logString);
        s_logString = "";

        SystemDefinition def = SystemDefinitionContainer.getDefinitionFor(
                Thread.currentThread().getContextClassLoader(), "tests"
        );
        DeploymentScope deploymentScope = def.getDeploymentScope("deployUndeployUsingPreparedPointcut");

        Deployer.deploy(AnnDefAspect.class, deploymentScope);

        deployUndeployUsingPreparedPointcut();
        assertEquals("before deployUndeployUsingPreparedPointcut after ", s_logString);
        s_logString = "";

        Deployer.undeploy(AnnDefAspect.class);

        deployUndeployUsingPreparedPointcut();
        assertEquals("deployUndeployUsingPreparedPointcut ", s_logString);
    }

    public void testDeployUndeployUsingHandle() {
        s_logString = "";

        deployUndeployUsingHandle();
        assertEquals("deployUndeployUsingHandle ", s_logString);
        s_logString = "";

        SystemDefinition def = SystemDefinitionContainer.getDefinitionFor(
                Thread.currentThread().getContextClassLoader(), "tests"
        );
        DeploymentScope deploymentScope = def.getDeploymentScope("deployUndeployUsingHandle");
        DeploymentHandle handle = Deployer.deploy(AnnDefAspect.class, deploymentScope);

        deployUndeployUsingHandle();
        assertEquals("before deployUndeployUsingHandle after ", s_logString);
        s_logString = "";

        Deployer.undeploy(handle);

        deployUndeployUsingHandle();
        assertEquals("deployUndeployUsingHandle ", s_logString);
    }

    public void testDeployUndeployUsingXmlDef() {
        s_logString = "";

        deployUndeployUsingXmlDef();
        assertEquals("deployUndeployUsingXmlDef ", s_logString);
        s_logString = "";

        SystemDefinition def = SystemDefinitionContainer.getDefinitionFor(
                Thread.currentThread().getContextClassLoader(), "tests"
        );
        DeploymentScope deploymentScope = def.getDeploymentScope("deployUndeployUsingXmlDef");

        String aspectXmlDef =
                "<aspect class=\"test.deployment.XmlDefAspect\">" +
                "<pointcut name=\"pc\" expression=\"execution(void test.deployment.DeployerTest.deployUndeployUsingXmlDef())\"/>" +
                "<advice name=\"advice\" type=\"around\" bind-to=\"pc\"/>" +
                "</aspect>";
        Deployer.deploy(XmlDefAspect.class, aspectXmlDef, deploymentScope);

        deployUndeployUsingXmlDef();
        assertEquals("before deployUndeployUsingXmlDef after ", s_logString);
        s_logString = "";

        Deployer.undeploy(XmlDefAspect.class);

        deployUndeployUsingXmlDef();
        assertEquals("deployUndeployUsingXmlDef ", s_logString);
    }

    private void deployUndeployUsingHandle() {
        log("deployUndeployUsingHandle ");
    }

    private void deployUndeployUsingPreparedPointcut() {
        log("deployUndeployUsingPreparedPointcut ");
    }

    private void deployUndeployUsingXmlDef() {
        log("deployUndeployUsingXmlDef ");
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(DeployerTest.class);
    }

    public static void log(final String wasHere) {
        s_logString += wasHere;
    }
}
