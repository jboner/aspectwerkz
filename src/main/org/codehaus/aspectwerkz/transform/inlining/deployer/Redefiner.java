/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.deployer;

/**
 * Interface that all "redefiner" implementations should implement.
 * <p/>
 * Redefines all classes at all points defined by the <code>ChangeSet</code> passed to the
 * <code>redefine</code> method.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface Redefiner {

    /**
     * Redefines all classes affected by the change set according to the rules defined in the change set.
     *
     * @param changeSet
     */
    void redefine(ChangeSet changeSet);
}
