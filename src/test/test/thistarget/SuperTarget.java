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
public abstract class SuperTarget {

    public SuperTarget() {
        TargetTest.log("SuperTarget");
        TargetTest.logCtorExe("SuperTarget");
    }

    public void target() {
        TargetTest.log("SuperTarget");
    }

    public void call() {
        TargetTest.log("SuperTarget");
    }

    public abstract void targetAbstract();

    public abstract void callAbstract();


}
