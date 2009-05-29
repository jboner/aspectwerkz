/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aopalliance.aspect;

import test.aopalliance.Test;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Test interceptor for the Aop Alliance compiler extension to AspectWerkz.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class TestInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Test.log("before-intercept ");
        Test.log(invocation.getMethod().getName() + ' ');
        Test.log(invocation.getThis().getClass().getName() + ' ');
        invocation.getArguments();
        Object rval = invocation.proceed();
        Test.log("after-intercept ");
        return rval;
    }
}
