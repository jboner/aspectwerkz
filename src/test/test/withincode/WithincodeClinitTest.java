/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.withincode;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.codehaus.aspectwerkz.joinpoint.CatchClauseRtti;
import org.codehaus.aspectwerkz.joinpoint.CatchClauseSignature;
import org.codehaus.aspectwerkz.joinpoint.ConstructorRtti;
import org.codehaus.aspectwerkz.joinpoint.ConstructorSignature;
import org.codehaus.aspectwerkz.joinpoint.EnclosingStaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.FieldRtti;
import org.codehaus.aspectwerkz.joinpoint.FieldSignature;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.codehaus.aspectwerkz.joinpoint.MethodSignature;
import org.codehaus.aspectwerkz.joinpoint.Rtti;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.impl.StaticInitializerSignatureImpl;
import org.codehaus.aspectwerkz.joinpoint.management.JoinPointType;


/**
 * Test for withincode(clinit).
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 */
public class WithincodeClinitTest extends TestCase {
	private static List s_messages  = new ArrayList();
	private static List s_staticJoinPoints = new ArrayList(); 
	private static List s_joinPoints = new ArrayList();
	
	private static final String[] EXPECTED_MSGS = {
	    "beforeCtorCall",
	    "beforeWithincodeClinitCtorCall",
	    "beforeWithincodeClinitPatternCtorCall",
	    "afterReturningCtorCall",
	    "afterCtorCall",
	    "afterWithincodeClinitCtorCall",
	    "afterWithincodeClinitPatternCtorCall",
	    "beforeGetSet",
	    "afterReturningGetSet",
	    "afterGetSet",
	    "beforeGetSet",
	    "afterReturningGetSet",
	    "afterGetSet",
	    "beforeMethodCall",
	    "afterThrowingTypeMethodCall",
	    "afterThrowingMethodCall",
	    "afterFinallyMethodCall",
	    "beforeHandler"
	};

	private static final Class[] EXPECTED_SIGNATURES = new Class[] {
	    ConstructorSignature.class,
	    ConstructorSignature.class,
	    ConstructorSignature.class,
	    ConstructorSignature.class,
		FieldSignature.class,
		FieldSignature.class,
		FieldSignature.class,
		FieldSignature.class,
		FieldSignature.class,
		FieldSignature.class,
		FieldSignature.class,
		FieldSignature.class,
		MethodSignature.class,
		MethodSignature.class,
		MethodSignature.class,
		MethodSignature.class,
		MethodSignature.class,
		CatchClauseSignature.class,
		CatchClauseSignature.class
	};
	
	private static final JoinPointType[] EXPECTED_JP_TYPES = new JoinPointType[] {
	    JoinPointType.CONSTRUCTOR_CALL,
	    JoinPointType.CONSTRUCTOR_CALL,
	    JoinPointType.CONSTRUCTOR_CALL,
	    JoinPointType.CONSTRUCTOR_CALL,
	    JoinPointType.FIELD_SET,
	    JoinPointType.FIELD_SET,
	    JoinPointType.FIELD_SET,
	    JoinPointType.FIELD_SET,
	    JoinPointType.FIELD_GET,
	    JoinPointType.FIELD_GET,
	    JoinPointType.FIELD_GET,
	    JoinPointType.FIELD_GET,
	    JoinPointType.METHOD_CALL,
	    JoinPointType.METHOD_CALL,
	    JoinPointType.METHOD_CALL,
	    JoinPointType.METHOD_CALL,
	    JoinPointType.METHOD_CALL,
	    JoinPointType.HANDLER,
	    JoinPointType.HANDLER
	};
	
	private static final Class ENCLOSING_SJP_CLASS = StaticInitializerSignatureImpl.class;
	private static final JoinPointType ENCLOSING_SJP_TYPE = JoinPointType.STATIC_INITIALIZATION;
	private static final String CALLER_CLASS_NAME = "test.withincode.Target";
	private static final String CALLER_INSTANCE = "null";
	
	private static final String[] CALLEE_CLASS_NAME = {
	    "test.withincode.Target$CtorCallTarget",
	    "test.withincode.Target$CtorCallTarget",
	    "test.withincode.Target$CtorCallTarget",
	    "test.withincode.Target$CtorCallTarget",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.withincode.Target",
	    "test.handler.HandlerTestBeforeException",
	    "test.handler.HandlerTestBeforeException"
	};
	
	private static final Class[] RTTI_CLASS = new Class[] {
	    ConstructorRtti.class,
	    ConstructorRtti.class,
	    ConstructorRtti.class,
	    ConstructorRtti.class,
	    FieldRtti.class,
	    FieldRtti.class,
	    FieldRtti.class,
	    FieldRtti.class,
	    FieldRtti.class,
	    FieldRtti.class,
	    FieldRtti.class,
	    FieldRtti.class,
	    MethodRtti.class,
	    MethodRtti.class,
	    MethodRtti.class,
	    MethodRtti.class,
	    MethodRtti.class,
	    CatchClauseRtti.class,
	    CatchClauseRtti.class
	};
	
	public void testWithincode() {
	    Class clazz = Target.class;

        try {
            // enfore clazz clinit triggering
            Object fake = clazz.newInstance();
        } catch (Exception e) {
            fail(e.toString());
        }

        checkMessages();
	    
	    checkStaticJoinPoints();
	    
	    checkJoinPoints();
	    
	}
	
	private void checkMessages() {
	    assertEquals("no of occured messages",
	            	EXPECTED_MSGS.length,
	            	s_messages.size()
	    );
	    
	    for(int i = 0; i < EXPECTED_MSGS.length; i++) {
	        assertEquals("expected message: " + EXPECTED_MSGS[i],
	                	EXPECTED_MSGS[i],
	                	s_messages.get(i)
	        );
	    }
	}

	private void checkStaticJoinPoints() {
	    assertEquals("captured SJP signature",
	            EXPECTED_SIGNATURES.length,
	            s_staticJoinPoints.size()
	    );
	    
	    for(int i = 0; i < EXPECTED_SIGNATURES.length; i++) {
	        StaticJoinPoint sjp = (StaticJoinPoint) s_staticJoinPoints.get(i);


			assertTrue(
			        EXPECTED_SIGNATURES[i].isAssignableFrom(sjp.getSignature().getClass())
			);

			assertEquals(EXPECTED_JP_TYPES[i], sjp.getType());
	        
	        EnclosingStaticJoinPoint esjp = sjp.getEnclosingStaticJoinPoint();

            assertTrue(
                    ENCLOSING_SJP_CLASS.isAssignableFrom(esjp.getSignature().getClass())
            );

            assertEquals(ENCLOSING_SJP_TYPE,
                         esjp.getType()
            );
		}
	}
	
	private void checkJoinPoints() {
	    assertEquals("captured JP signature",
	            EXPECTED_SIGNATURES.length,
	            s_joinPoints.size()
	    );
	    
	    for(int i = 0; i < s_joinPoints.size(); i++) {
	        JoinPoint jp = (JoinPoint) s_joinPoints.get(i);
	        
	        assertEquals(CALLER_CLASS_NAME, jp.getCallerClass().getName());
	        assertEquals(CALLEE_CLASS_NAME[i], jp.getCalleeClass().getName());
	        assertEquals(CALLER_INSTANCE, String.valueOf(jp.getCaller()));
	        assertEquals(CALLER_INSTANCE, String.valueOf(jp.getThis()));
	        
	        if(i < 4 || i > s_joinPoints.size() - 3) { // CTORS and HANDLERS CALLEE
	            assertNotNull(jp.getCallee());
	            assertNotNull(jp.getTarget());
	        } else {
	            assertEquals(CALLER_INSTANCE, String.valueOf(jp.getCallee()));
	            assertEquals(CALLER_INSTANCE, String.valueOf(jp.getTarget()));
	        }
	        
	        Rtti rtti = jp.getRtti();
	        
	        assertTrue("expected " + RTTI_CLASS[i].getName() + " found " + rtti.getClass().getName(),
	                RTTI_CLASS[i].isAssignableFrom(rtti.getClass()));
	    }
	}
	
	public static void addMessage(final String msg) {
	    s_messages.add(msg);
	}
	
	public static void addSJP(StaticJoinPoint sjp) {
	    s_staticJoinPoints.add(sjp);
	}
	
	public static void addJP(JoinPoint jp) {
	    s_joinPoints.add(jp);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(WithincodeClinitTest.class);
	}

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(WithincodeClinitTest.class);
    }


}
