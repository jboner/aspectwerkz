/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.abstractclass;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AbstractClassTest extends TestCase {
    public AbstractClassTest(String name) {
        super(name);
    }

    public void testInstrumentedAbstractMemberMethodInvocation() {
        try {
            TestAspect.s_log = "";
            AbstractTarget target = new AbstractTargetImpl();
            target.method1();
            assertEquals("method1method1XX", TestAspect.s_log);
        } catch (Exception e) {
            fail();
        }
    }

    public void testInstrumentedAbstractStaticMethodInvocation() {
        try {
            TestAspect.s_log = "";
            AbstractTarget target = new AbstractTargetImpl();
            target.method2();
            assertEquals("method2method2XX", TestAspect.s_log);
        } catch (Exception e) {
            fail();
        }
    }

    public void testInstrumentedAbstractImplementedMethodInvocation() {
        try {
            TestAspect.s_log = "";
            AbstractTarget target = new AbstractTargetImpl();
            target.method3();
            assertEquals("method3XX", TestAspect.s_log);
        } catch (Exception e) {
            fail();
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AbstractClassTest.class);
    }
}