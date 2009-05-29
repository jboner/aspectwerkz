/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.deployer;

import java.util.Set;
import java.util.HashSet;

import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilationInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.MatchingJoinPointInfo;

/**
 * Represents a change set of changes to be made to the class graph.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public final class ChangeSet {
    private final Set m_set = new HashSet();

    /**
     * Adds a change set element.
     *
     * @param element
     */
    public void addElement(final Element element) {
        m_set.add(element);
    }

    /**
     * Returns all elements in the change set.
     *
     * @return all elements in the change set
     */
    public Set getElements() {
        return m_set;
    }

    /**
     * Represents a change to be made to the class graph.
     *
     * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
     */
    public static class Element {
        private final CompilationInfo m_compilationInfo;
        private final MatchingJoinPointInfo m_joinPointInfo;

        public Element(final CompilationInfo compilationInfo, final MatchingJoinPointInfo joinPointInfo) {
            m_compilationInfo = compilationInfo;
            m_joinPointInfo = joinPointInfo;
        }

        public CompilationInfo getCompilationInfo() {
            return m_compilationInfo;
        }

        public MatchingJoinPointInfo getJoinPointInfo() {
            return m_joinPointInfo;
        }
    }
}