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

import org.codehaus.aspectwerkz.proxy.Proxy;
import org.codehaus.aspectwerkz.intercept.Advisable;
import org.codehaus.aspectwerkz.intercept.AroundAdvice;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodSignature;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class SubclassingProxyTest extends TestCase {

    public void testProxy() throws Exception {
        Object p = Proxy.newInstance(Proxied.class, new Class[]{String.class}, new Object[]{"name"}, true, true);
        assertTrue(p instanceof Proxied);
        assertTrue(p instanceof Advisable);

        assertEquals("name", ((Proxied)p).m_name);
        ((Proxied)p).finalMethod();// not advisable / not proxied

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

        ((Proxied)p).some(1L, 1);//advisabled
        ((Proxied)p).somePrivate();//non advisabled
        ((Proxied)p).finalMethod();//non advisabled

        assertEquals("some ", sb.toString());

        ((Advisable)p).aw_addAdvice(
                "execution(* *.*(..))",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        return jp.proceed();
                    }
                }
        );

        assertEquals("some ", sb.toString());// else aw_addAdvice advised!

        // create another proxy, and don't use the cache, so don't have it Advisable
        Class other = Proxy.getProxyClassFor(Proxied.class, false, false);
        assertEquals(0, other.getInterfaces().length);
    }

    static class Proxied {

        private String m_name;

        public Proxied(String name) {
            m_name = name;
        }

        final void finalMethod() {
            ;
        }
        long some(long j, int i) {
            return j + i;
        }
        private void somePrivate() {
            ;
        }
    }
}
