/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.annotation;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class Target {

    public static void main(String args[]) throws Throwable {
        System.out.println("examples.annotation.Target.main");
        Target me = new Target();
        me.targetAB();
        me.targetA();
        me.target();
    }

    /**
     * @examples.annotation.AnnotationA(some)
     * @examples.annotation.AnnotationB
     */
    public void targetAB() {
        System.out.println("Target.target AB ");
    }

    /**
     * @examples.annotation.AnnotationA
     */
    public void targetA() {
        System.out.println("Target.target A");
    }

    public void target() {
        System.out.println("Target.target");
    }

    public static class AnnotationMatchAspect {

        /**
         * @param jp
         * @Before execution(@examples.annotation.AnnotationA * examples.annotation.Target.*(..))
         */
        public void beforeA(JoinPoint jp) {
            System.out.println("Target$AnnotationMatchAspect.beforeA : " + jp.toString() );

        }

        /**
         * @param jp
         * @Before execution(@examples.annotation.AnnotationB * examples.annotation.Target.*(..))
         */
        public void beforeB(JoinPoint jp) {
            System.out.println("Target$AnnotationMatchAspect.beforeB");


        }
    }

}
