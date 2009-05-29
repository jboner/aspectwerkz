/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.expression;

/**
 * Helper class to have boolean operation on true / false and null, null is assumed to be undetermined, and "not null"="null"
 * A "false && null" will stay false, but a "true && null" will become undetermined (null).
 *
 * <p/>
 * This is used when the expression cannot be resolved entirely (early matching, cflow, runtime check residuals)
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public abstract class Undeterministic {

    /**
     * And operation
     *
     * @param lhs
     * @param rhs
     * @return
     */
    public static Boolean and(Boolean lhs, Boolean rhs) {
        if (lhs != null && rhs != null) {
            // regular AND
            if (lhs.equals(Boolean.TRUE) && rhs.equals(Boolean.TRUE)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else if (lhs != null && lhs.equals(Boolean.FALSE)) {
            // one is undetermined and the other is false, so result is false
            return Boolean.FALSE;
        } else if (rhs != null && rhs.equals(Boolean.FALSE)) {
            // one is undetermined and the other is false, so result is false
            return Boolean.FALSE;
        } else {
            // both are undetermined, or one is true and the other undetermined
            return null;
        }
    }

    /**
     * Or operation
     *
     * @param lhs
     * @param rhs
     * @return
     */
    public static Boolean or(Boolean lhs, Boolean rhs) {
        if (lhs != null && rhs != null) {
            // regular OR
            if (lhs.equals(Boolean.TRUE) || rhs.equals(Boolean.TRUE)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            // one or both is/are undetermined
            // OR cannot be resolved
            return null;
        }
    }

    /**
     * Not operation
     *
     * @param b
     * @return
     */
    public static Boolean not(Boolean b) {
        if (b != null) {
            // regular NOT
            if (b.equals(Boolean.TRUE)) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } else {
            return null;
        }
    }


}
