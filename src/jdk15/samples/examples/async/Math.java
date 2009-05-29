/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.async;

import is.Async;
import is.Service;

//@Service
public class Math {

    //@Async(timeout = 10)
    public void add(int a, int b) {
        System.out.printf(
                "[ %s ] %d + %d = %d\n",
                Thread.currentThread().getName(),
                new Integer(a), new Integer(b), new Integer(a + b)
        );
    }

    //@Async(timeout = 2)
    public void subtract(int a, int b) {
        System.out.printf(
                "[ %s ] %d - %d = %d\n",
                Thread.currentThread().getName(),
                new Integer(a), new Integer(b), new Integer(a - b)
        );
    }

    public static void main(String args[]) throws Throwable {
        Math math = new Math();
        System.out.println("\n================ Async sample =================");

        math.add(5, 4);
        math.add(1, 5);
        math.add(2, 6);
        math.add(4, 4);
        math.add(8, 4);
        math.subtract(7, 4);
        math.subtract(3, 5);
        math.subtract(1, 6);
        math.subtract(4, 4);
        math.subtract(8, 4);
        Thread.sleep(1000);
        System.exit(0);
    }
}
