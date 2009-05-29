/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD-style license *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.extension.hotswap;

import org.codehaus.aspectwerkz.hook.impl.ClassPreProcessorHelper;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;

/**
 * In process HotSwap - Java level API <p/>When used, the hook* classes (AspectWerkz - core) MUST be
 * in bootclasspath to ensure correct behavior and lookup of the ClassPreProcessor singleton
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class HotSwapClient {

    static {
        System.loadLibrary("aspectwerkz");
    }

    /**
     * Native method to calls the JVM C level API
     *
     * @param className
     * @param klazz
     * @param newBytes
     * @param newLength
     * @return
     */
    private static native int hotswap(String className, Class klazz, byte[] newBytes, int newLength);

    /**
     * In process hotswap
     *
     * @param klazz
     * @param newBytes
     */
    public static void hotswap(final Class klazz, final byte[] newBytes) {
        int code = hotswap(klazz.getName(), klazz, newBytes, newBytes.length);
        if (code != 0) {
            throw new RuntimeException("HotSwap failed for " + klazz.getName() + ": " + code);
        }
    }

}