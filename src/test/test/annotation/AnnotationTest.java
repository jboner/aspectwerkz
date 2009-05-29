/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.annotation;

import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.io.Serializable;

import org.codehaus.backport175.reader.Annotations;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AnnotationTest extends TestCase {
    private static String s_logString = "";

    /**
     * @AnnotationPrivateField
     */
    private int privateField;

    /**
     * @AnnotationProtectedField
     */
    protected int protectedField;

    /**
     * @AnnotationPublicField
     */
    public int publicField;

    /**
     * @AnnotationPackagePrivateField
     */
    int packagePrivateField;

    public AnnotationTest() {
    }

    public AnnotationTest(String name) {
        super(name);
    }

    public void testPrivateMethod() {
        s_logString = "";
        privateMethod();
        assertEquals("call execution invocation execution call ", s_logString);
    }

    public void testProtectedMethod() {
        s_logString = "";
        protectedMethod();
        assertEquals("call execution invocation execution call ", s_logString);
    }

    public void testPackagePrivateMethod() {
        s_logString = "";
        packagePrivateMethod();
        assertEquals("call execution invocation execution call ", s_logString);
    }

    public void testPublicMethod() {
        s_logString = "";
        publicMethod();
        assertEquals("call execution execution2 invocation execution2 execution call ", s_logString);
    }

    public void testSetPublicField() {
        s_logString = "";
        publicField = 0;
        assertEquals("set set ", s_logString);
    }

    public void testSetPrivateField() {
        s_logString = "";
        privateField = 0;
        assertEquals("set set ", s_logString);
    }

    public void testSetProtectedField() {
        s_logString = "";
        protectedField = 0;
        assertEquals("set set ", s_logString);
    }

    public void testSetPackagePrivateField() {
        s_logString = "";
        packagePrivateField = 0;
        assertEquals("set set ", s_logString);
    }

    public void testGetPublicField() {
        s_logString = "";
        int i = publicField;
        assertEquals("get get ", s_logString);
    }

    public void testGetPrivateField() {
        s_logString = "";
        int i = privateField;
        assertEquals("get get ", s_logString);
    }

    public void testGetProtectedField() {
        s_logString = "";
        int i = protectedField;
        assertEquals("get get ", s_logString);
    }

    public void testGetPackagePrivateField() {
        s_logString = "";
        int i = packagePrivateField;
        assertEquals("get get ", s_logString);
    }

    public void testBootstrapCLClassAnnotation() throws Throwable {
        Method concat = String.class.getMethod("concat", new Class[]{String.class});
        try {
            Annotations.getAnnotation(Serializable.class, String.class);
            Annotations.getAnnotation(Serializable.class, concat);
        } catch (Throwable t) {
            fail(t.toString());
        }
    }



    //-------

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AnnotationTest.class);
    }

    // ==== methods to test ====
    public static void log(final String wasHere) {
        s_logString += wasHere;
    }

    /**
     * @AnnotationPrivateMethod
     */
    private void privateMethod() {
        log("invocation ");
    }

    /**
     * @AnnotationProtectedMethod
     */
    protected void protectedMethod() {
        log("invocation ");
    }

    /**
     * @AnnotationPublicMethod
     * @AnnotationPublicMethod2
     */
    public void publicMethod() {
        log("invocation ");
    }

    /**
     * @AnnotationPackagePrivateMethod
     */
    void packagePrivateMethod() {
        log("invocation ");
    }
}