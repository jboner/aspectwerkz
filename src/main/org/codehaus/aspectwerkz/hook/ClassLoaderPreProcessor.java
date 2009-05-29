/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

/**
 * Implement to be the java.lang.ClassLoader pre processor. <p/>ProcessStarter calls once the no-arg constructor of the
 * class implementing this interface and specified with the <code>-Daspectwerkz.classloader.clpreprocessor</code>
 * option. It uses org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl by default, which is a ASM
 * implementation (since 2004 10 20).
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 * @see org.codehaus.aspectwerkz.hook.ProcessStarter
 * @see org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl
 */
public interface ClassLoaderPreProcessor {
    /**
     * instruments the java.lang.ClassLoader bytecode
     */
    public byte[] preProcess(byte[] b);
}