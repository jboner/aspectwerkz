/***************************************************************************************************
 * Copyright (c) Jonas BonŽr, Alexandre Vasseur. All rights reserved. *
 * http://aspectwerkz.codehaus.org *
 * ---------------------------------------------------------------------------------- * The software
 * in this package is published under the terms of the LGPL license * a copy of which has been
 * included with this distribution in the license.txt file. *
 **************************************************************************************************/
package test.reflection;

import java.lang.reflect.Method;

public class Child extends Super {
    public int incr(int value) {
        int res = super.incr(value);
        return (res >= 0) ? (res + 1) : (res - 1);
    }

    public static int incrStatic(int value) {
        int res = Super.incrStatic(value);
        return (res >= 0) ? (res + 1) : (res - 1);
    }

    public int do$2(int i) {
        return i;
    }

    public int do$1(int i) {
        return i;
    }

    public int reflectionCallIncr(int value) {
        try {
            Method m = this.getClass().getMethod(
                    "incr", new Class[]{
                        int.class
                    }
            );
            Integer res = (Integer) m.invoke(
                    this, new Object[]{
                        new Integer(value)
                    }
            );
            return res.intValue();
        } catch (Throwable t) {
            return -1000;
        }
    }
}