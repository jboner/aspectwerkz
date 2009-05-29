/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.thistarget;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class Target {

    public Target() {
        TargetTest.log("Target");
        TargetTest.logCtorExe("Target");
    }

    public void target() {
        TargetTest.log("Target");
    }

    public void call() {
        TargetTest.log("Target");
    }
}
