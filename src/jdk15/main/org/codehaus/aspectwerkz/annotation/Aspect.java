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
 * Annotation for Aspect (optional)
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
@Target(ElementType.TYPE)
        @Retention(RetentionPolicy.RUNTIME)
        public @interface Aspect {
    /**
     * Deployment model, when no aspect name is specified
     */
    String value() default "perJVM";//TODO should be an enum that maps to DeploymentModel

    /**
     * Deployment model, when aspect name is specified
     */
    String deploymentModel() default "perJVM";

    /**
     * Aspect name (defaults to fully qualified name of aspect class)
     */
    String name() default "";
}
