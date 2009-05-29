/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect;

/**
 * Interface for the method info implementations.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface MethodInfo extends MemberInfo {
    /**
     * Returns the return type.
     *
     * @return the return type
     */
    ClassInfo getReturnType();

    /**
     * Returns the parameter types.
     *
     * @return the parameter types
     */
    ClassInfo[] getParameterTypes();

    /**
     * Returns the parameter names as they appear in the source code.
     * This information is available only when class are compiled with javac -g (debug info), but is required
     * for Aspect that are using args() and target()/this() bindings.
     * <p/>
     * It returns null if not available.
     *
     * @return
     */
    String[] getParameterNames();

    /**
     * Returns the exception types.
     *
     * @return the exception types
     */
    ClassInfo[] getExceptionTypes();

}