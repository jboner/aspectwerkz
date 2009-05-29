/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.joinpoint;

import java.io.Serializable;

/**
 * Provides static and reflective information about the join point.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface Signature extends Serializable {
    /**
     * Returns the declaring class.
     *
     * @return the declaring class
     */
    Class getDeclaringType();

    /**
     * Returns the modifiers for the signature. <p/>Could be used like this:
     * <p/>
     * <pre>
     * boolean isPublic = java.lang.reflect.Modifier.isPublic(signature.getModifiers());
     * </pre>
     *
     * @return the mofifiers
     */
    int getModifiers();

    /**
     * Returns the name (f.e. name of method of field).
     *
     * @return
     */
    String getName();

}