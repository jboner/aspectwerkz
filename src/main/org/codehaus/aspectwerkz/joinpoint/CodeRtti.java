/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.joinpoint;

/**
 * Interface for the code RTTI (Runtime Type Information).
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface CodeRtti extends MemberRtti {
    /**
     * Returns the exception types declared by the code block.
     *
     * @return the exception types
     */
    Class[] getExceptionTypes();

    /**
     * Returns the parameter types.
     *
     * @return the parameter types
     */
    Class[] getParameterTypes();

    /**
     * Returns the values of the parameters.
     *
     * @return the values of the parameters
     */
    Object[] getParameterValues();

    /**
     * @param parameterValues
     * @TODO remove in 2.0
     * <p/>
     * Sets the values of the parameters.
     */
    void setParameterValues(Object[] parameterValues);
}