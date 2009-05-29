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

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class HotDeployedTest extends TestCase {

    static int HIT = 0;

    public void target() {
        HIT++;
    }

    public void testDeployment() {
        // is deployed from beginning
        target();
        assertEquals(1, HIT);
        assertEquals(1, HotdeployableAspect.HIT);

        // undeploy
        Deployer.undeploy(HotdeployableAspect.class);

        target();
        assertEquals(2, HIT);
        assertEquals(1, HotdeployableAspect.HIT);

        // redeploy
        Deployer.deploy(HotdeployableAspect.class);
        target();
        assertEquals(3, HIT);
        assertEquals(2, HotdeployableAspect.HIT);
    }




    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(HotDeployedTest.class);
    }

}
