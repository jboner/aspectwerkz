/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.enclosingsjp;


import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.ConstructorSignature;
import org.codehaus.aspectwerkz.joinpoint.EnclosingStaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.Signature;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;


/**
 * @Aspect("perClass")
 */
public class EnclosingStaticJoinPointAspect {

	/**
	 * @Before call(test.enclosingsjp.PointcutTarget.new()) && within(test.enclosingsjp..*)
	 */
	public void ctorCtorCall(StaticJoinPoint sjp) {
		//System.out.println(".ctorCtorCall");
		EnclosingStaticJoinPoint esjp = sjp.getEnclosingStaticJoinPoint();
		EnclosingSJPTest.registerEnclosingSJP(esjp);
	}
	
	/**
	 * @Before call(* test.enclosingsjp.PointcutTarget.method1()) && within(test.enclosingsjp..*)
	 */
	public void ctorMethodCall(StaticJoinPoint sjp) {
		//System.out.println(".ctorMethodCall");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before call(test.enclosingsjp.PointcutTarget.new()) && within(test.enclosingsjp.EnclosingTarget)
	 * @param sjp
	 */
	public void ctorCtorWithinCall(StaticJoinPoint sjp) {
		//System.out.println(".ctorCtorWithinCall");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before call(test.enclosingsjp.PointcutTarget.new()) && withincode(test.enclosingsjp.EnclosingTarget.new())
	 * @param sjp
	 */
	public void ctorCtorWithincodeCall(StaticJoinPoint sjp) {
		//System.out.println(".ctorCtorWithincodeCall");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before call(test.enclosingsjp.PointcutTarget.new()) && within(test.enclosingsjp..*) && cflow(execution(test.enclosingsjp.EnclosingTarget.new()))
	 * @param sjp
	 */
	public void ctorCtorCflow(StaticJoinPoint sjp) {
		//System.out.println(".ctorCtorCflow");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before call(test.enclosingsjp.PointcutTarget.new()) && within(test.enclosingsjp..*) && cflowbelow(execution(test.enclosingsjp.EnclosingTarget.new()))
	 * @param sjp
	 */
	public void ctorCtorCflowbelowExecution(StaticJoinPoint sjp) {
		//System.out.println(".ctorCtorCflowbelowExecution");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before within(test.enclosingsjp..*) && call(test.enclosingsjp.PointcutTarget.new()) && cflowbelow(call(test.enclosingsjp.EnclosingTarget.new()) && within(test.enclosingsjp..*))
	 * @param sjp
	 */
	public void ctorCtorCflowbelowCall(StaticJoinPoint sjp) {
		//System.out.println(".ctorCtorCflowbelowCall");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before within(test.enclosingsjp..*) && call(test.enclosingsjp.PointcutTarget.new()) && cflowbelow(execution(* test.enclosingsjp.EnclosingSJPTest.testConstructorEnclosing()))
	 * @param sjp
	 */
	public void testCtorCflowbelow(StaticJoinPoint sjp) {
		//System.out.println(".testCtorCflowbelow");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before handler(java.lang.IllegalAccessException) AND within(test.enclosingsjp.*)
	 */
	public void simpleHandler(StaticJoinPoint sjp) {
		//System.out.println(".simpleHandler");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before handler(java.lang.IllegalAccessException) AND within(test.enclosingsjp.*) AND args(iae)
	 */
	public void parameterHandler(StaticJoinPoint sjp, IllegalAccessException iae) {
		//System.out.println(".parameterHandler");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Expression set(java.lang.Object test.enclosingsjp.EnclosingTarget.m_field) && within(test.enclosingsjp..*)
	 */
	private Pointcut enclosingSet;
	
	/**
	 * @Before enclosingSet 
	 */
	public void enclosingConstructorSet(StaticJoinPoint sjp) {
		//System.out.println(".enclosingConstructorSet");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}
	
	/**
	 * @Before enclosingSet && cflow(call(test.enclosingsjp.EnclosingTarget.new(java.lang.Object)) && within(test.enclosingsjp..*))
	 * @param sjp
	 */
	public void cflowCallSet(StaticJoinPoint sjp) {
		//System.out.println(".cflowCallSet");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}

	/**
	 * @Before enclosingSet && cflow(execution(test.enclosingsjp.EnclosingTarget.new(java.lang.Object)))
	 * @param sjp
	 */
	public void cflowExecuteSet(StaticJoinPoint sjp) {
		//System.out.println(".cflowExecuteSet");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}

	/**
	 * @Expression("(set(java.lang.Object test.enclosingsjp.PointcutTarget.m_field) || get(java.lang.Object test.enclosingsjp.PointcutTarget.m_field)) && within(test.enclosingsjp..*)")
	 */
	private Pointcut getset;
	
	/**
	 * @Before getset
	 */
	public void  methodGetSet(StaticJoinPoint sjp) {
		//System.out.println(".methodGetSet");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}

	/**
	 * @Before getset && cflow(execution(test.enclosingsjp.EnclosingTarget.new(*)))
	 */
	public void methodCflowGetSet(StaticJoinPoint sjp) {
		//System.out.println(".methodCflowGetSet");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}

	/**
	 * @Before getset && cflowbelow(call(test.enclosingsjp.EnclosingTarget.new(*)) && within(test.enclosingsjp..*))
	 */
	public void methodCflowbelowGetSet(StaticJoinPoint sjp) {
		//System.out.println(".methodCflowbelowGetSet");
		EnclosingSJPTest.registerEnclosingSJP(sjp.getEnclosingStaticJoinPoint());
	}

}
