/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aopc;

import junit.framework.TestCase;

import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * TODO rewrite test.aopc.* with ASM or using an already builded jar with the small appp deployed
 * several time in difft CL to test system defs and namespaces.
 *
 * Note: does not work behing WeavingCL. Use a real online mode <p/>
 * java -Xrunaspectwerkz -Xdebug -Xbootclasspath/a:lib\aspectwerkz-core-1.0.jar ...
 * <p/>
 * The CallablePrototype class is renamed and defined as a deployed application class in a child classloader
 * with its own META-INF/aop.xml file.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class AspectSystemTest extends TestCase {
    public void testDoubleHierarchyMethodExecution() {
        // VM system classpath level classes
        Callable cvm = new CallablePrototype();
        cvm.methodAround();
        cvm.debug();
        assertEquals("methodAround ", cvm.getLogString());

        // appserver like classloader, with its own aop.xml file
        // the aop.xml file contains one aspect in the VM system classpath
        ClassLoader myCL = new URLClassLoader(
                new URL[]{ClassCreator.getPathFor(Callable.class.getResource("META-INF/aop.xml"))},
                ClassLoader.getSystemClassLoader()
        );
        Callable cas = (Callable) ClassCreator.createInstance(
                "test.aopc.CallableAppServer",
                CallablePrototype.class,
                myCL
        );
        cas.methodAround();
        cas.debug();
        assertEquals(
                "system/asCL/test.aopc.BaseAspect.beforeAround "
                + "methodAround "
                + "system/asCL/test.aopc.BaseAspect.afterAround ",
                cas.getLogString()
        );

        // deployed app A
        // the aop.xml file is REusing VM system classpath aspect and is defining one of its own as well, with 2 systems
        // we are defining the aspect on the fly in an intermediate CL without aop.xml
        ClassLoader mySubCLAAspect = new URLClassLoader(new URL[]{}, myCL);
        ClassCreator.createClass("test.aopc.a.Aspect", BaseAspect.class, mySubCLAAspect);
        ClassLoader mySubCLA = new URLClassLoader(
                new URL[]{ClassCreator.getPathFor(Callable.class.getResource("a/META-INF/aop.xml"))}, mySubCLAAspect
        );
        //ClassCreator.createClass("test.aopc.a.Aspect", BaseAspect.class, mySubCLA);
        Callable ca = (Callable) ClassCreator.createInstance("test.aopc.a.Callee", CallablePrototype.class, mySubCLA);
        ca.methodAround();
        ca.debug();
        assertEquals(
                "system/asCL/test.aopc.BaseAspect.beforeAround "
                + "system/subCL/a1/subCLAspect.beforeAround "
                + "system/subCL/a2/subCLAspect.beforeAround "
                + "methodAround "
                + "system/subCL/a2/subCLAspect.afterAround "
                + "system/subCL/a1/subCLAspect.afterAround "
                + "system/asCL/test.aopc.BaseAspect.afterAround ", ca.getLogString()
        );

        // deployed app B
        // no aop.xml
        ClassLoader mySubCLB = new URLClassLoader(new URL[]{}, myCL);
        Callable cb = (Callable) ClassCreator.createInstance("test.aopc.b.Callee", CallablePrototype.class, mySubCLB);
        cb.methodAround();
        cb.debug();
        assertEquals(
                "system/asCL/test.aopc.BaseAspect.beforeAround "
                + "methodAround "
                + "system/asCL/test.aopc.BaseAspect.afterAround ",
                cb.getLogString()
        );
    }

    // ------------------------------------------------
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AspectSystemTest.class);
    }
}