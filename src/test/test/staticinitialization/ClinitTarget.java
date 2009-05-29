/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.staticinitialization;


/**
 * Target for staticinitialization pointcuts.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 * 
 * @test.staticinitialization.StaticInitializationService
 */
public class ClinitTarget {
	static {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.CLINIT_EXECUTION_MESSAGE);
	}
}
