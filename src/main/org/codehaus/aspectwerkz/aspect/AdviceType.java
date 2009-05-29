/* ***********************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 * ************************************************************************************/
package org.codehaus.aspectwerkz.aspect;

import java.io.Serializable;

/**
 * Type-safe enum for the advice types.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AdviceType implements Serializable {

    public static final AdviceType AROUND = new AdviceType("AROUND");
    public static final AdviceType BEFORE = new AdviceType("BEFORE");
    public static final AdviceType AFTER = new AdviceType("AFTER");
    public static final AdviceType AFTER_FINALLY = new AdviceType("AFTER_FINALLY");
    public static final AdviceType AFTER_RETURNING = new AdviceType("AFTER_RETURNING");
    public static final AdviceType AFTER_THROWING = new AdviceType("AFTER_THROWING");

    private final String m_name;

    private AdviceType(String name) {
        m_name = name;
    }

    public String toString() {
        return m_name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdviceType)) {
            return false;
        }
        final AdviceType adviceType = (AdviceType) o;
        if ((m_name != null) ? (!m_name.equals(adviceType.m_name)) : (adviceType.m_name != null)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((m_name != null) ? m_name.hashCode() : 0);
    }
}