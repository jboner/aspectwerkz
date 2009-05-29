/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.joinpoint;

import java.lang.reflect.Method;

/**
 * Interface for the method signature.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface MethodSignature extends CodeSignature {
    /**
     * Returns the method.
     *
     * @return the method
     */
    Method getMethod();

    /**
     * Returns the return type.
     *
     * @return the return type
     */
    Class getReturnType();
}