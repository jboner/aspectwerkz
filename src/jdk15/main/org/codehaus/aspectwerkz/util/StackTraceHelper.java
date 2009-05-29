/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Utility methods for dealing with stack traces.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public final class StackTraceHelper {

    /**
     * Removes the AspectWerkz specific elements from the stack trace.
     *
     * @param exception the Throwable to modify the stack trace on
     * @param className the name of the fake origin class of the exception
     */
    public static void hideFrameworkSpecificStackTrace(final Throwable exception, final String className) {
        if (exception == null) {
            throw new IllegalArgumentException("exception can not be null");
        }
        if (className == null) {
            throw new IllegalArgumentException("class name can not be null");
        }
        final List newStackTraceList = new ArrayList();
        final StackTraceElement[] stackTrace = exception.getStackTrace();
        int i;
        for (i = 1; i < stackTrace.length; i++) {
            if (stackTrace[i].getClassName().equals(className)) {
                break;
            }
        }
        for (int j = i; j < stackTrace.length; j++) {
            newStackTraceList.add(stackTrace[j]);
        }
        final StackTraceElement[] newStackTrace = new StackTraceElement[newStackTraceList.size()];
        int k = 0;
        for (Iterator it = newStackTraceList.iterator(); it.hasNext(); k++) {
            final StackTraceElement element = (StackTraceElement) it.next();
            newStackTrace[k] = element;
        }
        exception.setStackTrace(newStackTrace);
    }
}