/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package yapbaop.demo;

import org.codehaus.aspectwerkz.proxy.Proxy;
import org.codehaus.aspectwerkz.intercept.Advisable;
import org.codehaus.aspectwerkz.intercept.Advice;
import org.codehaus.aspectwerkz.intercept.AfterThrowingAdvice;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import yapbaop.core.Yapbaop;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class YapbaopDemo {

    static int COUNT = 0;

    String m_name;

    public YapbaopDemo() {
        m_name = "YapbaopDemo-" + COUNT++;
    }

    public void method() {
        System.out.println(m_name + " .method");
    }

    public void canThrow(boolean doThrow) {
        System.out.println(m_name + " .canThrow");
        if (doThrow)
            throw new RuntimeException("Was asked to throw");
    }

    public static void main(String args[]) throws Throwable {

        // no aspect
        System.out.println(" ( no aspect )");
        YapbaopDemo me0 = new YapbaopDemo();
        me0.method();

        System.out.println(" ( bind a new aspect )");
        Yapbaop.Handle handle = Yapbaop.bindAspect(DemoAspect.class, "* yapbaop.demo.YapbaopDemo.*(..)");
        YapbaopDemo me1 = (YapbaopDemo) Proxy.newInstance(YapbaopDemo.class);
        me1.method();

        handle.unbind();

        // get a new one but not using the proxy cache then..
        System.out.println(" ( unbind it and get a new proxy YapbaopDemo-2)");
        YapbaopDemo me2 = (YapbaopDemo) Proxy.newInstance(YapbaopDemo.class, false, false/*not advisable*/);
        me1.method();// still has advice
        me2.method();// no advice

        // lets add some per instance interceptor now
        // don't use the cache, and ensure we have an advisable version
        System.out.println(" ( real per instance interception, lets add an afterThrowing on YapbaopDemo-3..)");
        YapbaopDemo me3 = (YapbaopDemo) Proxy.newInstance(YapbaopDemo.class, false, true/*IS advisable*/);
        me3.method();// nothing happen

        // note here that composition is not allowed for now since only execution pointcut are valid
        ((Advisable)me3).aw_addAdvice(
                "execution(* *.canThrow(..))",
                new AfterThrowingAdvice() {
                    public void invoke(JoinPoint joinPoint, Throwable throwable) throws Throwable {
                        System.out.print("afterThrowing on ");
                        System.out.print(((YapbaopDemo)joinPoint.getTarget()).m_name);
                        System.out.println(" : afterThrowing on " + joinPoint.getSignature().toString());
                        System.out.println("   exception is " + throwable.getClass().getName()
                                + " / " + throwable.getMessage());
                    }
                }
        );
        me3.canThrow(false);// nothing
        System.out.println(" ( nothing happen on YapbaopDemo-2 off course)");
        try {
            me2.canThrow(true);// after throwing NOT triggered !! this is me2
        } catch (Throwable t) {
            System.out.println("got " + t.getClass().getName() + " / " + t.getMessage());
        }
        System.out.println(" ( after throwing per instance on YapbaopDemo-3 happens)");
        try {
            me3.canThrow(true);// after throwing IS triggered !! this is me3
        } catch (Throwable t) {
            System.out.println("got " + t.getClass().getName() + " / " + t.getMessage());
        }

    }

}
