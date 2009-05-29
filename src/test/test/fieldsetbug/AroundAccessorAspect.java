/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.fieldsetbug;


import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

public class AroundAccessorAspect {
    /**
     * @Around set(* test.fieldsetbug.TargetClass.public*) AND within(test.fieldsetbug.*)
     */
    public Object aroundAccessor(JoinPoint jp) throws Throwable {
        if (jp.getCallee() != jp.getCaller()) {
            return null;
        } else {
            return jp.proceed();
        }
    }

}
