/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD-style license *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.joinpoint.impl;

import java.lang.reflect.Method;
import java.io.Serializable;

/**
 * Contains a pair of the original method and the wrapper method if such a method exists.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class MethodTuple implements Serializable {
    private final Method m_wrapperMethod;

    private final Method m_originalMethod;

    private final Class m_declaringClass;

    /**
     * @param wrapperMethod
     * @param originalMethod
     */
    public MethodTuple(Method wrapperMethod, Method originalMethod) {
        if (originalMethod == null) {
            originalMethod = wrapperMethod;
        }
        if (wrapperMethod.getDeclaringClass() != originalMethod.getDeclaringClass()) {
            throw new RuntimeException(
                    wrapperMethod.getName()
                    + " and "
                    + originalMethod.getName()
                    + " does not have the same declaring class"
            );
        }
        m_declaringClass = wrapperMethod.getDeclaringClass();
        m_wrapperMethod = wrapperMethod;
        m_wrapperMethod.setAccessible(true);
        m_originalMethod = originalMethod;
        m_originalMethod.setAccessible(true);
    }

    public boolean isWrapped() {
        return m_originalMethod != null;
    }

    public Class getDeclaringClass() {
        return m_declaringClass;
    }

    public Method getWrapperMethod() {
        return m_wrapperMethod;
    }

    public Method getOriginalMethod() {
        return m_originalMethod;
    }

    public String getName() {
        return m_wrapperMethod.getName();
    }
}