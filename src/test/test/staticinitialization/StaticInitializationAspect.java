/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.staticinitialization;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;


/**
 * Aspect on staticinitialization pointcut.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 * 
 * @Aspect("perClass")
 */
public class StaticInitializationAspect {
	/**
	 * @Expression staticinitialization(test.staticinitialization.ClinitTarget) 
	 */
	Pointcut staticInitialization;
	
	/**
	 * @Expression staticinitialization(@test.staticinitialization.StaticInitializationService)
	 */
	Pointcut staticServiceInitialization;
	
	/**
	 * @Expression staticinitialization(@test.staticinitialization.StaticInitializationService test.staticinitialization.*)
	 */
	Pointcut staticPatternInitialization;
	
	/**
	 * @Before staticInitialization
	 */
	public void beforeStaticInitializer() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[0]);	                                                                                   
	}
	
	/**
	 * @Before staticServiceInitialization
	 */
	public void beforeStaticServiceInitialization() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[0]);	                                                                                   
	}
	
	/**
	 * @Before staticPatternInitialization
	 */
	public void beforeStaticPatternInitialization() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[0]);	                                                                                   
	}
	
	/**
	 * @Before staticInitialization
	 */
	public void beforeStaticInitialization(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @Before staticServiceInitialization
	 */
	public void beforeStaticServiceInitialization(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @Before staticPatternInitialization
	 */
	public void beforeStaticPatternInitialization(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @Before staticInitialization
	 */
	public void beforeStaticInitialization(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
	
	/**
	 * @Before staticServiceInitialization
	 */
	public void beforeStaticServiceInitialization(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
	
	/**
	 * @Before staticPatternInitialization
	 */
	public void beforeStaticPatternInitialization(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
	
	/**
	 * @Around staticInitialization
	 */
	public Object aroundStaticInitialization(StaticJoinPoint sjp) throws Throwable {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[1]);
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
		
		return sjp.proceed();
	}
	
	/**
	 * @Around staticServiceInitialization
	 */
	public Object aroundStaticServiceInitialization(StaticJoinPoint sjp) throws Throwable {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[1]);
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
		
		return sjp.proceed();
	}
	
	/**
	 * @Around staticPatternInitialization
	 */
	public Object aroundStaticPatternInitialization(StaticJoinPoint sjp) throws Throwable {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[1]);
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
		
		return sjp.proceed();
	}
	
	/**
	 * @Around staticInitialization
	 */
	public Object aroundStaticInitialization(JoinPoint jp) throws Throwable {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[2]);
		StaticInitializationTest.s_joinPoints.add(jp);
		
		return jp.proceed();
	}
	
	/**
	 * @Around staticServiceInitialization
	 */
	public Object aroundStaticServiceInitialization(JoinPoint jp) throws Throwable {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[2]);
		StaticInitializationTest.s_joinPoints.add(jp);
		
		return jp.proceed();
	}
	
	/**
	 * @Around staticPatternInitialization
	 */
	public Object aroundStaticPatternInitialization(JoinPoint jp) throws Throwable {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.BEFORE_EXPECTED_MESSAGES[2]);
		StaticInitializationTest.s_joinPoints.add(jp);
		
		return jp.proceed();
	}
	
	/**
	 * @AfterReturning staticInitialization
	 */
	public void afterReturningStaticInitializer() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.AFTER_EXPECTED_MESSAGES[0]);
	}

	/**
	 * @AfterReturning staticServiceInitialization
	 */
	public void afterReturningServiceInitializer() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.AFTER_EXPECTED_MESSAGES[0]);
	}
	
	/**
	 * @AfterReturning staticPatternInitialization
	 */
	public void afterReturningPatternInitializer() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.AFTER_EXPECTED_MESSAGES[0]);
	}
	
	/**
	 * @AfterReturning staticInitialization
	 */
	public void afterReturningStaticInitializer(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @AfterReturning staticServiceInitialization
	 */
	public void afterReturningServiceInitializer(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @AfterReturning staticPatternInitialization
	 */
	public void afterReturningPatternInitializer(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @AfterReturning staticInitialization
	 */
	public void afterReturningStaticInitializer(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
	
	/**
	 * @AfterReturning staticServiceInitialization
	 */
	public void afterReturningServiceInitializer(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
	
	/**
	 * @AfterReturning staticPatternInitialization
	 */
	public void afterReturningPatternInitializer(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
	
	/**
	 * @After staticInitialization
	 */
	public void afterStaticInitializer() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.AFTER_EXPECTED_MESSAGES[1]);
	}
	
	/**
	 * @After staticServiceInitialization
	 */
	public void afterServiceInitializer() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.AFTER_EXPECTED_MESSAGES[1]);
	}
	
	/**
	 * @After staticPatternInitialization
	 */
	public void afterPatternInitializer() {
		StaticInitializationTest.s_messages.add(StaticInitializationTest.AFTER_EXPECTED_MESSAGES[1]);
	}
	
	/**
	 * @After staticInitialization
	 */
	public void afterStaticInitializer(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @After staticServiceInitialization
	 */
	public void afterServiceInitializer(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @After staticPatternInitialization
	 */
	public void afterPatternInitializer(StaticJoinPoint sjp) {
		StaticInitializationTest.s_staticJoinPoints.add(sjp);
	}
	
	/**
	 * @After staticInitialization
	 */
	public void afterStaticInitializer(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
	
	/**
	 * @After staticServiceInitialization
	 */
	public void afterServiceInitializer(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
	
	/**
	 * @After staticPatternInitialization
	 */
	public void afterPatternInitializer(JoinPoint jp) {
		StaticInitializationTest.s_joinPoints.add(jp);
	}
}
