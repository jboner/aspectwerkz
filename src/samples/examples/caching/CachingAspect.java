/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.caching;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodSignature;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.codehaus.aspectwerkz.joinpoint.Rtti;
import org.codehaus.aspectwerkz.AspectContext;
import org.codehaus.aspectwerkz.AspectContext;
import org.codehaus.aspectwerkz.AspectContext;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perInstance name=NAME
 */
public class CachingAspect {

    /**
     * The cross-cutting info.
     */
    private final AspectContext m_info;

    /**
     * The cache.
     */
    private final Map m_cache = new HashMap();

    /**
     * We are interested in cross-cutting info, therefore we have added a constructor that takes a
     * cross-cutting infor instance as its only parameter.
     *
     * @param info the cross-cutting info
     */
    public CachingAspect(final AspectContext info) {
        m_info = info;
    }

    /**
     * @Before call(int examples.caching.Pi.getPiDecimal(int)) && withincode(int
     * examples.caching.main(String[]))
     */
    public void invocationCounter(final JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CacheStatistics.addMethodInvocation(signature.getName(), signature.getParameterTypes());
    }

    /**
     * @Around execution(int examples.caching.Pi.getPiDecimal(int))
     */
    public Object cache(final JoinPoint joinPoint) throws Throwable {
        MethodRtti mrtti = (MethodRtti) joinPoint.getRtti();
        final Long hash = new Long(calculateHash(mrtti));
        final Object cachedResult = m_cache.get(hash);
        if (cachedResult != null) {
            System.out.println("using            cache");
            CacheStatistics.addCacheInvocation(mrtti.getName(), mrtti.getParameterTypes());
            System.out.println("parameter: timeout = " + m_info.getParameter("timeout"));
            return cachedResult;
        }
        final Object result = joinPoint.proceed();
        m_cache.put(hash, result);
        return result;
    }

    // ============ Utility methods ============

    private long calculateHash(final MethodRtti rtti) {
        int result = 17;
        result = 37 * result + rtti.getName().hashCode();
        Object[] parameters = rtti.getParameterValues();
        for (int i = 0, j = parameters.length; i < j; i++) {
            result = 37 * result + parameters[i].hashCode();
        }
        return result;
    }
}