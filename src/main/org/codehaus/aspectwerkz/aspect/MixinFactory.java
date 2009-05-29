/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.aspect;

/**
 * Interface for that all mixin factories must implement.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface MixinFactory {

    /**
     * Creates a new perJVM mixin instance, if it already exists then return it.
     *
     * @return the mixin instance
     */
    Object mixinOf();

    /**
     * Creates a new perClass mixin instance, if it already exists then return it.
     *
     * @param klass
     * @return the mixin instance
     */
    Object mixinOf(Class klass);

    /**
     * Creates a new perInstance mixin instance, if it already exists then return it.
     *
     * @param instance
     * @return the mixin instance
     */
    Object mixinOf(Object instance);
}