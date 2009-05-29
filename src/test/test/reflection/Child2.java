/***************************************************************************************************
 * Copyright (c) Jonas Bonr, Alexandre Vasseur. All rights reserved. *
 * http://aspectwerkz.codehaus.org *
 * ---------------------------------------------------------------------------------- * The software
 * in this package is published under the terms of the LGPL license * a copy of which has been
 * included with this distribution in the license.txt file. *
 **************************************************************************************************/
package test.reflection;

public class Child2 extends Super2 {
    public int incr(int value) {
        int res = super.incr(value);
        return (res >= 0) ? (res + 1) : (res - 1);
    }

    public static int incrStatic(int value) {
        int res = Super2.incrStatic(value);
        return (res >= 0) ? (res + 1) : (res - 1);
    }
}