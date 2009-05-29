/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.handler;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.CatchClauseRtti;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class HandlerTestAspect {

    public void before(final JoinPoint joinPoint) throws Throwable {
        HandlerTest.log("before ");
        // AW-276 access the rtti
        Throwable t = (Throwable) ((CatchClauseRtti) joinPoint.getRtti()).getParameterValue();
        if (t == null) {
            TestCase.fail("handler join point has invalid rttit");
        }
    }

    public void before2(final StaticJoinPoint staticJoinPoint) throws Throwable {
        HandlerTest.log("before2 ");
        Class exception = staticJoinPoint.getCalleeClass();
        if (exception == null) {
            TestCase.fail("handler join point has invalid rttit");
        }
    }

    public void before3(final JoinPoint joinPoint, HandlerTestBeforeException e) throws Throwable {
        HandlerTest.log("before3 ");
        if (e == null) {
            TestCase.fail("handler join point has invalid rttit");
        }
    }
}