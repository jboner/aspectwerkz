/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.intercept.call;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Callee {

    public void adviseWithAround() {
        InterceptTest.log("adviseWithAround ");
    }

    public void adviseWithAround2() {
        InterceptTest.log("adviseWithAround2 ");
    }

    public void adviseWithAroundStack() {
        InterceptTest.log("adviseWithAroundStack ");
    }

    public void adviseWithBefore() {
        InterceptTest.log("adviseWithBefore ");
    }

    public void adviseWithAfter() {
        InterceptTest.log("adviseWithAfter ");
    }

    public Object adviseWithAfterReturning() {
        InterceptTest.log("adviseWithAfterReturning ");
        return "returnValue";
    }

    public void adviseWithAfterThrowing() {
        InterceptTest.log("adviseWithAfterThrowing ");
        throw new RuntimeException("noop");
    }
}
