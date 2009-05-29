/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.annotation;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AnnotationLoadingTarget {

    public static final int FIELD = 1;

    /**
     * @Complex(
     *  klass=test.annotation.AnnotationLoadingTarget.class,
     *  field=test.annotation.AnnotationLoadingTarget.FIELD,
     *  klass2={test.annotation.AnnotationLoadingTarget[].class, test.annotation.AnnotationLoadingTarget.class}
     * )
     */
    public void annotatedMethod() {
    }
}
