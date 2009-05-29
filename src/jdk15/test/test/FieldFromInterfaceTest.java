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
package test;

import junit.framework.TestCase;

import java.rmi.AccessException;

import org.codehaus.aspectwerkz.annotation.Before;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class FieldFromInterfaceTest extends TestCase {

    static int COUNT = 0;

    static interface Some {
        // don't use constants since javac will inline them
        long detail = System.currentTimeMillis();
    }

    static class Foo implements Some {

    }

    public void testSome() {
        COUNT = 0;
        Exception e = new Exception("detail");
        AccessException rmi = new AccessException("detail message", e);
        assertTrue(e.equals(rmi.detail));//field advised
        assertEquals(1, COUNT);

        Foo f = new Foo();
        assertTrue(f.detail > 0);//field advised
        assertTrue(Foo.detail > 0);//field advised
        assertEquals(3, COUNT);
    }

    public static class TestAspect {
        @Before("withincode(* test.FieldFromInterfaceTest.test*()) && get(* *.detail)")
        public void before() {
            COUNT++;
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(FieldFromInterfaceTest.class);
    }
}
