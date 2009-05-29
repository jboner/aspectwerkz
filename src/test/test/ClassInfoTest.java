/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.reflect.impl.asm.AsmClassInfo;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaClassInfo;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.reflect.ReflectHelper;
import org.codehaus.aspectwerkz.reflect.FieldInfo;
import org.codehaus.aspectwerkz.transform.inlining.AsmNullAdapter;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;

import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.SortedSet;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class ClassInfoTest extends TestCase {

    public void method(int i, long l, String[] s, int[][] i2) {
        // some code to influence local variables
        for (int a = 0; a < 3; a++) {
            for (long b = 0; b < 2; b++) {
                ;
            }
        }
    }

    public static long[][][] smethod(long[][] l2, ClassInfoTest test, ClassInfoTest[][] test2) {
        return null;
    }

    public void testMethodInfo() {
        ClassInfo ci = AsmClassInfo.getClassInfo("test.ClassInfoTest", ClassLoader.getSystemClassLoader());
        MethodInfo[] methods = ci.getMethods();

        assertTrue(methods.length >= 2);

        for (int i = 0; i < methods.length; i++) {
            MethodInfo method = methods[i];
            if (method.getName().equals("method")) {
                checkMethod(method);
            } else if (method.getName().equals("smethod")) {
                checkStaticMethod(method);
            }
        }
    }

    private void checkMethod(MethodInfo method) {
        try {
            assertEquals("method", method.getName());
            assertTrue(!Modifier.isStatic(method.getModifiers()));

            assertEquals("i", method.getParameterNames()[0]);
            assertEquals("l", method.getParameterNames()[1]);
            assertEquals("s", method.getParameterNames()[2]);
            assertEquals("i2", method.getParameterNames()[3]);

            assertEquals("int", method.getParameterTypes()[0].getName());
            assertEquals("long", method.getParameterTypes()[1].getName());
            assertEquals("java.lang.String[]", method.getParameterTypes()[2].getName());
            assertEquals("int[][]", method.getParameterTypes()[3].getName());

            assertEquals("void", method.getReturnType().getName());
        } catch (Throwable t) {
            fail(t.toString());
        }
    }

    private void checkStaticMethod(MethodInfo method) {
        try {
            assertEquals("smethod", method.getName());
            assertTrue(Modifier.isStatic(method.getModifiers()));

            assertEquals("l2", method.getParameterNames()[0]);
            assertEquals("test", method.getParameterNames()[1]);
            assertEquals("test2", method.getParameterNames()[2]);

            assertEquals("long[][]", method.getParameterTypes()[0].getName());
            assertEquals("[[J", method.getParameterTypes()[0].getSignature());
            assertEquals("test.ClassInfoTest", method.getParameterTypes()[1].getName());
            assertEquals("Ltest/ClassInfoTest;", method.getParameterTypes()[1].getSignature());
            assertEquals("test.ClassInfoTest[][]", method.getParameterTypes()[2].getName());
            assertEquals("[[Ltest/ClassInfoTest;", method.getParameterTypes()[2].getSignature());

            assertEquals("long[][][]", method.getReturnType().getName());
            assertEquals("[[[J", method.getReturnType().getSignature());
        } catch (Throwable t) {
            fail(t.toString());
        }
    }

    public void testGetMethods() throws Exception {
        Class intfClazz = SortedSet.class;
        Method inIntfMethod = intfClazz.getMethod("first", new Class[0]);
        assertNotNull("first() is declared in java.util.SortedSet", inIntfMethod);

        Method inSuperMethod = intfClazz.getMethod("isEmpty", new Class[0]);
        assertNotNull("isEmpty() is declared in java.util.Set", inSuperMethod);

        int inIntfMethodHash = ReflectHelper.calculateHash(inIntfMethod);
        int inSuperMethodHash = ReflectHelper.calculateHash(inSuperMethod);

        ClassInfo clazzInfo = AsmClassInfo.getClassInfo("java.util.SortedSet", ClassInfoTest.class.getClassLoader());
        assertNotNull("java.util.SortedSet should be found", clazzInfo);

        MethodInfo inIntfMethodInfo = clazzInfo.getMethod(inIntfMethodHash);
        assertNotNull("first() method info should be found directly", inIntfMethodInfo);

        MethodInfo inSuperMethodInfo = clazzInfo.getMethod(inSuperMethodHash);
//        assertNotNull("isEmpty() method info from super interface", inSuperMethodInfo);

        ClassInfo clazzInfo2 = JavaClassInfo.getClassInfo(java.util.SortedSet.class);
        assertNotNull("java.util.SortedSet should be found", clazzInfo);

        MethodInfo inIntfMethodInfo2 = clazzInfo2.getMethod(inIntfMethodHash);
        assertNotNull("first() method info should be found directly", inIntfMethodInfo2);

        MethodInfo inSuperMethodInfo2 = clazzInfo2.getMethod(inSuperMethodHash);
        assertNotNull("isEmpty() method info from super interface", inSuperMethodInfo2);
    }

    //-- JUnit
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ClassInfoTest.class);
    }

}
