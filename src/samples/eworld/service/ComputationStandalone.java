/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package eworld.service;

import org.codehaus.aspectwerkz.extension.hotswap.EWorldUtil;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.codehaus.aspectwerkz.joinpoint.MemberSignature;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;

import java.util.Map;
import java.util.HashMap;


/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ComputationStandalone {

    private static final int WEAVING_FREQUENCY = new Integer(
            System
            .getProperty("weaving.frequency")
    ).intValue();

    private static final boolean USE_CACHE = System.getProperty("cache").equals("true");

    private static final boolean USE_TRACE = System.getProperty("trace").equals("true");

    private static final String EXPRESSION = "execution(int eworld.service.ComputationStandalone.fib(int))";

    private static final String SYSTEM_ID = "eworld/wlw/aop";

    private static final String CACHE_POINTCUT = "cache";

    private static final String TRACE_POINTCUT = "trace";

    private static final String CACHE_ADVICE = "cache";

    private static final String TRACE_ADVICE = "trace";

    public static int fib(int n) {
        if (n < 2) {
            System.err.println(n + ".");
            return 1;
        } else {
            System.err.print(n + ",");
            return fib(n - 1) + fib(n - 2);
        }
    }

    private static void weave() {
        if (USE_CACHE) {
            System.err.println("weaving in cache support");
            EWorldUtil.activate(
                    SYSTEM_ID,
                    CacheAspect.class.getName(),
                    CACHE_ADVICE,
                    EXPRESSION,
                    CACHE_POINTCUT
            );
        }
        if (USE_TRACE) {
            System.err.println("weaving in trace support");
            EWorldUtil.activate(
                    SYSTEM_ID,
                    TraceAspect.class.getName(),
                    TRACE_ADVICE,
                    EXPRESSION,
                    TRACE_POINTCUT
            );
        }
    }

    private static void unWeave() {
        if (USE_CACHE) {
            System.err.println("un-weaving cache support");
            EWorldUtil.deactivate(
                    SYSTEM_ID,
                    CacheAspect.class.getName(),
                    CACHE_ADVICE,
                    CACHE_POINTCUT
            );
            // flush the cache... as if we have a "onUndeploy callback.."
            System.err.println("** Flushing the cache...");
            CacheAspect.s_cache.clear();
        }
        if (USE_TRACE) {
            System.err.println("un-weaving trace support");
            EWorldUtil.deactivate(
                    SYSTEM_ID,
                    TraceAspect.class.getName(),
                    TRACE_ADVICE,
                    TRACE_POINTCUT
            );
        }
    }

    public static void main(String[] args) {

        //        if (args.length != 2) {
        //            System.err.println("fib(" + 3 + ") = " + fib(3));
        //
        //            System.err.println("weaving in trace support");
        //            EWorldUtil.activate(SYSTEM_ID, TraceAspect.class.getName(), TRACE_ADVICE, EXPRESSION,
        // TRACE_POINTCUT);
        //            EWorldUtil.hotswap("eworld.service");
        //            System.err.println("fib(" + 3 + ") = " + fib(3));
        //
        //            System.err.println("weaving in cache support");
        //            EWorldUtil.activate(SYSTEM_ID, CacheAspect.class.getName(), CACHE_ADVICE, EXPRESSION,
        // CACHE_POINTCUT);
        //            EWorldUtil.hotswap("eworld.service");
        //            System.err.println("fib(" + 3 + ") = " + fib(3));
        //            System.err.println("fib(" + 3 + ") = " + fib(3));
        //
        //            System.err.println("un-weaving trace support");
        //            EWorldUtil.deactivate(SYSTEM_ID, TraceAspect.class.getName(), TRACE_ADVICE,
        // TRACE_POINTCUT);
        //            EWorldUtil.hotswap("eworld.service");
        //            System.err.println("fib(" + 4 + ") = " + fib(4));
        //
        //            System.err.println("un-weaving cache support");
        //            EWorldUtil.deactivate(SYSTEM_ID, CacheAspect.class.getName(), CACHE_ADVICE,
        // CACHE_POINTCUT);
        //            EWorldUtil.hotswap("eworld.service");
        //            System.err.println("fib(" + 3 + ") = " + fib(3));
        //
        //            System.err.println("weaving in trace support");
        //            EWorldUtil.activate(SYSTEM_ID, TraceAspect.class.getName(), TRACE_ADVICE, EXPRESSION,
        // TRACE_POINTCUT);
        //            EWorldUtil.hotswap("eworld.service");
        //            System.err.println("fib(" + 3 + ") = " + fib(3));
        //
        //            System.err.println("un-weaving trace support");
        //            EWorldUtil.deactivate(SYSTEM_ID, TraceAspect.class.getName(), TRACE_ADVICE,
        // TRACE_POINTCUT);
        //            EWorldUtil.hotswap("eworld.service");
        //            System.err.println("fib(" + 4 + ") = " + fib(4));
        //
        //            System.exit(0);
        //            //throw new IllegalArgumentException("number of iterations and sleep time must be
        // specified");
        //        }
        try {
            int iterations = new Integer(args[0]).intValue();
            long sleep = new Long(args[1]).longValue();
            int counter = 0;
            boolean isWeaved = false;
            while (true) {
                System.out.println(
                        "TraceAspect weave status = "
                        + EWorldUtil.isWeaved(SYSTEM_ID, TraceAspect.class.getName())
                );
                System.out.println(
                        "CacheAspect weave status = "
                        + EWorldUtil.isWeaved(SYSTEM_ID, CacheAspect.class.getName())
                );
                counter++;
                Thread.sleep(sleep);
                System.err.println("fib(" + iterations + ") = " + fib(iterations));
                if (USE_CACHE || USE_TRACE) {
                    if ((counter %= WEAVING_FREQUENCY) == 0) {
                        if (isWeaved) {
                            unWeave();
                            isWeaved = false;
                        } else {
                            weave();
                            isWeaved = true;
                        }
                        EWorldUtil.hotswap("eworld.service");
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new WrappedRuntimeException(e);
        }
    }

    /**
     * Cache aspect.
     */
    public static class CacheAspect {

        /** a static cache to flush it from the outside for demo purpose. Safe since aspect is singleton */
        public static Map s_cache = new HashMap();

        public Object cache(final JoinPoint joinPoint) throws Throwable {
            MethodRtti mrtti = (MethodRtti) joinPoint.getRtti();
            Integer parameter = (Integer) mrtti.getParameterValues()[0];
            Integer cachedValue = (Integer) s_cache.get(parameter);
            if (cachedValue == null) {
                System.err.println("not in cache");
                Object newValue = joinPoint.proceed(); // not found => calculate
                s_cache.put(parameter, newValue);
                return newValue;
            } else {
                System.err.println("using cache: " + cachedValue);
                return cachedValue; // return cached value
            }
        }
    }

    /**
     * Trace aspect.
     */
    public static class TraceAspect {
        private int m_level = 0;

        public Object trace(final JoinPoint joinPoint) throws Throwable {
            MemberSignature signature = (MemberSignature) joinPoint.getSignature();
            indent();
            System.out.println(
                    "--> "
                    + signature.getDeclaringType().getName()
                    + "::"
                    + signature.getName()
            );
            m_level++;
            final Object result = joinPoint.proceed();
            m_level--;
            indent();
            System.out.println(
                    "<-- "
                    + signature.getDeclaringType().getName()
                    + "::"
                    + signature.getName()
            );
            return result;
        }

        private void indent() {
            for (int i = 0; i < m_level; i++) {
                System.out.print("  ");
            }
        }
    }
}