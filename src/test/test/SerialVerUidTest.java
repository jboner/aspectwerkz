/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.codehaus.aspectwerkz.transform.inlining.weaver.SerialVersionUidVisitor;

/**
 * Test for the SerialVerionUid computation.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class SerialVerUidTest extends TestCase implements Serializable {
    static {
        System.gc();
    }

    public Object[] someMethod() {
        return null;
    }

    protected static final int someField = 32;

    public void testSerialVerUid() throws Throwable {
        long UID = SerialVersionUidVisitor.calculateSerialVersionUID(SerialVerUidTest.class);
        //System.out.println(UID);

        Field f = SerialVerUidTest.class.getDeclaredField("serialVersionUID");
        long uid = ((Long)f.get(null)).longValue();
        //System.out.println(uid);

        // a bit odd but.. 
        try {
            Class.forName("java.lang.annotation.Annotation");
            assertEquals(7614081430767231713L, UID);//java 5
        } catch (ClassNotFoundException e) {
            assertEquals(-6289975506796941698L, UID);//java 1.4 (mthClass$() synthetic method)
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(SerialVerUidTest.class);
    }
}
