/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.optimizations;

import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.Rtti;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.codehaus.aspectwerkz.definition.Pointcut;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class OptimizeAspect {

    //------------- advice with no args

    /** @Before execution(* test.optimizations.OptimizeTest$OptimizeNothing.before())
     *          || execution(* test.optimizations.OptimizeTest$OptimizeNothing.beforeAround())*/
    public void beforeNothing() {
        OptimizeTest.log("before");
    }
    /** @Around execution(* test.optimizations.OptimizeTest$OptimizeNothing.around())
     *          || execution(* test.optimizations.OptimizeTest$OptimizeNothing.beforeAround()) */
    public Object aroundNothing() {
        OptimizeTest.log("around");
        return null;// a crapy around aspect!
    }
    /** @Before execution(* test.optimizations.OptimizeTest$OptimizeNothing.before(int)) && args(i) */
    public void beforeNothing(int i) {
        OptimizeTest.log("before"+i);
    }
    /** @Around execution(* test.optimizations.OptimizeTest$OptimizeNothing.around(int)) && args(i) */
    public Object aroundNothing(int i) {
        OptimizeTest.log("around"+i);
        return null;
    }


    //------------- advice with StaticJoinPoint

    /** @Before execution(* test.optimizations.OptimizeTest$OptimizeStaticJoinPoint.before())
     *          || execution(* test.optimizations.OptimizeTest$OptimizeStaticJoinPoint.beforeAround())*/
    public void beforeStaticJoinPoint(StaticJoinPoint sjp) {
        OptimizeTest.log("beforeSJP-" + sjp.getSignature().getName());
    }
    /** @Around execution(* test.optimizations.OptimizeTest$OptimizeStaticJoinPoint.around())
     *          || execution(* test.optimizations.OptimizeTest$OptimizeStaticJoinPoint.beforeAround()) */
    public Object aroundStaticJoinPoint(StaticJoinPoint sjp) throws Throwable {
        OptimizeTest.log("aroundSJP-" + sjp.getSignature().getName());
        return sjp.proceed();
    }
    /** @Before execution(* test.optimizations.OptimizeTest$OptimizeStaticJoinPoint.before(int)) && args(i) */
    public void beforeStaticJoinPoint(StaticJoinPoint sjp, int i) {
        OptimizeTest.log("beforeSJP"+i);
    }
    /** @Around execution(* test.optimizations.OptimizeTest$OptimizeStaticJoinPoint.around(int)) && args(i) */
    public Object aroundStaticJoinPoint(int i, StaticJoinPoint sjp) throws Throwable {
        OptimizeTest.log("aroundSJP"+i);
        return sjp.proceed();
    }

    //------------- advice with JoinPoint, will make use of a runtime check for target

    /** @Expression withincode(* test.optimizations.OptimizeTest.testJoinPoint(..)) && target(test.optimizations.OptimizeTest$OptimizeJoinPoint) */
    Pointcut pc_in;

    /** @Before "(call(* test.optimizations.OptimizeTest$IOptimize.before())
     *          || call(* test.optimizations.OptimizeTest$IOptimize.beforeAround())
     *          ) && pc_in"
     */
    public void beforeJoinPoint(JoinPoint jp) {
        OptimizeTest.log("beforeJP-" + jp.getSignature().getName() + "-" + jp.getCallee().toString() + "-" + jp.getCaller().toString());
    }
    /** @Around "(call(* test.optimizations.OptimizeTest$IOptimize.around())
     *          || call(* test.optimizations.OptimizeTest$IOptimize.beforeAround())
     *          ) && pc_in"
     */
    public Object aroundJoinPoint(JoinPoint jp) throws Throwable {
        OptimizeTest.log("aroundJP-" + jp.getSignature().getName() + "-" + jp.getCallee().toString() + "-" + jp.getCaller().toString());
        return jp.proceed();
    }
    /** @Before call(* test.optimizations.OptimizeTest$IOptimize.before(int)) && args(i) && pc_in */
    public void beforeJoinPoint(JoinPoint jp, int i) {
        OptimizeTest.log("beforeJP"+i);
    }
    /** @Around call(* test.optimizations.OptimizeTest$IOptimize.around(int)) && args(i) && pc_in */
    public Object aroundJoinPoint(int i, JoinPoint jp) throws Throwable {
        OptimizeTest.log("aroundJP"+i);
        return jp.proceed();
    }

    //------------- advice with Rtti

    /**
     * @Before execution(* test.optimizations.OptimizeTest$OptimizeRtti.before())
     * || execution(* test.optimizations.OptimizeTest$OptimizeRtti.beforeAround())
     */
    public void beforeRtti(JoinPoint jp) {
        OptimizeTest.log("beforeRTTI-" + jp.getRtti().getName() + jp.getRtti().getThis() + jp.getRtti().getTarget());
    }

    /**
     * @Around execution(* test.optimizations.OptimizeTest$OptimizeRtti.around())
     * || execution(* test.optimizations.OptimizeTest$OptimizeRtti.beforeAround())
     */
    public Object aroundRtti(JoinPoint jp) throws Throwable {
        OptimizeTest.log("aroundRTTI-" + jp.getRtti().getName() + jp.getRtti().getThis() + jp.getRtti().getTarget());
        return jp.proceed();
    }

    /**
     * @Before execution(* test.optimizations.OptimizeTest$OptimizeRtti.before(int))
     */
    public void beforeRttiInt(JoinPoint jp) {
        OptimizeTest.log("beforeRTTI-" + jp.getRtti().getName() + jp.getRtti().getThis() + jp.getRtti().getTarget());
        Integer param = (Integer) ((MethodRtti) jp.getRtti()).getParameterValues()[0];
        //TODO ...
    }

    /**
     * @Around execution(* test.optimizations.OptimizeTest$OptimizeRtti.around(int))
     */
    public Object aroundRtti(StaticJoinPoint sjp, JoinPoint jp /* note: silly but possible...*/) throws Throwable {
        OptimizeTest.log("aroundRTTI-" /*TODO*/);
        return sjp.proceed();
    }
}
