/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect;


/**
 * Marker interface for staticinitializer member info. 
 * There cannot be other exposed info than the declaring type.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 */
public interface StaticInitializationInfo extends MemberInfo {

}
