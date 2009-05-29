/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect;

import org.codehaus.backport175.reader.Annotation;
import org.codehaus.backport175.reader.bytecode.AnnotationElement;

import java.util.List;

/**
 * Base interface for the reflection info hierarchy.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface ReflectionInfo {

    /**
     * Returns the name element.
     * If the element is an array class, its name is as a human writes it: java.lang.String[]
     *
     * @return the name of the element
     */
    String getName();

    /**
     * Returns the signature for the element.
     *
     * @return the signature for the element
     */
    String getSignature();

    /**
     * Returns the class modifiers.
     *
     * @return the class modifiers
     */
    int getModifiers();

    /**
     * Returns the annotations.
     *
     * @return the annotations
     */
    AnnotationElement.Annotation[] getAnnotations();
}