/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.caching;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class CacheStatistics {

    private static Map s_methodInvocations = Collections.synchronizedMap(new HashMap());

    private static Map s_cacheInvocations = Collections.synchronizedMap(new HashMap());

    public static void addMethodInvocation(final String methodName, final Class[] parameterTypes) {
        Long hash = calculateHash(methodName, parameterTypes);
        if (!s_methodInvocations.containsKey(hash)) {
            s_methodInvocations.put(hash, new Integer(0));
        }
        int counter = ((Integer) s_methodInvocations.get(hash)).intValue();
        counter++;
        s_methodInvocations.put(hash, new Integer(counter));
    }

    public static void addCacheInvocation(final String methodName, final Class[] parameterTypes) {
        Long hash = calculateHash(methodName, parameterTypes);
        if (!s_cacheInvocations.containsKey(hash)) {
            s_cacheInvocations.put(hash, new Integer(0));
        }
        int counter = ((Integer) s_cacheInvocations.get(hash)).intValue();
        counter++;
        s_cacheInvocations.put(hash, new Integer(counter));
    }

    public static int getNrOfMethodInvocationsFor(final String methodName,
                                                  final Class[] parameterTypes) {
        Integer number = (Integer) s_methodInvocations
                .get(calculateHash(methodName, parameterTypes));
        if (number != null) {
            return number.intValue();
        } else {
            return 0;
        }
    }

    public static int getNrOfCacheInvocationsFor(final String methodName,
                                                 final Class[] parameterTypes) {
        Integer number = (Integer) s_cacheInvocations
                .get(calculateHash(methodName, parameterTypes));
        if (number != null) {
            return number.intValue();
        } else {
            return 0;
        }
    }

    private static Long calculateHash(final String methodName, final Class[] parameterTypes) {
        int result = 17;
        result = 37 * result + methodName.hashCode();
        for (int i = 0, j = parameterTypes.length; i < j; i++) {
            result = 37 * result + parameterTypes[i].hashCode();
        }
        return new Long(result);
    }
}