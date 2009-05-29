/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.definition;

/**
 * Holds the meta-data for the pointcuts.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class PointcutDefinition {
    /**
     * The expression.
     */
    private final String m_expression;

    /**
     * Creates a new pointcut definition instance.
     *
     * @param expression
     */
    public PointcutDefinition(final String expression) {
        m_expression = expression;
    }

    /**
     * Returns the expression for the pointcut.
     *
     * @return the expression for the pointcut
     */
    public String getExpression() {
        return m_expression;
    }
}