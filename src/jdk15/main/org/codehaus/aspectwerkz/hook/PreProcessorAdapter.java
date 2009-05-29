/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

import org.codehaus.aspectwerkz.hook.impl.ClassPreProcessorHelper;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * Java 1.5 adapter for load time weaving
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class PreProcessorAdapter implements ClassFileTransformer {

    /**
     * Concrete AspectWerkz preprocessor.
     */
    private static ClassPreProcessor s_preProcessor;

    static {
        try {
            s_preProcessor = ClassPreProcessorHelper.getClassPreProcessor();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("could not initialize JSR163 preprocessor due to: " + e.toString());
        }
    }

    /**
     * Weaving delegation
     *
     * @param loader              the defining class loader
     * @param className           the name of class beeing loaded
     * @param classBeingRedefined when hotswap is called
     * @param protectionDomain
     * @param bytes               the bytecode before weaving
     * @return the weaved bytecode
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        // TODO: do we really have to skip hotswap ?
        if (classBeingRedefined == null) {
            return s_preProcessor.preProcess(className, bytes, loader);
        } else {
            //return s_preProcessor.preProcess(className, bytes, loader);
            return bytes;
        }
    }

}
