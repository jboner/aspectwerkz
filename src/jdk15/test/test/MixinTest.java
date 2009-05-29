/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.annotation.Mixin;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class MixinTest extends TestCase {

    @AnnotatedMethod
    public void iHaveAnnotatedMethod() {}

    public void testIntroduced() {
        assertTrue(this instanceof Counter);
        assertEquals(1, ((Counter)this).getCounter());
        assertEquals(2, ((Counter)this).getCounter());

        // we have perInstance
        MixinTest another = new MixinTest();
        assertEquals(1, ((Counter)another).getCounter());
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(MixinTest.class);
    }

    static interface Counter {
        int getCounter();
    }

    @Mixin("hasmethod(@test.MixinTest$AnnotatedMethod * test.Mixin*.*(..))")
    public static class CounterImpl implements Counter {
        int i = 0;
        public int getCounter() {
            return ++i;
        }
    }

    public static @interface AnnotatedMethod {}
}
