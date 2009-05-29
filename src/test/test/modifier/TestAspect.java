/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.modifier;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect perJVM
 */
public class TestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression call(private * test.modifier.*.*Method(..)) && within(test.modifier.*)
     */
    Pointcut call_privateMethod;

    /**
     * @Expression call(protected * test.modifier.*.*Method(..)) && within(test.modifier.*)
     */
    Pointcut call_protectedMethod;

    /**
     * @Expression call(public * test.modifier.*.*Method(..)) && within(test.modifier.*)
     */
    Pointcut call_publicMethod;

    /**
     * @Expression call(static final * test.modifier.*.*Method(..)) && within(test.modifier.*)
     */
    Pointcut call_staticFinalMethod;

    /**
     * @Expression execution(private * test.modifier.*.*Method(..))
     */
    Pointcut execution_privateMethod;

    /**
     * @Expression execution(protected * test.modifier.*.*Method(..))
     */
    Pointcut execution_protectedMethod;

    /**
     * @Expression execution(public * test.modifier.*.*Method(..))
     */
    Pointcut execution_publicMethod;

    /**
     * @Expression get(private * test.modifier.*.*Field) && within(test.modifier.*)
     */
    Pointcut get_privateField;

    /**
     * @Expression get(protected * test.modifier.*.*Field) && within(test.modifier.*)
     */
    Pointcut get_protectedField;

    /**
     * @Expression get(public * test.modifier.*.*Field) && within(test.modifier.*)
     */
    Pointcut get_publicField;

    /**
     * @Expression set(private * test.modifier.*.*Field) && within(test.modifier.*)
     */
    Pointcut set_privateField;

    /**
     * @Expression set(protected * test.modifier.*.*Field) && within(test.modifier.*)
     */
    Pointcut set_protectedField;

    /**
     * @Expression set(public * test.modifier.*.*Field) && within(test.modifier.*)
     */
    Pointcut set_publicField;

    // ============ Advices ============

    /**
     * @Around call_privateMethod || call_publicMethod || call_protectedMethod ||
     * call_staticFinalMethod
     */
    public Object advice_CALL(final JoinPoint joinPoint) throws Throwable {
        ModifierTest.log("call ");
        Object result = joinPoint.proceed();
        ModifierTest.log("call ");
        return result;
    }

    /**
     * @Around execution_privateMethod || execution_protectedMethod || execution_publicMethod
     */
    public Object advice_EXECUTION(final JoinPoint joinPoint) throws Throwable {
        ModifierTest.log("execution ");
        Object result = joinPoint.proceed();
        ModifierTest.log("execution ");
        return result;
    }

    /**
     * @Around set_privateField || set_protectedField || set_publicField
     */
    public Object advice_SET(final JoinPoint joinPoint) throws Throwable {
        ModifierTest.log("set ");
        Object result = joinPoint.proceed();
        ModifierTest.log("set ");
        return result;
    }

    /**
     * @Around get_privateField || get_protectedField || get_publicField
     */
    public Object advice_GET(final JoinPoint joinPoint) throws Throwable {
        ModifierTest.log("get ");
        Object result = joinPoint.proceed();
        ModifierTest.log("get ");
        return result;
    }
}