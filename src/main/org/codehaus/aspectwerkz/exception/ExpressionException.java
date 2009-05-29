/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.exception;

/**
 * Thrown when error in expression AST evaluation or creation.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ExpressionException extends RuntimeException {
    /**
     * Sets the message for the exception.
     *
     * @param message the message
     */
    public ExpressionException(final String message) {
        super(message);
    }
}