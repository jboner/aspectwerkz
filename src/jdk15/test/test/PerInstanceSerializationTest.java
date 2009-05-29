/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import org.codehaus.aspectwerkz.annotation.Aspect;
import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.aspect.management.Aspects;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class PerInstanceSerializationTest extends TestCase implements Serializable {

    static StringBuffer s_log = new StringBuffer();
    static void log(String s) {
        s_log.append(s).append(" ");
    }

    void doStuff() {
        log("doStuff");
    }

    public void testSerializeTarget() {
        s_log = new StringBuffer();
        PerInstanceSerializationTest instance = new PerInstanceSerializationTest();
        try {instance.doStuff();// advised, new aspect gets created
        } catch (Throwable t) {t.printStackTrace();}
        Object theAspect = Aspects.aspectOf(TestAspect.class, instance);

        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream("instance.ser"));
            out.writeObject(instance);
            instance = null;
            out.close();

            File file = new File("instance.ser");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            PerInstanceSerializationTest unserInstance = (PerInstanceSerializationTest) in.readObject();
            in.close();
            Object theAspect2 = Aspects.aspectOf(TestAspect.class, unserInstance);
            unserInstance.doStuff();

            // the perInstance aspect does not get serialized but another instance is made available
            assertFalse(theAspect.hashCode() == theAspect2.hashCode());
            assertEquals("newAspect around doStuff newAspect around doStuff ", s_log.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(PerInstanceSerializationTest.class);
    }

    @Aspect("perInstance")
    public static class TestAspect {

        public TestAspect() {
            log("newAspect");
        }

        @Around("execution(void test.PerInstanceSerializationTest.doStuff(..))")
        public Object around(StaticJoinPoint joinpoint) throws Throwable {
            log("around");
            return joinpoint.proceed();
        }
    }

}
