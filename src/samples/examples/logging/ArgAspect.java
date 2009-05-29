/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.logging;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodSignature;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ArgAspect {

    private int m_level = 0;

    /**
     * @Around pc1(ai, as)
     */
    public Object around1(final JoinPoint joinPoint, int ai, String as) throws Throwable {
        indent();
        m_level++;
        System.out.println(" ==> around1 -- pre " + ai + ", " + as);
        Object result = joinPoint.proceed();
        m_level--;
        indent();
        System.out.println(" ==> around1 -- post " + ai + ", " + as);
        return result;
    }

    /**
     * @Before pc1(ai, as)
     */
    public void before1(final JoinPoint joinPoint, int ai, String as) throws Throwable {
        indent();
        m_level++;
        System.out.println(" ==> before1: " + ai + ", " + as);
    }

    /**
     * @After pc1(ai, as)
     */
    public void after1(final JoinPoint joinPoint, int ai, String as) throws Throwable {
        m_level--;
        indent();
        System.out.println(" ==> after1: " + ai + ", " + as);
    }

    /**
     * @Before pc1(ai, as)
     */
    public void before2(final JoinPoint joinPoint, String as, int ai) throws Throwable {
        indent();
        m_level++;
        System.out.println(" ==> before2: " + as + ", " + ai);
    }

    /**
     * @After pc1(ai, as)
     */
    public void after2(final JoinPoint joinPoint, String as, int ai) throws Throwable {
        m_level--;
        indent();
        System.out.println(" ==> after2: " + as + ", " + ai);
    }

    /**
     * @Around pc2(sarr)
     */
    public Object around3(final JoinPoint joinPoint, String[] sarr) throws Throwable {
        indent();
        m_level++;
        System.out.println("==> around3 -- pre " + sarr);
        Object result = joinPoint.proceed();
        m_level--;
        indent();
        System.out.println("==> around3 -- post " + sarr);
        return result;
    }

    /**
     * @Before pc2(sarr)
     */
    public void before3(final JoinPoint joinPoint, String[] sarr) throws Throwable {
        indent();
        m_level++;
        System.out.println("==> before3: " + sarr);
    }

    /**
     * @After pc2(sarr)
     */
    public void after3(final JoinPoint joinPoint, String[] sarr) throws Throwable {
        m_level--;
        indent();
        System.out.println("==> after3: " + sarr);
    }

    /**
     * @Around pcSet || pcGet
     */
    public Object aroundField(final JoinPoint joinPoint) throws Throwable {
        indent();
        m_level++;
        System.out.println("==> aroundField -- pre");
        Object result = joinPoint.proceed();
        m_level--;
        indent();
        System.out.println("==> aroundField -- post");
        return result;
    }

    /**
     * @Before pcSet || pcGet
     */
    public void beforeField(final JoinPoint joinPoint) throws Throwable {
        indent();
        m_level++;
        System.out.println("==> beforeField");
    }

    /**
     * @After pcSet || pcGet
     */
    public void after3(final JoinPoint joinPoint) throws Throwable {
        m_level--;
        indent();
        System.out.println("==> beforeField");
    }

    /**
     * @Expression execution(* ..ArgLoggingTarget.toLog*(..)) && args(int, s, i)
     */
    Pointcut pc1(int i, String s) {
        return null;
    }

    /**
     * @Expression execution(* ..ArgLoggingTarget.toLog*(..)) && args(int, sarr)
     */
    Pointcut pc2(String[] sarr) {
        return null;
    }

    /**
     * @Expression execution(* ..ArgLoggingTarget.toLog*(..))
     */
    Pointcut pc3() {
        return null;
    }

    /**
     * @Expression set(* ..ArgLoggingTarget.*)
     */
    Pointcut pcSet() {
        return null;
    }

    /**
     * @Expression get(* ..ArgLoggingTarget.*)
     */
    Pointcut pcGet() {
        return null;
    }

    private void indent() {
        for (int i = 0; i < m_level; i++) {
            System.out.print("  ");
        }
    }
}