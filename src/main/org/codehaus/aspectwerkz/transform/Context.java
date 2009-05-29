/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform;

import org.objectweb.asm.Label;

import java.util.List;
import java.util.Set;

/**
 * Interface for the different transformation context implementations. FIXME crap: abstract method on an interface.
 * Refactor some in between if we are sure to keep the delegation model
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface Context {

    public String getClassName();

    /**
     * Sets the current bytecode.
     *
     * @param bytecode
     */
    public abstract void setCurrentBytecode(final byte[] bytecode);

    /**
     * Returns the initial bytecode.
     *
     * @return bytecode
     */
    public abstract byte[] getInitialBytecode();

    /**
     * Returns the current bytecode.
     *
     * @return bytecode
     */
    public abstract byte[] getCurrentBytecode();

    /**
     * Returns the class loader.
     *
     * @return the class loader
     */
    public abstract ClassLoader getLoader();

    /**
     * The definitions context (with hierarchical structure)
     *
     * @return
     */
    public abstract Set getDefinitions();

    /**
     * Marks the class being transformed as advised. The marker can at most be set once per class per transformer
     */
    public abstract void markAsAdvised();

    /**
     * Resets the isAdviced flag.
     */
    public abstract void resetAdvised();

    /**
     * Checks if the class being transformed has been advised.
     * This should only be used after ALL actual transformations.
     *
     * @return boolean
     */
    public abstract boolean isAdvised();

    /**
     * Marks the context as read-only.
     */
    public abstract void markAsReadOnly();

    /**
     * Checks if the context is read-only.
     *
     * @return boolean
     */
    public abstract boolean isReadOnly();

    /**
     * Returns meta-data for the transformation.
     *
     * @param key the key
     * @return the value
     */
    public abstract Object getMetaData(final Object key);

    /**
     * Adds new meta-data for the transformation.
     *
     * @param key   the key
     * @param value the value
     */
    public abstract void addMetaData(final Object key, final Object value);

    /**
     * Dump the class to specific directory.
     *
     * @param dir
     */
    public abstract void dump(String dir);

    /**
     * Tries to resolve the line number from the given label
     *
     * @param label
     * @return
     */
    abstract int resolveLineNumberInfo(Label label);

}