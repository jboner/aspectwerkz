/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.util.definition;

import examples.util.definition.Definition;

/**
 * Definition for the asynchronous concern.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ThreadPoolDefinition implements Definition {

    public int getMaxSize() {
        return m_maxSize;
    }

    public void setMaxSize(final int maxSize) {
        m_maxSize = maxSize;
    }

    public int getMinSize() {
        return m_minSize;
    }

    public void setMinSize(final int minSize) {
        m_minSize = minSize;
    }

    public int getInitSize() {
        return _initSize;
    }

    public void setInitSize(final int initSize) {
        _initSize = initSize;
    }

    public int getKeepAliveTime() {
        return m_keepAliveTime;
    }

    public void setKeepAliveTime(final int keepAliveTime) {
        m_keepAliveTime = keepAliveTime;
    }

    public boolean getWaitWhenBlocked() {
        return m_waitWhenBlocked;
    }

    public void setWaitWhenBlocked(final boolean waitWhenBlocked) {
        m_waitWhenBlocked = waitWhenBlocked;
    }

    public boolean getBounded() {
        return m_bounded;
    }

    public void setBounded(final boolean bounded) {
        m_bounded = bounded;
    }

    private int m_maxSize;

    private int m_minSize;

    private int _initSize;

    private int m_keepAliveTime;

    private boolean m_waitWhenBlocked = true;

    private boolean m_bounded = true;
}