/***************************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved. *
 * http://aspectwerkz.codehaus.org *
 * ---------------------------------------------------------------------------------- * The software
 * in this package is published under the terms of the LGPL license * a copy of which has been
 * included with this distribution in the license.txt file. *
 **************************************************************************************************/
package test.inheritedmixinbug;

import junit.framework.TestCase;

public class Target extends TestCase {
    public Target() {
    }

    public Target(String name) {
        super(name);
    }

    public void testInstanceOf() {
        assertTrue(new Target() instanceof Aintf);
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(Target.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}