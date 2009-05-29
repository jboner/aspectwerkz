/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD-style license *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.constructor;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class TestAroundAdvice {
    public TestAroundAdvice(long l, Object o, String[] arr) {
        ConstructorAdviceTest.logCall("init ");
        ConstructorAdviceTest.logExecution("init ");

        //        System.out.println(this);
        //        (new Exception()).printStackTrace();
    }
}