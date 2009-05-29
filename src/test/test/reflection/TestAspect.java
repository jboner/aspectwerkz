/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.reflection;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Aspect
 */
public class TestAspect {
    /**
     * @Expression execution(* test.reflection.*2.*(..))
     */
    Pointcut test1_exclude;

    /**
     * @Expression execution(* test.reflection.*.incr(..))
     */
    Pointcut test1;

    /**
     * @Expression execution(* test.reflection.*.incrStatic(..))
     */
    Pointcut test1Static;

    /**
     * @Expression execution(* test.reflection.Super2.incr(..))
     */
    Pointcut test2;

    /**
     * @Expression execution(* test.reflection.Super2.incrStatic(..))
     */
    Pointcut test2Static;

    /**
     * @Expression execution(* test.reflection.*.do*(..))
     */
    Pointcut test3;

    /**
     * @Around test1 && !test1_exclude
     */
    public Object execute1(final JoinPoint jp) throws Throwable {
        Integer result = (Integer) jp.proceed();
        return new Integer(-1 * result.intValue());
    }

    /**
     * @Around test1Static && !test1_exclude
     */
    public Object execute2(final JoinPoint jp) throws Throwable {
        Integer result = (Integer) jp.proceed();
        return new Integer(-1 * result.intValue());
    }

    /**
     * @Around test2
     */
    public Object execute3(final JoinPoint jp) throws Throwable {
        Integer result = (Integer) jp.proceed();
        return new Integer(-1 * result.intValue());
    }

    /**
     * @Around test2Static
     */
    public Object execute4(final JoinPoint jp) throws Throwable {
        Integer result = (Integer) jp.proceed();
        return new Integer(-1 * result.intValue());
    }

    /**
     * @Around test3
     */
    public Object execute5(final JoinPoint jp) throws Throwable {
        Integer result = (Integer) jp.proceed();
        return new Integer(-1 * result.intValue());
    }
}