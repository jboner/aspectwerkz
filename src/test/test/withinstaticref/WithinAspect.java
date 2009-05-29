/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.withinstaticref;

import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

public class WithinAspect {
    static int s_count = 0;
    public void beforeAdvice(StaticJoinPoint sjp) {
        s_count++;
    }
}
