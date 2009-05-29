/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect;

/**
 * Interface for the constructor info implementations.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface ConstructorInfo extends MemberInfo {
    /**
     * Returns the parameter types.
     *
     * @return the parameter types
     */
    ClassInfo[] getParameterTypes();

    /**
     * Returns the exception types.
     *
     * @return the exception types
     */
    ClassInfo[] getExceptionTypes();
}