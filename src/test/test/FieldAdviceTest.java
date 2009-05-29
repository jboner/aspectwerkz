/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;

import java.io.PrintStream;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class FieldAdviceTest extends TestCase {
    private static String s_logString = "";

    private static long s_setStaticFieldAroundAdviced = 0L;

    private static int s_setStaticFieldPreAdviced = 0;

    private static String s_setStaticFieldPostAdviced = "string";

    private static double s_setStaticFieldPrePostAdviced = 0.000D;

    private static long s_getStaticFieldAroundAdviced = 1L;

    private static int s_getStaticFieldPreAdviced = 1;

    private static String s_getStaticFieldPostAdviced = "string";

    private static double s_getStaticFieldPrePostAdviced = 1.1111D;

    private long m_setFieldAroundAdviced = 0L;

    private int m_setFieldAroundAdvicedWithNullAdvice = 0;

    private String m_setFieldAroundAdvicedObjectWithNullAdvice = new String("0");

    private String m_setFieldAroundAdvicedObjectWithAPI = new String("0");

    private int m_setFieldAroundAdvicedWithAPI = 0;

    private int m_setFieldPreAdviced = 0;

    private String m_setFieldPostAdviced = "string";

    private double m_setFieldPrePostAdviced = 0.000D;

    private long m_getFieldAroundAdviced = 1L;

    private String m_getFieldAroundAdvicedWithNullAdvice = "string";

    private double m_getFieldPreAdviced = 1.0000D;

    private int m_getFieldPostAdviced = 1;

    private int m_getFieldPrePostAdviced = 1;

    public FieldAdviceTest() {
    }

    public FieldAdviceTest(String name) {
        super(name);
    }

    public void testSetMemberFieldAroundAdviced() {
        s_logString = "";
        try {
            setFieldAroundAdviced();
            assertEquals("before after ", s_logString);
            assertEquals(187, m_setFieldAroundAdviced);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetMemberFieldAroundAdvicedWithNullAdvice() {
        s_logString = "";
        try {
            setFieldAroundAdvicedWithNullAdvice();
            assertEquals("before after ", s_logString);

            //CAUTION: null advice for @Set leave the assigned value
            //The advice return value is ignored
            assertEquals(187, m_setFieldAroundAdvicedWithNullAdvice);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetMemberFieldAroundAdvicedObjectWithNullAdvice() {
        s_logString = "";
        try {
            setFieldAroundAdvicedObjectWithNullAdvice();
            assertEquals("before after ", s_logString);

            //CAUTION: null advice for @Set leave the assigned value
            //The advice return value is ignored
            assertEquals("1", m_setFieldAroundAdvicedObjectWithNullAdvice);
        } catch (Exception e) {
            fail();
        }
    }

    //FIXME - activate when proceed(args) will be supported 

//    public void testSetMemberFieldAroundAdvicedObjectWithAPI() {
//        s_logString = "";
//        try {
//            setFieldAroundAdvicedObjectWithAPI();
//            assertEquals("before after ", s_logString);
//
//            //The advice is using the Signature API to alter the assigned value
//            assertEquals("byAdvice", m_setFieldAroundAdvicedObjectWithAPI);
//        } catch (Exception e) {
//            fail();
//        }
//    }
//
//    public void testSetMemberFieldAroundAdvicedWithAPI() {
//        s_logString = "";
//        try {
//            setFieldAroundAdvicedWithAPI();
//            assertEquals("before after ", s_logString);
//
//            //The advice is using the Signature API to alter the assigned value
//            assertEquals(3, m_setFieldAroundAdvicedWithAPI);
//        } catch (Exception e) {
//            fail();
//        }
//    }

    public void testGetMemberFieldAroundAdviced() {
        s_logString = "";
        try {
            long i = getFieldAroundAdviced(); // int default value
            assertEquals("before after ", s_logString);
            assertEquals(1L, i);
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetMemberFieldAroundAdvicedWithNullAdvice() {
        s_logString = "";
        try {
            String i = getFieldAroundAdvicedWithNullAdvice();
            assertEquals("before after ", s_logString);
            assertEquals(null, i);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetFieldPreAdviced() {
        s_logString = "";
        try {
            setFieldPreAdviced();
            assertEquals("pre1 pre2 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetFieldPostAdviced() {
        s_logString = "";
        try {
            setFieldPostAdviced();
            assertEquals("post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetFieldPrePostAdviced() {
        s_logString = "";
        try {
            setFieldPrePostAdviced();
            assertEquals("pre1 pre2 post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetFieldPreAdviced() {
        s_logString = "";
        try {
            getFieldPreAdviced();
            assertEquals("pre1 pre2 ", s_logString);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testGetFieldPostAdviced() {
        s_logString = "";
        try {
            getFieldPostAdviced();
            assertEquals("post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetFieldPrePostAdviced() {
        s_logString = "";
        try {
            getFieldPrePostAdviced();
            assertEquals("pre1 pre2 post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetStaticFieldAroundAdviced() {
        s_logString = "";
        try {
            setStaticFieldAroundAdviced();
            assertEquals("before after ", s_logString);
            assertEquals(3, s_setStaticFieldAroundAdviced);
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetStaticFieldAroundAdviced() {
        s_logString = "";
        try {
            long i = getStaticFieldAroundAdviced();
            assertEquals("before after ", s_logString);
            assertEquals(1L, i);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetStaticFieldPreAdviced() {
        s_logString = "";
        try {
            setStaticFieldPreAdviced();
            assertEquals("pre1 pre2 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetStaticFieldPostAdviced() {
        s_logString = "";
        try {
            setStaticFieldPostAdviced();
            assertEquals("post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetStaticFieldPrePostAdviced() {
        s_logString = "";
        try {
            setStaticFieldPrePostAdviced();
            assertEquals("pre1 pre2 post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetStaticFieldPreAdviced() {
        s_logString = "";
        try {
            getStaticFieldPreAdviced();
            assertEquals("pre1 pre2 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetStaticFieldPostAdviced() {
        s_logString = "";
        try {
            getStaticFieldPostAdviced();
            assertEquals("post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testStaticGetFieldPrePostAdviced() {
        s_logString = "";
        try {
            getStaticFieldPrePostAdviced();
            assertEquals("pre1 pre2 post2 post1 ", s_logString);
        } catch (Exception e) {
            fail();
        }
    }

    public void testPublicFieldOutOfWeaverScope() {
        s_logString = "";
        PrintStream out = System.out;//field get(* java.lang.System) && withincode ..
        PrintStream err = System.err;        
        assertEquals("adviceOnPublicField ", s_logString);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(FieldAdviceTest.class);
    }

    // ==== methods to test ====
    public static void log(final String wasHere) {
        s_logString += wasHere;
    }

    public void setFieldAroundAdviced() {
        m_setFieldAroundAdviced = 3 + (23 * 8);
    }

    public void setFieldAroundAdvicedWithNullAdvice() {
        m_setFieldAroundAdvicedWithNullAdvice = 3 + (23 * 8);
    }

    public void setFieldAroundAdvicedObjectWithNullAdvice() {
        m_setFieldAroundAdvicedObjectWithNullAdvice = new String("1");
    }

    public void setFieldAroundAdvicedObjectWithAPI() {
        m_setFieldAroundAdvicedObjectWithAPI = new String("original");
    }

    public void setFieldAroundAdvicedWithAPI() {
        m_setFieldAroundAdvicedWithAPI = 2;
    }

    public void setFieldPreAdviced() {
        m_setFieldPreAdviced = 3 + (23 * 8);
    }

    public void setFieldPostAdviced() {
        m_setFieldPostAdviced = "asdf";
    }

    public void setFieldPrePostAdviced() {
        m_setFieldPrePostAdviced = 3;
    }

    public long getFieldAroundAdviced() {
        return m_getFieldAroundAdviced;
    }

    public String getFieldAroundAdvicedWithNullAdvice() {
        return m_getFieldAroundAdvicedWithNullAdvice;
    }

    public double getFieldPreAdviced() {
        return m_getFieldPreAdviced;
    }

    public int getFieldPostAdviced() {
        return m_getFieldPostAdviced;
    }

    public int getFieldPrePostAdviced() {
        return m_getFieldPrePostAdviced;
    }

    public static void setStaticFieldAroundAdviced() {
        s_setStaticFieldAroundAdviced = 3;
    }

    public static void setStaticFieldPreAdviced() {
        s_setStaticFieldPreAdviced = 3;
    }

    public static void setStaticFieldPostAdviced() {
        s_setStaticFieldPostAdviced = "asdf";
    }

    public static void setStaticFieldPrePostAdviced() {
        s_setStaticFieldPrePostAdviced = 3;
    }

    public static long getStaticFieldAroundAdviced() {
        return s_getStaticFieldAroundAdviced;
    }

    public static int getStaticFieldPreAdviced() {
        return s_getStaticFieldPreAdviced;
    }

    public static String getStaticFieldPostAdviced() {
        return s_getStaticFieldPostAdviced;
    }

    public static double getStaticFieldPrePostAdviced() {
        return s_getStaticFieldPrePostAdviced;
    }
}