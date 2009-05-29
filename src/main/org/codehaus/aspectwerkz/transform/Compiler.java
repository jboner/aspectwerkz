/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform;

/**
 * Generic interface for the code generation compilers used in the AspectWerkz weaver.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface Compiler {

    /**
     * Compiles the code and returns the bytecode for the compiled code.
     *
     * @return the bytecode for the compiled code
     */
    public byte[] compile();
}
