/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

/**
 * Implement to be a class PreProcessor in the AspectWerkz univeral loading architecture. <p/>A single instance of the
 * class implementing this interface is build during the java.lang.ClassLoader initialization or just before the first
 * class loads, bootclasspath excepted. Thus there is a single instance the of ClassPreProcessor per JVM. <br/>Use the
 * <code>-Daspectwerkz.classloader.preprocessor</code> option to specify which class preprocessor to use.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 * @see org.codehaus.aspectwerkz.hook.ProcessStarter
 */
public interface ClassPreProcessor {

    public abstract void initialize();

    public abstract byte[] preProcess(String klass, byte[] abyte, ClassLoader caller);
}