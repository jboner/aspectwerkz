/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aopc;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public abstract class BaseCallable {
    public String m_logString = "";

    public void debug() {
//        System.out.println("CallablePrototype.debug");
//        System.out.println(" this.getClass().getName() = " + this.getClass().getName());
//        System.out.println(" this.getClass().getClassLoader() = " +
//                this.getClass().getClassLoader());
//        System.out.println("logString = " + getLogString());
    }

    public String getLogString() {
        return m_logString;
    }

    public void setLogString(String s) {
        m_logString = s;
    }

    public void log(String s) {
        m_logString += s;
    }
}