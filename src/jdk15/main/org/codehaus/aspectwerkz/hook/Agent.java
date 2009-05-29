/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.ClassFileTransformer;

/**
 * Java 1.5 preMain agent
 * Can be used with -javaagent:aspectwerkz-core-XXX.jar
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class Agent {

    /**
     * The instrumentation instance
     */
    private static Instrumentation s_instrumentation;

    /**
     * The ClassFileTransformer wrapping AspectWerkz weaver
     */
    public static ClassFileTransformer s_transformer = new PreProcessorAdapter();

    /**
     * JSR-163 preMain Agent entry method
     */
    public static void premain(String options, Instrumentation instrumentation) {
        s_instrumentation = instrumentation;
        s_instrumentation.addTransformer(s_transformer);
    }

    /**
     * Returns the Instrumentation system level instance
     */
    public static Instrumentation getInstrumentation() {
        if (s_instrumentation == null) {
            throw new UnsupportedOperationException("Java 5 was not started with preMain -javaagent for AspectWerkz");
        }
        return s_instrumentation;
    }

}
