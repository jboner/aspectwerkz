/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.aspectj;

import org.codehaus.aspectwerkz.aspect.AdviceType;

/**
 * Struct for the AspectJ advice metadata.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @TODO should be immutable
 */
class AdviceInfo {
    AdviceType type;
    String aspectClassName;
    String adviceMethodName;
    String pointcut;
    int extraParameterFlags;
    String[] parameterTypes = new String[0];

    public String toString() {
        StringBuffer stringRepr = new StringBuffer().
                append('[').append(type).
                append(',').append(aspectClassName).
                append(',').append(adviceMethodName).
                append(',').append(pointcut).
                append(',').append(extraParameterFlags);
        for (int i = 0; i < parameterTypes.length; i++) {
            stringRepr.append(',').append(parameterTypes[i]);
        }
        stringRepr.append(']');
        return stringRepr.toString();
    }
}
