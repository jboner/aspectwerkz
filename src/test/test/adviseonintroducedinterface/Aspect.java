/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.adviseonintroducedinterface;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér</a>
 */
public class Aspect {

    /**
     * @Before(" (execution(void test.adviseonintroducedinterface.Intf1+.m1())
     * ||
     * execution(void test.adviseonintroducedinterface.Intf2+.m2()))
     * &&
     * !within(test.adviseonintroducedinterface.Aspect$Mixin)
     * ")
     */
    public void before(JoinPoint jp) {
        Test.log("before ");
    }


    /**
     * @Introduce within(test.adviseonintroducedinterface.Target)
     */
    Intf1 marker;

    public static class Mixin implements Intf2 {
        public void m2() {
            Test.log("m2 ");
        }
    }
}
