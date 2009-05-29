/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.logging;

import org.codehaus.aspectwerkz.transform.inlining.deployer.Deployer;
import org.codehaus.aspectwerkz.transform.inlining.deployer.DeploymentHandle;
import org.codehaus.aspectwerkz.definition.DeploymentScope;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Target {

    /**
     * log level=1 flt=5.8F iconstant=org.codehaus.aspectwerkz.DeploymentModel.PER_CLASS
     */
    private int m_counter1;

    /**
     * log level=1 iconstant=org.codehaus.aspectwerkz.DeploymentModel.PER_THREAD
     */
    private int m_counter2;

    public int getCounter() {
        return m_counter1;
    }

    public void increment() {
        m_counter2 = m_counter2 + 1;
    }

    /**
     * log level=0
     * sconstant=org.codehaus.aspectwerkz.transform.TransformationConstants.ASPECTWERKZ_PREFIX
     */
    public static int toLog1(int i) {
        System.out.println("Target.toLog1()");
        new Target().toLog2(
                new String[]{
                    "parameter"
                }
        );
        return 1;
    }

    /**
     * log level=3 sarr={"Hello","World", "Jonas's car"}
     */
    public java.lang.String[] toLog2(java.lang.String[] arg) {
        System.out.println("Target.toLog2()");
        new Target().toLog3();
//        throw new RuntimeException();
        return null;
    }

    /**
     * log level=4 darr={4.5D,8.98665D,0.00000342}
     */
    public Object toLog3() {
        System.out.println("Target.toLog3()");
        return "result";
    }

    public static void main(String[] args) {

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("---- deploy/undeploy using explicit prepared pointcut ----");
        System.out.println("-----------------------------------------------------------------------------------");

        run();
        SystemDefinition def = SystemDefinitionContainer.getDefinitionFor(
                Thread.currentThread().getContextClassLoader(), "samples"
        );
        DeploymentScope deploymentScope = def.getDeploymentScope("prepareMethodsToLog");

        Deployer.deploy(LoggingAspect.class, deploymentScope);
        run();
        Deployer.undeploy(LoggingAspect.class);
        run();


        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("---- deploy/undeploy using deployment handle ----");
        System.out.println("-----------------------------------------------------------------------------------");

        run();
        DeploymentHandle handle2 = Deployer.deploy(LoggingAspect.class);
        run();
        Deployer.undeploy(handle2);
        run();


        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("---- deploy using XML def and undeploy using handle ----");
        System.out.println("-----------------------------------------------------------------------------------");

        run();
        String aspectXmlDef = "<aspect class=\"examples.logging.XmlDefLoggingAspect\"><pointcut name=\"methodsToLog\" expression=\"execution(* examples.logging.Target.toLog*(..))\"/><advice name=\"logMethod\" type=\"around\" bind-to=\"methodsToLog\"/><advice name=\"logBefore\" type=\"before\" bind-to=\"methodsToLog\"/></aspect>";
        Deployer.deploy(XmlDefLoggingAspect.class, aspectXmlDef);
        run();
        Deployer.undeploy(XmlDefLoggingAspect.class);
        run();

    }

    private static void run() {
        try {
            System.out.println("Target.main");
            Target.toLog1(3);
            Target target = new Target();
            target.increment();
            target.getCounter();

            TargetOther.toLog1(new int[]{1, 2, 3}, null, null, 0);
        } catch (Throwable e) {
            System.out.println("The runtime exception went thru: " + e.toString());
            e.printStackTrace();
        }
    }

    public static class TargetOther {

        public static int[] toLog1(int i[], String[] a, String b, int c) {
            System.out.println("TargetOther.toLog1()");
            return i;
        }
    }

    public void dummy() {

    }
}