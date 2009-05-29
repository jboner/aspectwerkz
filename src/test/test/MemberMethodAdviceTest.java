/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class MemberMethodAdviceTest extends TestCase implements Loggable {
    private String java = "a field that can make bytecode tools confused, AW-147 item2, fixed in AW 1.0-beta1";

    private String m_logString = "";

    public MemberMethodAdviceTest() {
        MemberMethodAdviceTest fake = new MemberMethodAdviceTest(new Long(0));
    }

    //AW-393 test case
    public MemberMethodAdviceTest(Integer fake) {
    }

    //AW-393 test case
    public MemberMethodAdviceTest(Long l) {
        this(new Integer(0));
    }

    public void testBeforeAroundAroundAfterAdvice() {
        m_logString = "";
        try {
            beforeAroundAfterAdvicedMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("pre before1 before2 invocation after2 after1 post ", m_logString);
    }

    public void testBeforeAdvice() {
        m_logString = "";
        beforeAdvicedMethod();
        assertEquals("pre invocation ", m_logString);
    }

    public void testAfterAdvice() {
        m_logString = "";
        afterAdvicedMethod();
        assertEquals("invocation post ", m_logString);
    }

    public void testBeforeAfterAdvice() {
        m_logString = "";
        beforeAfterAdvicedMethod();
        assertEquals("pre invocation post ", m_logString);
    }

    public void testAroundAdvice() {
        m_logString = "";
        methodAdvicedMethod();
        assertEquals("before1 invocation after1 ", m_logString);
    }

    public void testAroundAdvice2() {
        m_logString = "";
        methodAdvicedMethod(0);
        assertEquals("invocation ", m_logString);
    }

    public void testAroundAdviceNewThread() {
        m_logString = "";
        // call + execution advice
        methodAdvicedMethodNewThread();
        assertEquals("before before invocation after after ", m_logString);
    }

    public void testMultipleAroundAdvices() {
        m_logString = "";
        multipleMethodAdvicedMethod();
        assertEquals("before1 before2 invocation after2 after1 ", m_logString);
    }

    public void testMultipleChainedAroundAdvices() {
        m_logString = "";
        multipleChainedMethodAdvicedMethod();
        assertEquals("before1 before2 invocation after2 after1 ", m_logString);
    }

    public void testMultiplePointcuts() {
        m_logString = "";
        multiplePointcutsMethod();
        assertEquals("before2 before1 invocation after1 after2 ", m_logString);
    }

    //    public void testGetJoinPointMetaData() {
    //        String param = "parameter";
    //        assertEquals(
    //                getClass().getName() +
    //                "___AW_$_AW_$joinPointMetaData$_AW_$1$_AW_$test_MemberMethodAdviceTest" +
    //                hashCode() +
    //                param +
    //                param.getClass().getName() +
    //                "java.lang.String" +
    //                "result",
    //                joinPointMetaData(param)
    //        );
    //    }
    public void testHasPointcutButNoAdvice() {
        try {
            hasPointcutButNoAdvice();
        } catch (Exception e) {
            fail();
        }
    }

    public void testAnonymousAdviced() {
        try {
            anonymousAdviced();
        } catch (Exception e) {
            fail();
        }
    }

    public void testThrowException() {
        try {
            exceptionThrower();
        } catch (Throwable e) {
            assertTrue(e instanceof UnsupportedOperationException);
            return;
        }
        fail("this point should never be reached");
    }

    public void testThrowExceptionChecked() {
        try {
            exceptionThrowerChecked();
        } catch (Throwable e) {
            assertTrue(e instanceof CheckedException);
            return;
        }
        fail("this point should never be reached");
    }

    public void testReturnVoid() {
        getVoid();
    }

    public void testReturnLong() {
        assertEquals(1L, getLong());
    }

    public void testReturnInt() {
        assertEquals(1, getInt());
    }

    public void testReturnShort() {
        assertEquals(1, getShort());
    }

    public void testReturnDouble() {
        assertEquals(new Double(1.1D), new Double(getDouble()));
    }

    public void testReturnFloat() {
        assertEquals(new Float(1.1F), new Float(getFloat()));
    }

    public void testReturnByte() {
        assertEquals(Byte.parseByte("1"), getByte());
    }

    public void testReturnChar() {
        assertEquals('A', getChar());
    }

    public void testReturnPrimitiveAndNullFromAdvice() {
        try {
            assertEquals(0L, getPrimitiveAndNullFromAdvice());
        } catch (NullPointerException e) {
            fail(
                    "If method that returns a primitive has an advice that returns NULL then it causes a NPE. The NULL should be handled in bytecode and it should return the default value for the primitive (wrapped)"
            );
        }
    }

    public void testReturnBoolean() {
        assertEquals(true, getBoolean());
    }

    public void testNoArgs() {
        noParams();
    }

    public void testIntArg() {
        assertEquals(12, intParam(12));
    }

    public void testLongArg() {
        assertEquals(12L, longParam(12L));
    }

    public void testShortArg() {
        assertEquals(3, shortParam((short) 3));
    }

    public void testDoubleArg() {
        assertEquals(new Double(2.3D), new Double(doubleParam(2.3D)));
    }

    public void testFloatArg() {
        assertEquals(new Float(2.3F), new Float(floatParam(2.3F)));
    }

    public void testByteArg() {
        assertEquals(Byte.parseByte("1"), byteParam(Byte.parseByte("1")));
    }

    public void testCharArg() {
        assertEquals('B', charParam('B'));
    }

    public void testBooleanArg() {
        assertEquals(false, booleanParam(false));
    }

    public void testObjectArg() {
        assertEquals(this, objectParam(this));
    }

    public void testObjectArrayArg() {
        String[] array = new String[]{
            "one", "two", "three"
        };
        assertTrue(arrayParam(array)[0].equals(array[0]));
        assertTrue(arrayParam(array)[1].equals(array[1]));
        assertTrue(arrayParam(array)[2].equals(array[2]));
    }

    public void testCharArrayArg() {
        char[] array = new char[]{
            'A', 'B', 'C'
        };
        assertTrue(charArrayParam(array)[0] == array[0]);
        assertTrue(charArrayParam(array)[1] == array[1]);
        assertTrue(charArrayParam(array)[2] == array[2]);
    }

    public void testLongArrayArg() {
        long[] array = new long[]{
            1L, 2L, 3L
        };
        assertTrue(longArrayParam(array)[0] == array[0]);
        assertTrue(longArrayParam(array)[1] == array[1]);
        assertTrue(longArrayParam(array)[2] == array[2]);
    }

    public void testIntArrayArg() {
        int[] array = new int[]{
            1, 2, 3
        };
        assertTrue(intArrayParam(array)[0] == array[0]);
        assertTrue(intArrayParam(array)[1] == array[1]);
        assertTrue(intArrayParam(array)[2] == array[2]);
    }

    public void testShortArrayArg() {
        short[] array = new short[]{
            1, 2, 3
        };
        assertTrue(shortArrayParam(array)[0] == array[0]);
        assertTrue(shortArrayParam(array)[1] == array[1]);
        assertTrue(shortArrayParam(array)[2] == array[2]);
    }

    public void testBooleanArrayArg() {
        boolean[] array = new boolean[]{
            true, false
        };
        assertTrue(booleanArrayParam(array)[0] == array[0]);
        assertTrue(booleanArrayParam(array)[1] == array[1]);
    }

    public void testByteArrayArg() {
        byte[] array = new byte[]{
            1, 2, 3
        };
        assertTrue(byteArrayParam(array)[0] == array[0]);
        assertTrue(byteArrayParam(array)[1] == array[1]);
        assertTrue(byteArrayParam(array)[2] == array[2]);
    }

    public void testFloatArrayArg() {
        float[] array = new float[]{
            1.1F, 2.1F, 3.1F
        };
        assertTrue(floatArrayParam(array)[0] == array[0]);
        assertTrue(floatArrayParam(array)[1] == array[1]);
        assertTrue(floatArrayParam(array)[2] == array[2]);
    }

    public void testVariousArguments1() {
        assertEquals(
                "dummy".hashCode() + 1 + (int) 2.3F,
                this.hashCode() + (int) 34L,
                variousParams1("dummy", 1, 2.3F, this, 34L)
        );
    }

    public void testVariousArguments2() {
        assertEquals(
                (int) 2.3F
                + 1
                + "dummy".hashCode()
                + this.hashCode()
                + (int) 34L
                + "test".hashCode(), variousParams2(2.3F, 1, "dummy", this, 34L, "test")
        );
    }

    public void testVariousArguments4() {
        assertEquals(
                "dummy", takesArrayAsArgument(
                        new String[]{
                            "dummy", "test"
                        }
                )[0]
        );
        assertEquals(
                "test", takesArrayAsArgument(
                        new String[]{
                            "dummy", "test"
                        }
                )[1]
        );
    }

    public void testLongParamNoAroundAdvice() {
        assertEquals(12L, longNoAroundAdvice(12L));
    }

    public void testWithincodeCtor() {
        MemberMethodAdviceTest me = new MemberMethodAdviceTest(123);
        assertEquals("ctor call post ", me.m_logString);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(MemberMethodAdviceTest.class);
    }

    // ==== methods to test ====
    public void log(final String wasHere) {
        m_logString += wasHere;
    }

    private void nonAdvisedMethod() {
    }

    private void methodAdvicedMethod() {
        log("invocation ");
    }

    private void methodAdvicedMethod(int o) {
        log("invocation ");
    }

    public void beforeAroundAfterAdvicedMethod() {
        log("invocation ");
    }

    public void beforeAdvicedMethod() {
        log("invocation ");
    }

    public void afterAdvicedMethod() {
        log("invocation ");
    }

    public void beforeAfterAdvicedMethod() {
        log("invocation ");
    }

    public void methodAdvicedMethodNewThread() {
        log("invocation ");
    }

    public void multipleMethodAdvicedMethod() {
        log("invocation ");
    }

    public void multipleChainedMethodAdvicedMethod() {
        log("invocation ");
    }

    public void multiplePointcutsMethod() {
        log("invocation ");
    }

    public void multipleMethodAndPrePostAdvicedMethod() {
        log("invocation ");
    }

    public void methodAdvicedWithPreAndPost() {
        log("invocation ");
    }

    public void multipleMethodAdvicedWithPreAndPost() {
        log("invocation ");
    }

    private void methodAdviceWithMultiplePreAndPostAdviced() {
        log("invocation ");
    }

    public void exceptionThrower() throws Throwable {
        throw new UnsupportedOperationException("this is a test");
    }

    public void exceptionThrowerChecked() throws CheckedException {
        throw new CheckedException();
    }

    public String joinPointMetaData(String param) {
        return "result";
    }

    public void hasPointcutButNoAdvice() {
    }

    public String postAdviced() {
        return "test";
    }

    public void anonymousAdviced() {
    }

    public void throwsException() throws Exception {
        throw new Exception("test");
    }

    public void throwsRuntimeException() {
        throw new RuntimeException("test");
    }

    public void throwsError() {
        throw new Error("test");
    }

    public void noParams() throws RuntimeException {
    }

    public long longParam(long arg) {
        return arg;
    }

    public long longNoAroundAdvice(long arg) {
        return arg;
    }

    public int intParam(int arg) {
        return arg;
    }

    public short shortParam(short arg) {
        return arg;
    }

    public double doubleParam(double arg) {
        return arg;
    }

    private float floatParam(float arg) {
        return arg;
    }

    public byte byteParam(byte arg) {
        return arg;
    }

    public boolean booleanParam(boolean arg) {
        return arg;
    }

    public char charParam(char arg) {
        return arg;
    }

    protected Object objectParam(Object arg) {
        return arg;
    }

    public String[] arrayParam(String[] arg) {
        return arg;
    }

    public long[] longArrayParam(long[] arg) {
        return arg;
    }

    public float[] floatArrayParam(float[] arg) {
        return arg;
    }

    public char[] charArrayParam(char[] arg) {
        return arg;
    }

    public int[] intArrayParam(int[] arg) {
        return arg;
    }

    public short[] shortArrayParam(short[] arg) {
        return arg;
    }

    public boolean[] booleanArrayParam(boolean[] arg) {
        return arg;
    }

    public byte[] byteArrayParam(byte[] arg) {
        return arg;
    }

    public int variousParams1(String str, int i, float f, Object o, long l) throws RuntimeException {
        return str.hashCode() + i + (int) f + o.hashCode() + (int) l;
    }

    private int variousParams2(float f, int i, String str1, Object o, long l, String str2) throws RuntimeException {
        return (int) f + i + str1.hashCode() + o.hashCode() + (int) l + str2.hashCode();
    }

    public float variousParams3(String s, long y, String t, String r, String e, int w, String q) {
        return 2.5F;
    }

    public String[] takesArrayAsArgument(String[] arr) {
        return arr;
    }

    protected void getVoid() throws RuntimeException {
    }

    public long getLong() throws RuntimeException {
        return 1L;
    }

    public int getInt() throws RuntimeException {
        return 1;
    }

    public short getShort() throws RuntimeException {
        return 1;
    }

    public double getDouble() throws RuntimeException {
        return 1.1D;
    }

    public float getFloat() throws RuntimeException {
        return 1.1F;
    }

    public byte getByte() throws RuntimeException {
        return Byte.parseByte("1");
    }

    public char getChar() throws RuntimeException {
        return 'A';
    }

    private boolean getBoolean() throws RuntimeException {
        return true;
    }

    public long getPrimitiveAndNullFromAdvice() throws RuntimeException {
        return 123456789L;
    }

    private static class CheckedException extends Exception {
        public CheckedException() {
            super();
        }
    }

    public MemberMethodAdviceTest(int dummy) {
        log("ctor ");
        callWithincodeCtor();
    }

    public void callWithincodeCtor() {
        log("call ");
    }

}