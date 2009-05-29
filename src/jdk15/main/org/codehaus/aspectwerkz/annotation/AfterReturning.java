/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for after returning advice
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
@Target(ElementType.METHOD)
        @Retention(RetentionPolicy.RUNTIME)
        public @interface AfterReturning {
    /**
     * The pointcut expression to bind, when no type is specified for the returned value
     */
    String value() default "";

    /**
     * The pointcut expression to bind, when a type is specified for the returned value
     */
    String pointcut() default "";

    /**
     * The type pattern for the returned value
     */
    String type() default "";
}
