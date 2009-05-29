/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.ejb3;

import org.codehaus.aspectwerkz.ejb3.AroundInvokeAOP;

import javax.ejb.AroundInvoke;
import javax.ejb.InvocationContext;

/**
 * A standalone interceptor
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class MyInterceptor {

    @AroundInvoke
    @AroundInvokeAOP("execution(* *.businessSum(..))")
    public Object interceptStandalone(InvocationContext ctx) throws Exception {
        System.out.println("--> MyInterceptor.interceptStandalone");
        System.out.println("  method: " + ctx.getMethod());
        for (int i = 0; i < ctx.getParameters().length; i++) {
            Object o = ctx.getParameters()[i];
            System.out.println("  args["+i+"]: " + o);
        }
        return ctx.proceed();
    }
}
