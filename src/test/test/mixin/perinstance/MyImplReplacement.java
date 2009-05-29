/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.mixin.perinstance;

/**
 * Replacement for MyImpl mixin
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class MyImplReplacement implements Cloneable, Introductions {
    public MyImplReplacement(Object target) {
    }

    public void noArgs() throws RuntimeException {
    }

    public long longArg(long arg) {
        return arg;
    }

    /**
     * Used in test suite: replacement does a -2 x
     */
    public int intArg(int arg) {
        return -2 * arg;
    }

    public short shortArg(short arg) {
        return arg;
    }

    public double doubleArg(double arg) {
        return arg;
    }

    public float floatArg(float arg) {
        return arg;
    }

    public byte byteArg(byte arg) {
        return arg;
    }

    public boolean booleanArg(boolean arg) {
        return arg;
    }

    public char charArg(char arg) {
        return arg;
    }

    public Object objectArg(Object arg) {
        return arg;
    }

    public String[] arrayArg(String[] arg) {
        return arg;
    }

    public int variousArguments1(String str, int i, float f, Object o, long l) throws RuntimeException {
        return str.hashCode() + i + (int) f + o.hashCode() + (int) l;
    }

    public int variousArguments2(float f, int i, String str1, Object o, long l, String str2) throws RuntimeException {
        return (int) f + i + str1.hashCode() + o.hashCode() + (int) l + str2.hashCode();
    }

    public void getVoid() throws RuntimeException {
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

    public boolean getBoolean() throws RuntimeException {
        return true;
    }

    public void exceptionThrower() throws Throwable {
        throw new UnsupportedOperationException("this is a test");
    }

    public void exceptionThrowerChecked() throws CheckedException {
        throw new CheckedException();
    }
}
