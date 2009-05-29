/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.cflow;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Target {

    public void step1() {
        System.out.println("  --> invoking step1");
        step2();
    }

    public void step2() {
        System.out.println("  --> invoking step2");
    }

    public static void main(String[] args) {
        Target target = new Target();
        System.out.println("\n--------------------------");
        System.out.println("step2 is called in the cflow of step1 => should trigger the advice");
        target.step1();
        System.out.println("\n--------------------------");
        System.out
                .println("step2 is called directly (not in cflow of step1) => should NOT trigger the advice");
        target.step2();
    }
}