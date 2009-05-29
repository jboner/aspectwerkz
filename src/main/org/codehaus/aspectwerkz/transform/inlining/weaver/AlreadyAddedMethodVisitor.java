/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.weaver;

import java.util.Set;

import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.transform.inlining.AsmNullAdapter;
import org.objectweb.asm.MethodVisitor;

/**
 * A read only visitor to gather wrapper methods and proxy methods
 * Makes use of the NullVisitors
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class AlreadyAddedMethodVisitor extends AsmNullAdapter.NullClassAdapter implements TransformationConstants {

    /**
     * Set of "<methodName><methodDesc>" strings populated with wrapper methods, prefixed originals
     * and ctor body wrappers to allow multiweaving support.
     */
    private final Set m_addedMethods;

    /**
     * Creates a new class adapter.
     *
     * @param wrappers
     */
    public AlreadyAddedMethodVisitor(final Set wrappers) {
        m_addedMethods = wrappers;
    }

    /**
     * Visits the methods.
     *
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    public MethodVisitor visitMethod(final int access,
                                   final String name,
                                   final String desc,
                                   final String signature,
                                   final String[] exceptions) {
        if (name.startsWith(WRAPPER_METHOD_PREFIX) ||
            name.startsWith(ORIGINAL_METHOD_PREFIX)) {
            //FIXME do it for ctor exe wrapper
            m_addedMethods.add(getMethodKey(name, desc));
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    /**
     * Returns the key of the method.
     *
     * @param name
     * @param desc
     * @return
     */
    static String getMethodKey(final String name, final String desc) {
        StringBuffer sb = new StringBuffer(name);
        return sb.append(desc).toString();
    }
}
