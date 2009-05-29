/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.caching;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Pi {

    public int getPiDecimal(int n) {
        System.out.println("Pi.getPiDecimal");
        String decimals = "141592653";
        if (n > decimals.length()) {
            return 0;
        } else {
            return Integer.parseInt(decimals.substring(n, n + 1));
        }
    }
}