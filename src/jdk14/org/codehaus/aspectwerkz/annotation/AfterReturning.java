/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.annotation;

/**
 * Annotation for after returning advice
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public interface AfterReturning {
    /**
     * The pointcut expression to bind, when no type is specified for the returned value
     */
    String value();

    /**
     * The pointcut expression to bind, when a type is specified for the returned value
     */
    String pointcut();

    /**
     * The type pattern for the returned value
     */
    String type();
}
