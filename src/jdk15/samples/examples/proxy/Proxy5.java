/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.proxy;

import org.codehaus.aspectwerkz.annotation.Before;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.proxy.Proxy;
import org.codehaus.aspectwerkz.intercept.Advisable;
import org.codehaus.aspectwerkz.intercept.BeforeAdvice;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class Proxy5 {

    static @interface AspectMarker {}

    public String getName() {
       return "AWProxy";
    }

    //@AspectMarker
    public void publicMethod() {
        System.out.println("publicMethod");
        protectedMethod();
    }

    @AspectMarker
    protected void protectedMethod() {
        System.out.println("protectedMethod");
        privateMethod();
    }

    @AspectMarker
    private void privateMethod() {
        System.out.println("privateMethod");
        publicFinalMethod();
    }

    @AspectMarker
    public final void publicFinalMethod() {
        System.out.println("publicFinalMethod");
    }

    public static void main(String args[]) throws Throwable {
        System.out.println("**** Use without proxy - only regular weaving may occur on non-proxy");
        Proxy5 me = new Proxy5();
        me.publicMethod();

        System.out.println("\n**** Use with proxy - both regular weaving and proxy weaving occur");
        // make it advisable
        Proxy5 meP = (Proxy5) Proxy.newInstance(Proxy5.class, true, true);
        System.out.println("I am : " + meP.getName());
        meP.publicMethod();

        System.out.println("\n**** Use with proxy - adding interceptor to publicMethod()");
        // do some per instance changes
        ((Advisable)meP).aw_addAdvice(
                "execution(* *.publicMethod(..))",
                new BeforeAdvice() {
                    public void invoke(JoinPoint jp) throws Throwable {
                        System.out.println("Intercept : " + jp.getSignature());
                    }
                }
        );
        meP.publicMethod();
    }

    /**
     * An aspect that is always there
     */
    public static class Aspect {
        @Before("execution(@examples.proxy.Proxy5$AspectMarker !static * examples.proxy.Proxy5.*(..))")
        void before(StaticJoinPoint jp) {
            System.out.println(jp.getType() + " : " + jp.getSignature());
        }
    }
}
