/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.util;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Extends the <code>java.lang.ThreadLocal</code> to be able to add additional functionality. <p/>This classes
 * enhances the base implementation by: <p/>making it serializable <p/>making it wrap an unwrap the values in a
 * <code>java.lang.ref.WeakReference</code> to avoid potential memory leaks
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class SerializableThreadLocal extends java.lang.ThreadLocal implements Serializable {
    /**
     * Constructor. Simply calls the base class constructor.
     */
    public SerializableThreadLocal() {
        super();
    }

    /**
     * Overrides the <code>java.lang.ThreadLocal#get()</code> method. Retrieves and returns the value wrapped up in a
     * <code>java.lang.ref.WeakReference</code> by the <code>SerializableThreadLocal#set(Object value)</code> method
     *
     * @return the value wrapped up in a weak reference
     */
    public Object get() {
        Object ref = super.get();
        if (ref == null) {
            return ref;
        } else {
            return ((WeakReference) ref).get();
        }
    }

    /**
     * Overrides the <code>java.lang.ThreadLoca#set(Object value)</code> method. Wraps the value in a
     * <code>java.lang.ref.WeakReference</code> before passing it on to the <code>java.lang.ThreadLocal#set(Object
     * value)</code>
     * method
     *
     * @param value the value that should be wrapped up in a weak reference
     */
    public void set(final Object value) {
        synchronized (this) {
            super.set(new WeakReference(value));
        }
    }
}