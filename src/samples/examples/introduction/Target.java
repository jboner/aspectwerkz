/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.introduction;

/**
 * @examples.annotation.Annotation
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Target {
    public static void main(String[] args) {
        Target target = new Target();
        System.out.println("The mixin says: " + ((Mixin) target).sayHello1());
        System.out.println("The mixin says: " + ((Mixin) target).sayHello2());
        Target target2 = new Target();
        System.out.println("The mixin says: " + ((Mixin) target2).sayHello1());
        System.out.println("The mixin says: " + ((Mixin) target2).sayHello2());
    }
}