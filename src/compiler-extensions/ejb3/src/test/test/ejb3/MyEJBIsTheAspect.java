/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.ejb3;

import javax.ejb.InvocationContext;
import javax.ejb.AroundInvoke;
import javax.ejb.Interceptor;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */

//@Stateless etc for a real EJB
@Interceptor("test.ejb3.MyInterceptor")
public class MyEJBIsTheAspect {

    // business method
    public int businessSum(int i, int j) {
        return i + j;
    }

    // business method
    public int businessSubstract(int i, int j) {
        return i - j;
    }

    // interceptor method within the bean (the bean is the aspect)
    @AroundInvoke
    public Object interceptMySelf(InvocationContext ctx) throws Exception {
        System.out.println("--> MyEJBIsTheAspect.interceptMySelf");
        System.out.println("  method: " + ctx.getMethod());
        for (int i = 0; i < ctx.getParameters().length; i++) {
            Object o = ctx.getParameters()[i];
            System.out.println("  args["+i+"]: " + o);
        }
        return ctx.proceed();
    }

}
