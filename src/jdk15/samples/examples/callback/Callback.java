/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.callback;

import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import org.codehaus.aspectwerkz.annotation.Around;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Callback call from an async aspect
 * Run with VM options:
 * -javaagent:lib\aspectwerkz-core-2.0beta1.jar
 * -Daspectwerkz.definition.file=src\jdk15\samples\examples\callback\aop.xml
 * And optianally:
 * -Daspectwerkz.transform.verbose=true
 * <p/>
 * Note: you can avoid use of -D...file=...aop.xml if you have the aop.xml in a META-INF folder somewhere in the classpath.
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class Callback {

    /**
     * This method is a callback - could be looked up with a custom @ or etc
     */
    public void callback(String message) {
        System.out.println("Callback.callback " + message);
    }

    /**
     * Triggers the time consuming operation on callee
     * Note that when used with "this(..) pcd, the caller method cannot be static else we won't match
     * since caller instance is not available..
     */
    public void work() {
        System.out.println("Callback.work - start");
        Callee callee = new Callee();
        callee.longOp(10000);
        System.out.println("Callback.work - end");
    }

    /**
     * Sample
     *
     * @param args
     * @throws Throwable
     */
    public static void main(String args[]) throws Throwable {
        Callback caller = new Callback();
        caller.work();
    }

    /**
     * Callee does a time consuming operation
     */
    public static class Callee {

        public void longOp(int howLong) {
            System.out.println("Callback$Callee.longOp for " + howLong);
            for (int i = 0; i < howLong; i++) {
                ;
            }
        }
    }

    public static class AsyncAspect {

        /**
         * Java 5 thread utils
         */
        private Executor m_threadPool = Executors.newCachedThreadPool(
                new ThreadFactory() {
                    public Thread newThread(Runnable target) {
                        Thread t = new Thread(target);
                        t.setDaemon(true);// use of daemon to run from Ant
                        return t;
                    }
                }
        );

        // a bit tedious to match inner class so I am a bit lazy here.
        @Around("call(* *..*.*Callee.longOp(int)) && args(howLong) && this(caller)")
                public Object doAsync(final StaticJoinPoint sjp, int howLong, final Callback caller) throws Throwable {
            System.out.println("[AOP powered] - Callback$AsyncAspect.doAsync - for this long: " + howLong);
            m_threadPool.execute(
                    new Runnable() {
                        public void run() {
                            try {
                                // proceed in a new thread
                                sjp.proceed();
                                // when done, triggers the callback
                                caller.callback(
                                        "[AOP powered] - I am done there .. " + Thread.currentThread().getName()
                                );
                            } catch (Throwable e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
            return null;
        }
    }


}
