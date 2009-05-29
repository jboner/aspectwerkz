/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.staticfield;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Test case for AW-92 for collection field altered
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class CollectionFieldTest extends TestCase {
    public static String s_log = "";

    private static Collection s_field = new ArrayList();

    private Collection m_field = new ArrayList();

    //-- member field --//
    public void testCollectionFieldOutsideStaticContext() {
        s_log = "";
        alterFieldOutsideStaticContext();
        assertEquals("MyPreAdvice2 ", s_log);
    }

    public void testCollectionFieldInsideStaticContext() {
        s_log = "";
        alterFieldInsideStaticContext(this);
        assertEquals("MyPreAdvice2 ", s_log);
    }

    public void testGetCollectionFieldOusideStaticContext() {
        s_log = "";
        getFieldOutsideStaticContext();
        assertEquals("MyPostAdvice2 MyPreAdvice2 ", s_log);
    }

    public void testGetCollectionFieldInsideStaticContext() {
        s_log = "";
        getFieldInsideStaticContext(this);
        assertEquals("MyPostAdvice2 MyPreAdvice2 ", s_log);
    }

    //-- static field --//
    public void testStaticCollectionFieldOutsideStaticContext() {
        s_log = "";
        alterStaticFieldOutsideStaticContext();
        assertEquals("MyPreAdvice1 ", s_log);
    }

    public void testStaticCollectionFieldInsideStaticContext() {
        s_log = "";
        alterStaticFieldInsideStaticContext();
        assertEquals("MyPreAdvice1 ", s_log);
    }

    public void testGetStaticCollectionFieldInsideStaticContext() {
        s_log = "";
        getStaticFieldInsideStaticContext();
        assertEquals("MyPostAdvice1 MyPreAdvice1 ", s_log);
    }

    public void testGetStaticCollectionFieldOutsideStaticContext() {
        s_log = "";
        getStaticFieldOutsideStaticContext();
        assertEquals("MyPostAdvice1 MyPreAdvice1 ", s_log);
    }

    // -- methods for member collection field -- //
    public void alterFieldOutsideStaticContext() {
        m_field.clear();
    }

    public static void alterFieldInsideStaticContext(CollectionFieldTest myself) {
        myself.m_field.clear();
    }

    public void getFieldOutsideStaticContext() {
        Collection ref = m_field;
        m_field = new ArrayList();
    }

    public static void getFieldInsideStaticContext(CollectionFieldTest myself) {
        Collection ref = myself.m_field;
        myself.m_field = new ArrayList();
    }

    // -- method for static member collection -- //
    public void alterStaticFieldOutsideStaticContext() {
        s_field.clear();
    }

    public static void alterStaticFieldInsideStaticContext() {
        s_field.clear();
    }

    public void getStaticFieldOutsideStaticContext() {
        Collection ref = s_field;
        s_field = new ArrayList();
    }

    public static void getStaticFieldInsideStaticContext() {
        Collection ref = s_field;
        s_field = new ArrayList();
    }

    //-- check the bytecode created FYI. That's fun here. --//
    public void showComplexUsage() {
        int local = 0;
        while (m_field.remove(null)) {
            local++;
        }
    }

    //-- junit hooks --//
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(CollectionFieldTest.class);
    }
}