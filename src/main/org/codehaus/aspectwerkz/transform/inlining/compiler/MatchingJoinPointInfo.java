/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.compiler;

import org.codehaus.aspectwerkz.expression.ExpressionContext;

/**
 * Holds info sufficient for picking out the join points we are interested in advising.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
final public class MatchingJoinPointInfo {
    private final Class m_joinPointClass;
    private final CompilationInfo m_compilationInfo;
    private final ExpressionContext m_expressionContext;

    public MatchingJoinPointInfo(final Class joinPointClass,
                                 final CompilationInfo compilationInfo,
                                 final ExpressionContext expressionContext) {
        m_joinPointClass = joinPointClass;
        m_compilationInfo = compilationInfo;
        m_expressionContext = expressionContext;
    }

    public Class getJoinPointClass() {
        return m_joinPointClass;
    }

    public CompilationInfo getCompilationInfo() {
        return m_compilationInfo;
    }

    public ExpressionContext getExpressionContext() {
        return m_expressionContext;
    }

    public int hashCode() {
        return m_compilationInfo.hashCode();
    }

    public boolean equals(Object o) {
        return ((MatchingJoinPointInfo) o).m_compilationInfo == m_compilationInfo;
    }
}