/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.joinpoint;

import org.codehaus.aspectwerkz.joinpoint.management.JoinPointType;
import org.codehaus.aspectwerkz.reflect.MemberInfo;

/**
 * Implements the join point concept, e.g. defines a well defined point in the program flow.
 * <p/>
 * Provides access to only static data, is therefore much more performant than the usage of the {@link
 * org.codehaus.aspectwerkz.joinpoint.JoinPoint} interface.
 *
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 */
public interface EnclosingStaticJoinPoint {
    /**
     * Returns the signature for the join point.
     *
     * @return the signature
     */
    Signature getSignature();
    
    /**
     * 
     * @return
     */
    JoinPointType getType();
}