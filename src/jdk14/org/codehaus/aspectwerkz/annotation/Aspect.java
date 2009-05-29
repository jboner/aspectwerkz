/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.annotation;

/**
 * Annotation for Aspect (optional)
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public interface Aspect {
    /**
     * Deployment model, when no aspect name is specified
     *
     * @org.codehaus.backport175.DefaultValue("perJVM")
     */
    String value();

    /**
     * Deployment model, when aspect name is specified
     *
     * @org.codehaus.backport175.DefaultValue("perJVM")
     */
    String deploymentModel();

    /**
     * Aspect name (defaults to fully qualified name of aspect class)
     */
    String name();
}
