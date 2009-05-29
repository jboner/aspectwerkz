/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.annotation;

/**
 * Mixin annotation
 * Annotate the mixin implementation class
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public interface Mixin {
    /**
     * Pointcut the mixin applies to (within / hasMethod / hasField)
     * When used, all others elements are assumed to their default value
     */
    public String value();

    /**
     * Pointcut the mixin applies to (within / hasMethod / hasField)
     * Used when deploymentModel / isTransient is specified
     */
    public String pointcut();

    /**
     * Mixin deployment model.
     * Defaults to "perInstance". Only "perClass" and "perInstance" are supported for now
     *
     * @org.codehaus.backport175.DefaultValue("perInstance")
     *
     * @see org.codehaus.aspectwerkz.DeploymentModel
     */
    public String deploymentModel();

    /**
     * True if mixin should behave as transient and not be serialized alongside the class it is introduced to.
     * Defaults to false.
     *
     * @org.codehaus.backport175.DefaultValue(false)
     */
    public boolean isTransient();
}
