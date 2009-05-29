/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.joinpoint.impl;

import org.codehaus.aspectwerkz.joinpoint.CatchClauseSignature;
import org.codehaus.aspectwerkz.joinpoint.Signature;

/**
 * Implementation for the catch clause signature.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class CatchClauseSignatureImpl implements CatchClauseSignature {

    private Class m_exceptionType;

    /**
     * Creates a new catch clause signature.
     *
     * @param exceptionClass
     */
    public CatchClauseSignatureImpl(final Class exceptionClass) {
        m_exceptionType = exceptionClass;
    }

    /**
     * Returns the exception class.
     *
     * @return the declaring class
     */
    public Class getDeclaringType() {
        return m_exceptionType;
    }

    /**
     * Returns the modifiers for the signature. <p/>Could be used like this:
     * <p/>
     * <pre>
     * boolean isPublic = java.lang.reflect.Modifier.isPublic(signature.getModifiers());
     * </pre>
     *
     * @return the mofifiers
     */
    public int getModifiers() {
        return m_exceptionType.getModifiers();
    }

    /**
     * Returns the name
     *
     * @return
     */
    public String getName() {
        return m_exceptionType.getName();
    }

    /**
     * Returns the exception type.
     *
     * @return the parameter type
     * @deprecated
     */
    public Class getParameterType() {
        return m_exceptionType;
    }

    /**
     * Returns a string representation of the signature.
     *
     * @return a string representation
     */
    public String toString() {
        return getName();
    }

    /**
     * Creates a deep copy of the signature.
     *
     * @return a deep copy of the signature
     */
    public Signature newInstance() {
        return new CatchClauseSignatureImpl(m_exceptionType);
    }
}