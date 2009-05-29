/*******************************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *   Alexandre Vasseur         initial implementation
 *******************************************************************************/
package test.priv;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.annotation.Before;
import test.priv.sub.PrivateTarget;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class PrivateCtorTest extends TestCase {

    public void testPrivateCtorCall() {
        PrivateTarget i = PrivateTarget.s_singleton;
        assertTrue(i!=null);
    }

    public static class TestAspect {

        @Before("within(test.priv.sub.PrivateTarget) && call(*.new(..))")
        public void before() {
            int i = 0;
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(PrivateCtorTest.class);
    }
}
