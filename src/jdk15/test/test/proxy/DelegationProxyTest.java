/*******************************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *   Alexandre Vasseur         initial implementation
 *******************************************************************************/
package test.proxy;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.proxy.Proxy;
import org.codehaus.aspectwerkz.intercept.Advisable;
import org.codehaus.aspectwerkz.intercept.AroundAdvice;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodSignature;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class DelegationProxyTest extends TestCase {

    public void testProxy() {
        Object p = Proxy.newInstance(
                new Class[]{IFirst.class, ISecond.class},
                new Object[]{new First(), new Second()},
                true,
                true
        );

        assertTrue(p instanceof IFirst);
        assertTrue(p instanceof ISecond);
        assertTrue(p instanceof Advisable);

        final StringBuffer sb = new StringBuffer();
        ((Advisable)p).aw_addAdvice(
                "execution(* *.*(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        sb.append(((MethodSignature)jp.getSignature()).getName()).append(" ");
                        return jp.proceed();
                    }
                }
        );

        ((IFirst)p).inBoth();//advisabled, comes from IFirst
        assertEquals(1, ((IFirst)p).inBoth());
        ((ISecond)p).inBoth();//advisabled, comes from IFirst in fact !
        assertEquals(1, ((ISecond)p).inBoth());
        ((IFirst)p).doSome();//advisabled
        ((ISecond)p).doOther();//advisabled

        assertEquals("inBoth inBoth inBoth inBoth doSome doOther ", sb.toString());

        ((Advisable)p).aw_addAdvice(
                "execution(* *.*(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        return jp.proceed();
                    }
                }
        );

        assertEquals("inBoth inBoth inBoth inBoth doSome doOther ", sb.toString());// else aw_addAdvice advised!

        // create another proxy, and don't use the cache, so don't have it Advisable
        Class other = Proxy.getProxyClassFor(new Class[]{IFirst.class, ISecond.class}, false, false);
        assertEquals(2, other.getInterfaces().length);
    }


    static interface IFirst {
        void doSome();
        int inBoth();
    }
    static interface ISecond {
        void doOther();
        int inBoth();
    }
    static class First implements IFirst {
        public void doSome() {}
        public int inBoth() {return 1;}
    }
    static class Second implements ISecond {
        public void doOther() {}
        public int inBoth() {return -1;}
    }
}
