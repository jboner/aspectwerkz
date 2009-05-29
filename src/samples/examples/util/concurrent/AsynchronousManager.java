/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.util.concurrent;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;
import examples.util.definition.Definition;

/**
 * Manages the thread pool for all the asynchronous invocations.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AsynchronousManager {

    protected static final AsynchronousManager INSTANCE = new AsynchronousManager();

    protected PooledExecutor m_threadPool = null;

    protected boolean m_initialized = false;

    /**
     * Executes a task in a thread from the thread pool.
     *
     * @param task the task to execute (Runnable)
     */
    public void execute(final Runnable task) {
        if (notInitialized()) {
            throw new IllegalStateException("asynchronous thread pool not initialized");
        }
        try {
            m_threadPool.execute(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            notifyAll();
            throw new WrappedRuntimeException(e);
        } catch (Exception e) {
            throw new WrappedRuntimeException(e);
        }
    }

    /**
     * Returns the one A only AsynchronousManager instance.
     *
     * @return the asynchronous manager
     */
    public static AsynchronousManager getInstance() {
        return INSTANCE;
    }

    /**
     * Initializes the thread pool.
     *
     * @param def the definition
     */
    public synchronized void initialize(final Definition definition) {
        if (definition == null) {
            return;
        }
        if (m_initialized) {
            return;
        }
        examples.util.definition.ThreadPoolDefinition def = (examples.util.definition.ThreadPoolDefinition) definition;
        int threadPoolMaxSize = def.getMaxSize();
        int threadPoolInitSize = def.getInitSize();
        int threadPoolMinSize = def.getMinSize();
        int keepAliveTime = def.getKeepAliveTime();
        boolean waitWhenBlocked = def.getWaitWhenBlocked();
        boolean bounded = def.getBounded();
        if (threadPoolMaxSize < threadPoolInitSize || threadPoolMaxSize < threadPoolMinSize) {
            throw new IllegalArgumentException("max size of thread pool can not exceed the init size");
        }

        // if threadPoolMaxSize is -1 or less => no maximum limit
        // if keepAliveTime is -1 or less => threads are alive forever, i.e no timeout
        if (bounded) {
            createBoundedThreadPool(
                    threadPoolMaxSize,
                    threadPoolMinSize,
                    threadPoolInitSize,
                    keepAliveTime,
                    waitWhenBlocked
            );
        } else {
            createDynamicThreadPool(threadPoolMinSize, threadPoolInitSize, keepAliveTime);
        }
        m_initialized = true;
    }

    /**
     * Closes down the thread pool.
     */
    public void stop() {
        m_threadPool.shutdownNow();
    }

    /**
     * Creates a bounded thread pool.
     *
     * @param threadPoolMaxSize
     * @param threadPoolMinSize
     * @param threadPoolInitSize
     * @param keepAliveTime
     * @param waitWhenBlocked
     */
    protected void createBoundedThreadPool(final int threadPoolMaxSize,
                                           final int threadPoolMinSize,
                                           final int threadPoolInitSize,
                                           final int keepAliveTime,
                                           final boolean waitWhenBlocked) {
        m_threadPool = new PooledExecutor(new BoundedBuffer(threadPoolInitSize), threadPoolMaxSize);
        m_threadPool.setKeepAliveTime(keepAliveTime);
        m_threadPool.createThreads(threadPoolInitSize);
        m_threadPool.setMinimumPoolSize(threadPoolMinSize);
        if (waitWhenBlocked) {
            m_threadPool.waitWhenBlocked();
        }
    }

    /**
     * Creates a dynamic thread pool.
     *
     * @param threadPoolMinSize
     * @param threadPoolInitSize
     * @param keepAliveTime
     */
    protected void createDynamicThreadPool(final int threadPoolMinSize,
                                           final int threadPoolInitSize,
                                           final int keepAliveTime) {
        m_threadPool = new PooledExecutor(new LinkedQueue());
        m_threadPool.setKeepAliveTime(keepAliveTime);
        m_threadPool.createThreads(threadPoolInitSize);
        m_threadPool.setMinimumPoolSize(threadPoolMinSize);
    }

    /**
     * Checks if the service has been initialized.
     *
     * @return boolean
     */
    protected boolean notInitialized() {
        return !m_initialized;
    }

    /**
     * Private constructor.
     */
    protected AsynchronousManager() {
    }
}