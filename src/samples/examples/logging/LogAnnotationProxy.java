/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.logging;

/**
 * The 'log' annotation proxy.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface LogAnnotationProxy {
    public static final int INFO = 0;

    public static final int ERROR = 1;

    public static final int WARNING = 2;

    public int level();

    public float flt();

    public int iconstant();

    public String sconstant();

    public double[] darr();

    public String[] sarr();

}