/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.withincode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.EnclosingStaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.FieldRtti;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.Rtti;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

import test.handler.HandlerTestBeforeException;


/**
 * Withincode(clinit) aspect.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 * 
 * @Aspect("perClass")
 */
public class WithincodeStaticinitializationAspect {
    /**
     * @Expression withincode(staticinitialization(test.withincode.Target))
     */
    Pointcut withincodeTarget;
    
    /**
     * @Expression withincode(staticinitialization(@test.withincode.WithincodeClinit))
     */
    Pointcut withincodeClinit;
    
    /**
     * @Expression withincode(staticinitialization(@test.withincode.WithincodeClinit test.withincode.*))
     */
    Pointcut withincodeClinitPattern;
    
    /**
     * @Expression set(test.withincode.Target$CtorCallTarget test.withincode.Target.s_field)
     */
    Pointcut set;
    
    /**
     * @Expression get(test.withincode.Target$CtorCallTarget test.withincode.Target.s_field)
     */
    Pointcut get;
    
    /**
     * @Expression set || get
     */
    Pointcut getset;
    
    /**
     * @Expression call(test.withincode.Target$CtorCallTarget.new())
     */
    Pointcut ctorCall;
    
    /**
     * @Expression call(void test.withincode.Target.staticMethod())
     */
    Pointcut methodCall;

	/**
	 * @Before ctorCall && withincode(staticinitialization(test.withincode.Target))
	 */
	public void beforeCtorCall() {
	    WithincodeClinitTest.addMessage("beforeCtorCall");
	}

	/**
	 * @Before ctorCall && withincodeClinit
	 */
	public void beforeWithincodeClinitCtorCall() {
	    WithincodeClinitTest.addMessage("beforeWithincodeClinitCtorCall");
	}
	
	/**
	 * @Before ctorCall && withincodeClinitPattern
	 */
	public void beforeWithincodeClinitPatternCtorCall() {
	    WithincodeClinitTest.addMessage("beforeWithincodeClinitPatternCtorCall");
	}
	
	/**
	 * @After ctorCall && withincodeClinitPattern
	 */
	public void afterWithincodeClinitPatternCtorCall() {
	    WithincodeClinitTest.addMessage("afterWithincodeClinitPatternCtorCall");
	}
	
	/**
	 * @After ctorCall && withincodeClinit
	 */
	public void afterWithincodeClinitCtorCall() {
	    WithincodeClinitTest.addMessage("afterWithincodeClinitCtorCall");
	}

	/**
	 * @After ctorCall && withincode(staticinitialization(test.withincode.Target))
	 */
	public void afterCtorCall() {
	    WithincodeClinitTest.addMessage("afterCtorCall");
	}
		
	/**
	 * @AfterReturning ctorCall && withincode(staticinitialization(test.withincode.Target))
	 */
	public void afterReturningCtorCall() {
	    WithincodeClinitTest.addMessage("afterReturningCtorCall");
	}
	
    /**
     * @Before getset && withincode(staticinitialization(test.withincode.Target))
     */
	public void beforeGetSet() {
	    WithincodeClinitTest.addMessage("beforeGetSet");
	}
	
    /**
     * @After getset && withincode(staticinitialization(test.withincode.Target))
     */
	public void afterGetSet() {
	    WithincodeClinitTest.addMessage("afterGetSet");
	}
	
	
    /**
     * @AfterReturning getset && withincode(staticinitialization(test.withincode.Target))
     */
	public void afterReturningGetSet() {
	    WithincodeClinitTest.addMessage("afterReturningGetSet");
	}
	
	/**
	 * @Before methodCall && withincode(staticinitialization(test.withincode.Target))
	 */
	public void beforeMethodCall() {
	    WithincodeClinitTest.addMessage("beforeMethodCall");
	}
	
	/**
	 * @AfterFinally methodCall && withincode(staticinitialization(test.withincode.Target))
	 */
	public void afterFinallyMethodCall() {
	    WithincodeClinitTest.addMessage("afterFinallyMethodCall");
	}
	
	/**
	 * @AfterReturning methodCall && withincode(staticinitialization(test.withincode.Target))
	 */
	public void afterReturningMethodCall() {
	    // should neven occur
	    WithincodeClinitTest.addMessage("afterReturningMethodCall");
	}
	
	/**
	 * @AfterThrowing methodCall && withincode(staticinitialization(test.withincode.Target))
	 */
	public void afterThrowingMethodCall() {
	    WithincodeClinitTest.addMessage("afterThrowingMethodCall");
	}
	
	/**
	 * @AfterThrowing(type="test.handler.HandlerTestBeforeException", pointcut="methodCall && withincode(staticinitialization(test.withincode.Target))")
	 */
	public void afterThrowingTypeMethodCall() {
	    WithincodeClinitTest.addMessage("afterThrowingTypeMethodCall");
	}
	
	/**
	 * @Before handler(test.handler.HandlerTestBeforeException) && withincode(staticinitialization(test.withincode.Target))
	 */
	public void beforeHandler() {
	    WithincodeClinitTest.addMessage("beforeHandler");
	}
	
	// SJP
	/**
	 * @Before ctorCall && withincodeTarget
	 */
	public void beforeCtorCall(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}

	/**
	 * @Around ctorCall && withincodeTarget
	 */
	public Object aroundCtorCall(StaticJoinPoint sjp) throws Throwable {
	    WithincodeClinitTest.addSJP(sjp);
	    return sjp.proceed();
	}
	
	/**
	 * @After ctorCall && withincodeTarget
	 */
	public void afterCtorCall(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
	/**
	 * @AfterReturning ctorCall && withincodeTarget
	 */
	public void afterReturningCtorCall(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
    /**
     * @Before getset && withincodeTarget
     */
	public void beforeGetSet(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
	/**
	 * @Around getset && withincodeTarget
	 */
	public Object aroundGetSet(StaticJoinPoint sjp) throws Throwable {
	    WithincodeClinitTest.addSJP(sjp);
	    return sjp.proceed();
	}
	
    /**
     * @After getset && withincodeTarget
     */
	public void afterGetSet(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
    /**
     * @AfterReturning getset && withincodeTarget
     */
	public void afterReturningGetSet(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
	/**
	 * @Before methodCall && withincodeTarget
	 */
	public void beforeMethodCall(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
	/**
	 * @Around methodCall && withincodeTarget
	 */
	public Object aroundMethodCall(StaticJoinPoint sjp) throws Throwable {
	    WithincodeClinitTest.addSJP(sjp);
	    return sjp.proceed();
	}

	/**
	 * @AfterThrowing(type="test.handler.HandlerTestBeforeException", pointcut="methodCall && withincodeTarget")
	 */
	public void afterThrowingTypeMethodCall(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}

	/**
	 * @AfterThrowing methodCall && withincodeTarget
	 */
	public void afterThrowingMethodCall(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
	/**
	 * @AfterFinally methodCall && withincodeTarget
	 */
	public void afterFinallyMethodCall(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
	/**
	 * @Before handler(test.handler.HandlerTestBeforeException) && withincodeTarget
	 */
	public void beforeHandler(StaticJoinPoint sjp) {
	    WithincodeClinitTest.addSJP(sjp);
	}
	
	/**
	 * @Before handler(test.handler.HandlerTestBeforeException) && withincodeTarget && args(htbe)
	 */
	public void beforeArgsHandler(StaticJoinPoint sjp, HandlerTestBeforeException htbe) {
	    WithincodeClinitTest.addSJP(sjp);
	}

	// JoinPoints
	/**
	 * @Before ctorCall && withincodeTarget
	 */
	public void beforeCtorCall(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}

	/**
	 * @Around ctorCall && withincodeTarget
	 */
	public Object aroundCtorCall(JoinPoint sjp) throws Throwable {
	    WithincodeClinitTest.addJP(sjp);
	    return sjp.proceed();
	}
	
	/**
	 * @After ctorCall && withincodeTarget
	 */
	public void afterCtorCall(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
	/**
	 * @AfterReturning ctorCall && withincodeTarget
	 */
	public void afterReturningCtorCall(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
    /**
     * @Before getset && withincodeTarget
     */
	public void beforeGetSet(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
	/**
	 * @Around getset && withincodeTarget
	 */
	public Object aroundGetSet(JoinPoint sjp) throws Throwable {
	    WithincodeClinitTest.addJP(sjp);
	    return sjp.proceed();
	}
	
    /**
     * @After getset && withincodeTarget
     */
	public void afterGetSet(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
    /**
     * @AfterReturning getset && withincodeTarget
     */
	public void afterReturningGetSet(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
	/**
	 * @Before methodCall && withincodeTarget
	 */
	public void beforeMethodCall(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
	/**
	 * @Around methodCall && withincodeTarget
	 */
	public Object aroundMethodCall(JoinPoint sjp) throws Throwable {
	    WithincodeClinitTest.addJP(sjp);
	    return sjp.proceed();
	}

	/**
	 * @AfterThrowing(type="test.handler.HandlerTestBeforeException", pointcut="methodCall && withincodeTarget")
	 */
	public void afterThrowingTypeMethodCall(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}

	/**
	 * @AfterThrowing methodCall && withincodeTarget
	 */
	public void afterThrowingMethodCall(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
	/**
	 * @AfterFinally methodCall && withincodeTarget
	 */
	public void afterFinallyMethodCall(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
	/**
	 * @Before handler(test.handler.HandlerTestBeforeException) && withincodeTarget
	 */
	public void beforeHandler(JoinPoint sjp) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
	/**
	 * @Before handler(test.handler.HandlerTestBeforeException) && withincodeTarget && args(htbe)
	 */
	public void beforeArgsHandler(JoinPoint sjp, HandlerTestBeforeException htbe) {
	    WithincodeClinitTest.addJP(sjp);
	}
	
}
