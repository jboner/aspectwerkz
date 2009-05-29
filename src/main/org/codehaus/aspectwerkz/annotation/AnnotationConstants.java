/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.annotation;

/**
 * Defines doclet nicknames for Java 1.3/1.4 annotations in JavaDoc and annotation implementations
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public abstract class AnnotationConstants {

    private static final String PACKAGE = "org.codehaus.aspectwerkz.annotation.";

    static final String ASPECT_DOCLET = "Aspect";
    static final String ASPECT = PACKAGE + ASPECT_DOCLET;

    static final String AROUND_DOCLET = "Around";
    static final String AROUND = PACKAGE + AROUND_DOCLET;

    static final String BEFORE_DOCLET = "Before";
    static final String BEFORE = PACKAGE + BEFORE_DOCLET;

    static final String AFTER_DOCLET = "After";
    static final String AFTER = PACKAGE + AFTER_DOCLET;

    static final String AFTER_FINALLY_DOCLET = "AfterFinally";
    static final String AFTER_FINALLY = PACKAGE + AFTER_FINALLY_DOCLET;

    static final String AFTER_RETURNING_DOCLET = "AfterReturning";
    static final String AFTER_RETURNING = PACKAGE + AFTER_RETURNING_DOCLET;

    static final String AFTER_THROWING_DOCLET = "AfterThrowing";
    static final String AFTER_THROWING = PACKAGE + AFTER_THROWING_DOCLET;

    static final String EXPRESSION_DOCLET = "Expression";
    static final String EXPRESSION = PACKAGE + EXPRESSION_DOCLET;

    static final String INTRODUCE_DOCLET = "Introduce";
    static final String INTRODUCE = PACKAGE + INTRODUCE_DOCLET;

    static final String MIXIN_DOCLET = "Mixin";
    static final String MIXIN = PACKAGE + MIXIN_DOCLET;

}
