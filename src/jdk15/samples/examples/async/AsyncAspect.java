/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD-style license *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.async;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodSignature;
import org.codehaus.aspectwerkz.annotation.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.lang.reflect.Method;

import is.Async;

public class AsyncAspect {

    private Executor m_threadPool = Executors.newCachedThreadPool();

    @Around("execution(void examples.async.Math.add(..))")
    //@Around("execution(@is.Async) && within(@is.Service)")
    public Object async(final JoinPoint jp) throws Throwable {
        m_threadPool.execute(
                new Runnable() {
                    public void run() {
                        try {
                            // proceed in a new thread

                            // AOP code
                            Method currentMethod = ((MethodSignature)jp.getSignature()).getMethod();

                            // plain Java code
                            Async theCurrentAnnotation = currentMethod.getAnnotation(Async.class);
                            //System.out.println("AsyncAspect.run - timeout = " + theCurrentAnnotation.timeout());

                            jp.proceed();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
        );
        return null;
    }




//    @Before("staticinitialization(@is.Service)")
//    public void before(JoinPoint jp) {
//        System.out.println(jp.toString());
//        System.out.println(jp.getSignature().toString());
//    }


}