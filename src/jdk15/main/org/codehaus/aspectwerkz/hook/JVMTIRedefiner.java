/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

import org.codehaus.aspectwerkz.transform.inlining.deployer.Redefiner;
import org.codehaus.aspectwerkz.transform.inlining.deployer.ChangeSet;
import org.codehaus.aspectwerkz.transform.inlining.compiler.JoinPointFactory;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;

import java.util.Iterator;
import java.lang.instrument.ClassDefinition;

/**
 * Redefines classes using Java 5 HotSwap.
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class JVMTIRedefiner implements Redefiner {

    public JVMTIRedefiner() {
        if (!Agent.getInstrumentation().isRedefineClassesSupported()) {
            throw new UnsupportedOperationException("This Java 5 does not support JVMTI redefine()");
        }
    }

    /**
     * Redefines all classes affected by the change set according to the rules defined in the change set.
     *
     * @param changeSet
     */
    public void redefine(final ChangeSet changeSet) {
        if (! Agent.getInstrumentation().isRedefineClassesSupported()) {
            //TODO - should we fail ?
            return;
        }
        ClassDefinition[] changes = new ClassDefinition[changeSet.getElements().size()];
        int index = 0;
        for (Iterator it = changeSet.getElements().iterator(); it.hasNext(); index++) {
            ChangeSet.Element changeSetElement = (ChangeSet.Element) it.next();
            final byte[] bytecode = JoinPointFactory.redefineJoinPoint(changeSetElement.getCompilationInfo());
            changes[index] = new ClassDefinition(changeSetElement.getJoinPointInfo().getJoinPointClass(), bytecode);
        }
        try {
            Agent.getInstrumentation().redefineClasses(changes);
        } catch (Exception e) {
            throw new WrappedRuntimeException(e);
        }
    }
}
