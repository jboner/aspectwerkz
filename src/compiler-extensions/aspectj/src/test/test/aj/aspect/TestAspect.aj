/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aj.aspect;

import test.aj.Test;

/**
 * Test aspect for the AspectJ compiler extension to AspectWerkz.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public aspect TestAspect {

	before(): execution(int test.aj.Test.before()) {
        Test.log("before ");
	}

	after(): execution(void test.aj.Test.afterFinally()) {
        Test.log("after-finally ");
	}

	after() throwing(java.lang.RuntimeException e): execution(String test.aj.Test.afterThrowing()) {
        Test.log("after-throwing ");
	}

	after() returning(java.lang.String s): execution(Object test.aj.Test.afterReturning()) {
        Test.log("after-returning ");
	}

	Object around(): execution(long test.aj.Test.around()) {
        Test.log("before-around ");
		Object result = proceed();
        Test.log("after-around ");
		return result;
	}
}
