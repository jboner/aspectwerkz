/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.annotation.Before;
import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import org.codehaus.aspectwerkz.transform.inlining.weaver.SerialVersionUidVisitor;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class CtorExecution extends TestCase implements Serializable {

    static int s_count = 0;

    public CtorExecution m_ref;

    public int m_i;// = 1;

    public CtorExecution(CtorExecution ref) {
        postInit(this);
        //m_ref = ref;
    }

    static void postInit(CtorExecution target) {
        ;
    }

    public CtorExecution() {
        // tricky INVOKESPECIAL indexing
        this(new CtorExecution((CtorExecution)null));
        new CtorExecution((CtorExecution)null);
        postInit(this);
    }

    public CtorExecution(String s) {
        // tricky INVOKESPECIAL indexing
        // and tricky new CtorExecution() call before instance initialization
        // and tricky method call before instance initialization
        super((new CtorExecution()).string(s));
        (new CtorExecution()).string(s);
    }

    public CtorExecution(int i) {
        // tricky field get and set before instance initialization
        super(""+(new CtorExecution()).m_i++);
        (new CtorExecution()).m_i++;
    }

    public String string(String s) {
        return s;
    }

    public void testSome() {
        s_count = 0;
        CtorExecution me = new CtorExecution();
        me = new CtorExecution(me);
        me = new CtorExecution("foo");
        me = new CtorExecution(2);
        assertEquals(116, s_count);// don't know if it is the right number but decompiled seems ok..
    }

    public void testSerialVer() throws Throwable {
        Class x = CtorExecution.class;
        long l = SerialVersionUidVisitor.calculateSerialVersionUID(x);
        // uncomment me and turn off weaver to compute the expected serialVerUID
        //System.out.println(l);

        Field f = x.getDeclaredField("serialVersionUID");
        long uid = ((Long)f.get(null)).longValue();
        //System.out.println(uid);
        assertEquals(3813928159352352835L, uid);
    }

    public static class Aspect {
        @Before("within(test.CtorExecution)")
        void before(StaticJoinPoint sjp) {
            s_count++;
            //System.err.println(sjp.getSignature());
        }

        @Around("execution(test.CtorExecution.new(..))" +
                " || (call(test.CtorExecution.new(..)) && within(test.CtorExecution))")
        Object around(StaticJoinPoint sjp) throws Throwable {
            s_count++;
            //System.out.println(sjp.getSignature());
            return sjp.proceed();
        }

    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(CtorExecution.class);
    }

}
