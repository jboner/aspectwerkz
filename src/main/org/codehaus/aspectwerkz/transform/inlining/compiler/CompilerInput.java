/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.compiler;

import org.codehaus.aspectwerkz.transform.TransformationConstants;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class CompilerInput {

    public String joinPointClassName;

    public int joinPointInstanceIndex = TransformationConstants.INDEX_NOTAVAILABLE;

    public boolean isOptimizedJoinPoint = false;

    /**
     * Index on stack of the first target method arg (0 or 1, depends of static target or not
     */
    public int argStartIndex = TransformationConstants.INDEX_NOTAVAILABLE;

    public int callerIndex = TransformationConstants.INDEX_NOTAVAILABLE;
    public String callerClassSignature;

    public int calleeIndex = TransformationConstants.INDEX_NOTAVAILABLE;
    public String calleeClassSignature;

    /**
     * Returns a new instance that suits for proceed() ie where jp index is 0 etc.
     * @return
     */
    public CompilerInput getCopyForProceed() {
        CompilerInput input = new CompilerInput();
        input.joinPointClassName = joinPointClassName;
        input.calleeClassSignature = calleeClassSignature;
        input.callerClassSignature = callerClassSignature;

        // proceed() needs specific values
        input.isOptimizedJoinPoint = false;
        input.joinPointInstanceIndex = 0;
        return input;
    }
}
