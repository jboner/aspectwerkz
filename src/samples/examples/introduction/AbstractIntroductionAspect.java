/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.introduction;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public abstract class AbstractIntroductionAspect {

    /**
     * The Mixin doclet is not necessary here. This aspect provides a half completed mixin impl
     * (abstract one)
     */
    public static abstract class MyImpl implements Mixin {
        public String sayHello1() {
            return "Hello World!";
        }
    }
}