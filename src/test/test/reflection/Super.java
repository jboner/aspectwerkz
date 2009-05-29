/***************************************************************************************************
 * Copyright (c) Jonas BonŽr, Alexandre Vasseur. All rights reserved. *
 * http://aspectwerkz.codehaus.org *
 * ---------------------------------------------------------------------------------- * The software
 * in this package is published under the terms of the LGPL license * a copy of which has been
 * included with this distribution in the license.txt file. *
 **************************************************************************************************/
package test.reflection;

public class Super {
    public int incr(int value) {
        return (value >= 0) ? (value + 1) : (value - 1);
    }

    public static int incrStatic(int value) {
        return (value >= 0) ? (value + 1) : (value - 1);
    }
}