/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.deployment.HotDeployedTest;
import test.proxy.DelegationProxyTest;
import test.proxy.SubclassingProxyTest;
import test.priv.PrivateCtorTest;
import test.bugs.interfacesubtypebug.InterfaceSubtypeBug;

/**
 * JDK 5 specific tests.
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AllJdk5Tests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite("All JDK 5 tests");

        suite.addTestSuite(HotDeployedTest.class);
        suite.addTestSuite(CflowBelowTest.class);

        // bug fix tests
        suite.addTestSuite(FieldGetOutOfWeaver.class);
        suite.addTestSuite(InterfaceDefinedMethodTest.class);
        suite.addTestSuite(CtorExecution.class);
        suite.addTestSuite(MixinTest.class);
        suite.addTestSuite(CustomProceedChangeTargetTest.class);
        suite.addTestSuite(PerInstanceSerializationTest.class);
        suite.addTestSuite(QNameTest.class);
        suite.addTestSuite(AfterReturningThrowingTest.class);

        suite.addTestSuite(SubclassingProxyTest.class);
        suite.addTestSuite(DelegationProxyTest.class);

        suite.addTestSuite(PrivateCtorTest.class);
        suite.addTestSuite(FieldFromInterfaceTest.class);

        // jdk14 migrated test for simplicity
        suite.addTestSuite(InterfaceSubtypeBug.class);

        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

}
