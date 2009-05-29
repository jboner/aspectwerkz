/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.thistarget;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class ThisTargetAspect {

    //------------------------- Method execution

    /**
     * @Expression execution(* test.thistarget.*.target())
     */
    Pointcut exe_target;

    /**
     * @Expression execution(* test.thistarget.*.targetAbstract())
     */
    Pointcut exe_targetAbstract;

    // interface

    /**
     * @Before exe_target && target(t)
     */
    public void beforeITarget(ITarget t) {
        validate(t, ITarget.class);
        TargetTest.log("before_ITarget");
    }

    /**
     * @Around exe_target && target(t)
     */
    public Object aroundITarget(JoinPoint jp, ITarget t) throws Throwable {
        validate(t, ITarget.class);
        TargetTest.log("pre_ITarget");
        Object o = jp.proceed();
        TargetTest.log("post_ITarget");
        return o;
    }

    /**
     * @After exe_target && target(t)
     */
    public void afterITarget(ITarget t) {
        validate(t, ITarget.class);
        TargetTest.log("after_ITarget");
    }

    // interface implementation

    /**
     * @Before exe_target && target(t) && this(callee)
     */
    public void beforeTargetIWithThis(TargetI t, Object callee) {
        validate(t, TargetI.class);
        validate(callee, TargetI.class);
        TargetTest.log("before_TargetI");
    }

    /**
     * @Around exe_target && target(t)
     */
    public Object aroundTargetI(JoinPoint jp, TargetI t) throws Throwable {
        validate(t, TargetI.class);
        TargetTest.log("pre_TargetI");
        Object o = jp.proceed();
        TargetTest.log("post_TargetI");
        return o;
    }

    /**
     * @After exe_target && target(t)
     */
    public void afterTargetI(TargetI t) {
        validate(t, TargetI.class);
        TargetTest.log("after_TargetI");
    }

    // super class

    /**
     * @Before exe_target && target(t)
     */
    public void beforeSuperTarget(SuperTarget t) {
        validate(t, SuperTarget.class);
        TargetTest.log("before_SuperTarget");
    }

    /**
     * @Around exe_target && target(t)
     */
    public Object aroundSuperTarget(JoinPoint jp, SuperTarget t) throws Throwable {
        validate(t, SuperTarget.class);
        TargetTest.log("pre_SuperTarget");
        Object o = jp.proceed();
        TargetTest.log("post_SuperTarget");
        return o;
    }

    /**
     * @After exe_target && target(t)
     */
    public void afterSuperTarget(SuperTarget t) {
        validate(t, SuperTarget.class);
        TargetTest.log("after_SuperTarget");
    }

    // super class abstract method

    /**
     * @Before exe_targetAbstract && target(t)
     */
    public void beforeSuperTargetA(SuperTarget t) {
        validate(t, SuperTarget.class);
        TargetTest.log("before_SuperTargetA");
    }

    /**
     * @Around exe_targetAbstract && target(t)
     */
    public Object aroundSuperTargetA(JoinPoint jp, SuperTarget t) throws Throwable {
        validate(t, SuperTarget.class);
        TargetTest.log("pre_SuperTargetA");
        Object o = jp.proceed();
        TargetTest.log("post_SuperTargetA");
        return o;
    }

    /**
     * @After exe_targetAbstract && target(t)
     */
    public void afterSuperTargetA(SuperTarget t) {
        validate(t, SuperTarget.class);
        TargetTest.log("after_SuperTargetA");
    }

    //------------------------- Ctor call

    /**
     * @Expression this(caller) && call(test.thistarget.*.new()) && withincode(* test.*.*.testConstructorCallTargetThis(..))
     */
    Pointcut cctor_this(TargetTest caller) {
        return null;
    }


    // interface

    /**
     * @Before cctor_this(caller) && target(t)
     */
    public void beforeITarget(ITarget t, Object caller) {
        validate(t, null);
        validate(caller, TargetTest.class);
        TargetTest.log("before_ITarget");
    }

    /**
     * @Around cctor_this(caller) && target(t)
     */
    public Object aroundITarget(JoinPoint jp, ITarget t, Object caller) throws Throwable {
        validate(t, null);
        validate(caller, TargetTest.class);
        TargetTest.log("pre_ITarget");
        Object o = jp.proceed();
        validate(o, ITarget.class);
        validate(t, null);// in an around advice, target is a local variable so even if jp has set callee, the local
        // instance is not.
        TargetTest.log("post_ITarget");
        return o;
    }

    /**
     * @After cctor_this(caller) && target(t)
     */
    public void afterITarget(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("after_ITarget");
    }

    // interface implementation

    /**
     * @Before cctor_this(caller) && target(t)
     */
    public void beforeTargetI(TargetI t, Object caller) {
        validate(t, null);
        validate(caller, TargetTest.class);
        TargetTest.log("before_TargetI");
    }

    /**
     * @Around cctor_this(caller) && target(t)
     */
    public Object aroundTargetI(JoinPoint jp, TargetI t, Object caller) throws Throwable {
        validate(t, null);
        validate(caller, TargetTest.class);
        TargetTest.log("pre_TargetI");
        Object o = jp.proceed();
        validate(o, TargetI.class);
        validate(t, null);// still null
        TargetTest.log("post_TargetI");
        return o;
    }

    /**
     * @After cctor_this(caller) && target(t)
     */
    public void afterTargetI(TargetI t, Object caller) {
        validate(t, TargetI.class);
        validate(caller, TargetTest.class);
        TargetTest.log("after_TargetI");
    }

    // super class

    /**
     * @Before cctor_this(caller) && target(t)
     */
    public void beforeSuperTarget(SuperTarget t, Object caller) {
        validate(t, null);
        validate(caller, TargetTest.class);
        TargetTest.log("before_SuperTarget");
    }

    /**
     * @Around cctor_this(caller) && target(t)
     */
    public Object aroundSuperTarget(JoinPoint jp, SuperTarget t, Object caller) throws Throwable {
        validate(t, null);
        validate(caller, TargetTest.class);
        TargetTest.log("pre_SuperTarget");
        Object o = jp.proceed();
        validate(o, SuperTarget.class);
        validate(t, null);//still null - local variable
        TargetTest.log("post_SuperTarget");
        return o;
    }

    /**
     * @After cctor_this(caller) && target(t)
     */
    public void afterSuperTarget(SuperTarget t, Object caller) {
        validate(t, SuperTarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("after_SuperTarget");
    }





    //------------------------- Method call

    /**
     * @Expression this(caller) && call(* test.thistarget.*.call()) && withincode(* test.*.*.testMethodCallTargetThis(..))
     */
    Pointcut call_this(TargetTest caller) {
        return null;
    }

    /**
     * @Expression this(caller) && call(* test.thistarget.*.callAbstract()) && withincode(* test.*.*.testMethodCallTargetThis(..))
     */
    Pointcut callAbstract_this(TargetTest caller) {
        return null;
    }

    // interface

    /**
     * @Before call_this(caller) && target(t)
     */
    public void beforeICall(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("before_ITarget");
    }

    /**
     * @Around call_this(caller) && target(t)
     */
    public Object aroundICall(JoinPoint jp, ITarget t, Object caller) throws Throwable {
        validate(t, ITarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("pre_ITarget");
        Object o = jp.proceed();
        validate(t, ITarget.class);
        // instance is not.
        TargetTest.log("post_ITarget");
        return o;
    }

    /**
     * @After call_this(caller) && target(t)
     */
    public void afterICall(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("after_ITarget");
    }

    // interface implementation

    /**
     * @Before call_this(caller) && target(t)
     */
    public void beforeCallI(TargetI t, Object caller) {
        validate(t, TargetI.class);
        validate(caller, TargetTest.class);
        TargetTest.log("before_TargetI");
    }

    /**
     * @Around call_this(caller) && target(t)
     */
    public Object aroundCallI(JoinPoint jp, TargetI t, Object caller) throws Throwable {
        validate(t, TargetI.class);
        validate(caller, TargetTest.class);
        TargetTest.log("pre_TargetI");
        Object o = jp.proceed();
        validate(t, TargetI.class);
        TargetTest.log("post_TargetI");
        return o;
    }

    /**
     * @After call_this(caller) && target(t)
     */
    public void afterCallI(TargetI t, Object caller) {
        validate(t, TargetI.class);
        validate(caller, TargetTest.class);
        TargetTest.log("after_TargetI");
    }

    // super class

    /**
     * @Before call_this(caller) && target(t)
     */
    public void beforeSuperCall(SuperTarget t, Object caller) {
        validate(t, SuperTarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("before_SuperTarget");
    }

    /**
     * @Around call_this(caller) && target(t)
     */
    public Object aroundSuperCall(JoinPoint jp, SuperTarget t, Object caller) throws Throwable {
        validate(t, SuperTarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("pre_SuperTarget");
        Object o = jp.proceed();
        validate(t, SuperTarget.class);
        TargetTest.log("post_SuperTarget");
        return o;
    }

    /**
     * @After call_this(caller) && target(t)
     */
    public void afterSuperCall(SuperTarget t, Object caller) {
        validate(t, SuperTarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("after_SuperTarget");
    }

    // super class - abstract method

    /**
     * @Before callAbstract_this(caller) && target(t)
     */
    public void beforeSuperCallA(SuperTarget t, Object caller) {
        validate(t, SuperTarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("before_SuperTargetA");
    }

    /**
     * @Around callAbstract_this(caller) && target(t)
     */
    public Object aroundSuperCallA(JoinPoint jp, SuperTarget t, Object caller) throws Throwable {
        validate(t, SuperTarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("pre_SuperTargetA");
        Object o = jp.proceed();
        validate(t, SuperTarget.class);
        TargetTest.log("post_SuperTargetA");
        return o;
    }

    /**
     * @After callAbstract_this(caller) && target(t)
     */
    public void afterSuperCallA(SuperTarget t, Object caller) {
        validate(t, SuperTarget.class);
        validate(caller, TargetTest.class);
        TargetTest.log("after_SuperTargetA");
    }




    //------------------------- Ctor exe

    /**
     * @Expression this(self) && execution(test.thistarget.*.new())
     */
    Pointcut ector_this(Object self) {
        return null;
    }


    // interface

    /**
     * @Before ector_this(caller) && target(t)
     */
    public void ector_beforeITarget(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, ITarget.class);
        TargetTest.logCtorExe("before_ITarget");
    }

    /**
     * @Around ector_this(caller) && target(t)
     */
    public Object ector_aroundITarget(JoinPoint jp, ITarget t, Object caller) throws Throwable {
        validate(t, ITarget.class);
        validate(caller, ITarget.class);
        TargetTest.logCtorExe("pre_ITarget");
        Object o = jp.proceed();
        //validate(o, ITarget.class);
        validate(t, ITarget.class);
        // instance is not.
        TargetTest.logCtorExe("post_ITarget");
        return o;
    }

    /**
     * @After ector_this(caller) && target(t)
     */
    public void ector_afterITarget(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, ITarget.class);
        TargetTest.logCtorExe("after_ITarget");
    }

    // interface implementation

    /**
     * @Before ector_this(caller) && target(t)
     */
    public void ector_beforeTargetI(TargetI t, Object caller) {
        validate(t, TargetI.class);
        validate(caller, TargetI.class);
        TargetTest.logCtorExe("before_TargetI");
    }

    /**
     * @Around ector_this(caller) && target(t)
     */
    public Object ector_aroundTargetI(JoinPoint jp, TargetI t, Object caller) throws Throwable {
        validate(t, TargetI.class);
        validate(caller, TargetI.class);
        TargetTest.logCtorExe("pre_TargetI");
        Object o = jp.proceed();
        //validate(o, TargetI.class);
        validate(t, TargetI.class);
        TargetTest.logCtorExe("post_TargetI");
        return o;
    }

    /**
     * @After ector_this(caller) && target(t)
     */
    public void ector_afterTargetI(TargetI t, Object caller) {
        validate(t, TargetI.class);
        validate(caller, TargetI.class);
        TargetTest.logCtorExe("after_TargetI");
    }

    // super class

    /**
     * @Before ector_this(caller) && target(t)
     */
    public void ector_beforeSuperTarget(SuperTarget t, Object caller) {
        validate(t, SuperTarget.class);
        validate(caller, SuperTarget.class);
        TargetTest.logCtorExe("before_SuperTarget");
    }

    /**
     * @Around ector_this(caller) && target(t)
     */
    public Object ector_aroundSuperTarget(JoinPoint jp, SuperTarget t, Object caller) throws Throwable {
        validate(t, SuperTarget.class);
        validate(caller, SuperTarget.class);
        TargetTest.logCtorExe("pre_SuperTarget");
        Object o = jp.proceed();
        //validate(o, SuperTarget.class);
        validate(t, SuperTarget.class);
        TargetTest.logCtorExe("post_SuperTarget");
        return o;
    }

    /**
     * @After ector_this(caller) && target(t)
     */
    public void ector_afterSuperTarget(SuperTarget t, Object caller) {
        validate(t, SuperTarget.class);
        validate(caller, SuperTarget.class);
        TargetTest.logCtorExe("after_SuperTarget");
    }


    //------------------------- Method call while "this" is subclassed

    /**
     * @Expression this(caller) && call(* test.thistarget.*.call()) && withincode(* test.*.*.callFrom(..))
     */
    Pointcut call_thisSubinterface(IThis caller) {
        return null;
    }

    // interface, while this implements the interface we match

    /**
     * @Before call_thisSubinterface(caller) && target(t)
     */
    public void beforeICallSubinterface(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, IThis.class);
        TargetTest.log("before_ITarget");
    }

    /**
     * @Around call_thisSubinterface(caller) && target(t)
     */
    public Object aroundICallSubinterface(JoinPoint jp, ITarget t, Object caller) throws Throwable {
        validate(t, ITarget.class);
        validate(caller, IThis.class);
        TargetTest.log("pre_ITarget");
        Object o = jp.proceed();
        TargetTest.log("post_ITarget");
        return o;
    }

    /**
     * @After call_thisSubinterface(caller) && target(t)
     */
    public void afterICallSubinterface(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, IThis.class);
        TargetTest.log("after_ITarget");
    }

    /**
     * @Expression this(caller) && call(* test.thistarget.*.call()) && withincode(* test.*.*.callFrom(..))
     */
    Pointcut call_thisSubclass(SuperThis caller) {
        return null;
    }

    // interface, while this subclass the class we match

    /**
     * @Before call_thisSubclass(caller) && target(t)
     */
    public void beforeICallSubclass(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, SuperThis.class);
        TargetTest.log("before_ITarget");
    }

    /**
     * @Around call_thisSubclass(caller) && target(t)
     */
    public Object aroundICallSubclass(JoinPoint jp, ITarget t, Object caller) throws Throwable {
        validate(t, ITarget.class);
        validate(caller, SuperThis.class);
        TargetTest.log("pre_ITarget");
        Object o = jp.proceed();
        TargetTest.log("post_ITarget");
        return o;
    }

    /**
     * @After call_thisSubclass(caller) && target(t)
     */
    public void afterICallSubclass(ITarget t, Object caller) {
        validate(t, ITarget.class);
        validate(caller, SuperThis.class);
        TargetTest.log("after_ITarget");
    }


    /**
     * We need to validate the bounded this/target since if the indexing is broken, we may have
     * the joinpoint instance instead etc, and if not used, the VM will not complain.
     *
     * @param t
     * @param checkCast
     */
    static void validate(Object t, Class checkCast) {
        if (checkCast == null && t != null) {
            TestCase.fail("should ne null: " + t.getClass().getName());
        } else if (checkCast != null) {
            if (!checkCast.isAssignableFrom(t.getClass())) {
                TestCase.fail("t " + t.getClass().getName() + " is not instance of " + checkCast.getName());
            }
        }
    }

}
