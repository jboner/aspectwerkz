/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.mixin.perjvm;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.io.Serializable;

import junit.framework.TestCase;
import test.SerialVerUidTest;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class IntroductionTest extends TestCase {

    public void testIntroducedComesFromInterfaces() {
        Class klass = ToBeIntroduced.class.getClass();
        try {
            Method m = klass.getDeclaredMethod("NOT_IN_MIXIN_INTF", new Class[0]);
            fail("should not have introduced : " + m);
        } catch (NoSuchMethodException e) {
            ;//ok
        }
    }

    public void testSerialVer() {
        // a field should have been added
        try {
            Field f = ToBeIntroduced.class.getDeclaredField("serialVersionUID");
        } catch (Throwable t) {
            fail(t.toString());
        }
    }

    public void testMixinInterface() {
        ToBeIntroduced target = new ToBeIntroduced();
        assertTrue(target instanceof Introductions);
    }

    public void testSome() {
        ToBeIntroduced target = new ToBeIntroduced();
        ((Introductions)target).noArgs();
        ToBeIntroduced target2 = new ToBeIntroduced();
        assertEquals(2, ((Introductions)target2).intArg(2));

        // only one mixin instance
        assertEquals(1, MyImpl.s_count);
    }

    public void testParams() {
        assertEquals("v1", MyImpl.s_params.get("p1"));
        assertEquals("v2", MyImpl.s_params.get("p2"));
    }

    //-- junit
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(IntroductionTest.class);
    }
}