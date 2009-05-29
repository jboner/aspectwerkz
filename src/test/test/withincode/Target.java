/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.withincode;

import test.handler.HandlerTestBeforeException;


/**
 * Target for test with withincode(staticinitialization)
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 * 
 * @test.withincode.WithincodeClinit
 */
public class Target {
    private static CtorCallTarget s_field;
    
	static {
	    s_field = new CtorCallTarget(); // SET && CALL(CTOR)
	    
	    if(null != s_field) {	// GET
	        ;
	    }
	    
	    try {
	    	staticMethod();
	    } catch(HandlerTestBeforeException htbe) {
	        ;
	    }
	}
	
	public static final void staticMethod() throws HandlerTestBeforeException {
	    throw new HandlerTestBeforeException();
	}
	
	private static class CtorCallTarget {
	    public CtorCallTarget() {
	    }
	}
}
