/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.annotation;

import junit.framework.TestCase;

import java.util.List;
import java.lang.reflect.Method;

import org.codehaus.backport175.reader.Annotations;
import org.codehaus.backport175.reader.Annotation;

/**
 * Note: when using untyped annotation, then the first space character(s) in the value part will be
 * resumed to only one space (untyped     type -> untyped type), due to BP doclet handling.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 *
 * @Complex(i=3, ls={1l,2l,6L},  klass=java.lang.String.class)
 * @ComplexNested(nesteds={@Simple(val="foo"), @Simple(val="bar")})
 */
public class AnnotationCTest extends TestCase {

    /**
     * @BeforeAction some untype that starts with Before
     */
    static class A1 {}
    /**
     * @BeforeAction ("other   untyped")
     */
    static class A2 {}
    /**
     * @BeforeAction("yet another untyped")
     */
    static class A3 {}
    /**
     * @packaged.BeforeAction
     */
    static class A4 {}
    /**
     * @Void
     * @Simple()
     * @DefaultString("hello")
     */
    static class B1{}
    /**
     * @Void()
     * @Simple(val="foo", s="bar")
     * @packaged.DefaultString("hello")
     */
    static class B2{}
    /**
     * @Untyped
     */
    static class C1 {}
    /**
     * @Untyped "hello"
     */
    static class C2 {}
    /**
     * @Untyped ("hello2")
     */
    static class C3 {}
    /**
     * @Untyped "(hello) - see the space here !"
     */
    static class C4 {}
    /**
     * @Untyped("preserved hello")
     */
    static class C5 {}

    public void testClassAnnotation() {
        // Void
        Annotation at = Annotations.getAnnotation(AnnotationParserTest.VoidTyped.class, B1.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.VoidTyped);
        at = Annotations.getAnnotation(AnnotationParserTest.VoidTyped.class, B2.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.VoidTyped);

        // Simple
        at = Annotations.getAnnotation(AnnotationParserTest.Simple.class, B1.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Simple);
        at = Annotations.getAnnotation(AnnotationParserTest.Simple.class, B2.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Simple);
        assertEquals("foo", ((AnnotationParserTest.Simple)at).val());
        assertEquals("bar", ((AnnotationParserTest.Simple)at).s());

        // BeforeAction
        at = Annotations.getAnnotation(BeforeAction.class, A1.class);
        assertNotNull(at);
        assertTrue(at instanceof BeforeAction);
        assertEquals("some untype that starts with Before", ((BeforeAction)at).value());
        at = Annotations.getAnnotation(BeforeAction.class, A2.class);
        assertNotNull(at);
        assertTrue(at instanceof BeforeAction);
        assertEquals("other untyped", ((BeforeAction)at).value());//See space munging here... BP issue.
        at = Annotations.getAnnotation(BeforeAction.class, A3.class);
        assertNotNull(at);
        assertTrue(at instanceof BeforeAction);
        assertEquals("yet another untyped", ((BeforeAction)at).value());
        at = Annotations.getAnnotation(PackagedBeforeAction.class, A4.class);
        assertNotNull(at);
        assertTrue(at instanceof PackagedBeforeAction);
        assertEquals(null, ((PackagedBeforeAction)at).value());

        // DefaultString
        at = Annotations.getAnnotation(AnnotationParserTest.DefaultString.class, B1.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.DefaultString);
        assertEquals("hello", ((AnnotationParserTest.DefaultString)at).value());
        at = Annotations.getAnnotation(AnnotationParserTest.PackagedDefaultString.class, B2.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.PackagedDefaultString);
        assertEquals("hello", ((AnnotationParserTest.PackagedDefaultString)at).value());


        // Complex
        at = Annotations.getAnnotation(AnnotationParserTest.Complex.class, this.getClass());
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Complex);
        assertEquals(String.class, ((AnnotationParserTest.Complex)at).klass());
        assertEquals(3, ((AnnotationParserTest.Complex)at).i());
        assertEquals(3, ((AnnotationParserTest.Complex)at).ls().length);
        assertEquals(1l, ((AnnotationParserTest.Complex)at).ls()[0]);
        assertEquals(2l, ((AnnotationParserTest.Complex)at).ls()[1]);
        assertEquals(6L, ((AnnotationParserTest.Complex)at).ls()[2]);

        // Untyped
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, C1.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals(null, ((AnnotationParserTest.Untyped)at).value());
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, C2.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals("hello", ((AnnotationParserTest.Untyped)at).value());
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, C3.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals("hello2", ((AnnotationParserTest.Untyped)at).value());
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, C4.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals("(hello) - see the space here !", ((AnnotationParserTest.Untyped)at).value());
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, C5.class);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals("preserved hello", ((AnnotationParserTest.Untyped)at).value());
    }

    /**
     * @Void
     * @Simple()
     * @DefaultString("hello")
     * @Complex(i=3, ls={1l,2l,6L},  klass=java.lang.String.class)
     */
    void mA1() {}
    /**
     * @Void()
     * @Simple(val="foo", s="bar")
     */
    void mA2() {}
    /**
     * @Untyped
     */
    void mB1() {}
    /**
     * @Untyped "hello"
     */
    void mB2() {}
    /**
     * @Untyped "hello"
     */
    void mB3() {}
    /**
     * @Untyped "(hello) - see the space here !"
     */
    void mB4() {}

    public void testMethodAnnotation() throws Throwable {
        Class me = test.annotation.AnnotationCTest.class;
        Method mA1 = me.getDeclaredMethod("mA1", new Class[0]);
        Method mA2 = me.getDeclaredMethod("mA2", new Class[0]);
        Method mB1 = me.getDeclaredMethod("mB1", new Class[0]);
        Method mB2 = me.getDeclaredMethod("mB2", new Class[0]);
        Method mB3 = me.getDeclaredMethod("mB3", new Class[0]);
        Method mB4 = me.getDeclaredMethod("mB4", new Class[0]);

        // Void
        Annotation at = Annotations.getAnnotation(AnnotationParserTest.VoidTyped.class, mA1);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.VoidTyped);
        at = Annotations.getAnnotation(AnnotationParserTest.VoidTyped.class, mA2);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.VoidTyped);

        // Simple
        at = Annotations.getAnnotation(AnnotationParserTest.Simple.class, mA1);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Simple);
        at = Annotations.getAnnotation(AnnotationParserTest.Simple.class, mA2);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Simple);
        assertEquals("foo", ((AnnotationParserTest.Simple)at).val());
        assertEquals("bar", ((AnnotationParserTest.Simple)at).s());

        // DefaultString
        at = Annotations.getAnnotation(AnnotationParserTest.DefaultString.class, mA1);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.DefaultString);
        assertEquals("hello", ((AnnotationParserTest.DefaultString)at).value());

        // Complex
        at = Annotations.getAnnotation(AnnotationParserTest.Complex.class, mA1);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Complex);
        assertEquals(String.class, ((AnnotationParserTest.Complex)at).klass());
        assertEquals(3, ((AnnotationParserTest.Complex)at).i());
        assertEquals(3, ((AnnotationParserTest.Complex)at).ls().length);
        assertEquals(1l, ((AnnotationParserTest.Complex)at).ls()[0]);
        assertEquals(2l, ((AnnotationParserTest.Complex)at).ls()[1]);
        assertEquals(6L, ((AnnotationParserTest.Complex)at).ls()[2]);

        // Untyped
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, mB1);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals(null, ((AnnotationParserTest.Untyped)at).value());
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, mB2);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals("hello", ((AnnotationParserTest.Untyped)at).value());
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, mB3);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals("hello", ((AnnotationParserTest.Untyped)at).value());
        at = Annotations.getAnnotation(AnnotationParserTest.Untyped.class, mB4);
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.Untyped);
        assertEquals("(hello) - see the space here !", ((AnnotationParserTest.Untyped)at).value());
    }

    public void testNestedAnnotation() throws Throwable {
        // ComplexNested
        Annotation at = Annotations.getAnnotation(AnnotationParserTest.ComplexNested.class, this.getClass());
        assertNotNull(at);
        assertTrue(at instanceof AnnotationParserTest.ComplexNested);
        assertEquals(2, ((AnnotationParserTest.ComplexNested)at).nesteds().length);
        AnnotationParserTest.ComplexNested ann = (AnnotationParserTest.ComplexNested)at;
        AnnotationParserTest.Simple ann1 = ann.nesteds()[0];
        AnnotationParserTest.Simple ann2 = ann.nesteds()[1];
        String ann12 = ann1.val()+"."+ann2.val();
        if (ann12.equals("foo.bar")) {
            ;//ok
        } else {
            fail("Annotation is not correct " + ann.toString());
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AnnotationCTest.class);
    }
}
