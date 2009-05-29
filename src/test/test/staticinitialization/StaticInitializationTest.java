/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.staticinitialization;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.codehaus.aspectwerkz.joinpoint.EnclosingStaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.Rtti;
import org.codehaus.aspectwerkz.joinpoint.Signature;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.impl.StaticInitializationRttiImpl;
import org.codehaus.aspectwerkz.joinpoint.impl.StaticInitializerSignatureImpl;
import org.codehaus.aspectwerkz.joinpoint.management.JoinPointType;
import test.CallerSideAdviceTest;


/**
 * Test for staticinitialization pointcuts.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 */
public class StaticInitializationTest extends TestCase {
	public static final String[] BEFORE_EXPECTED_MESSAGES = {
		"beforeStaticinitialization",
		"aroundStaticinitializationSJP",
		"aroundStaticinitializationJP"
	};
	
	public static final String[] AFTER_EXPECTED_MESSAGES = {
		"afterReturningStaticinitialization",
		"afterStaticinititalization"
	};
	
	public static final String CLINIT_EXECUTION_MESSAGE = "<clinit>.execution";
	
	public static List s_messages = new ArrayList();
	public static List s_staticJoinPoints = new ArrayList();
	public static List s_joinPoints = new ArrayList();
	
	public void testStaticInitializer() throws ClassNotFoundException {
		Class reflectClazz = Class.forName("test.staticinitialization.ClinitTarget"); 
        try {
            // required to run the clinit on Java 1.5
            reflectClazz.newInstance();
        } catch (Exception e) {
            fail(e.toString());
        }

        checkMessages();
		
		checkStaticJoinPoints(reflectClazz, s_staticJoinPoints);
		checkStaticJoinPoints(reflectClazz, s_joinPoints);
		
		checkJoinPoints(reflectClazz);
	}

	private void checkMessages() {
		int messages = 3 * (BEFORE_EXPECTED_MESSAGES.length 
             	+ AFTER_EXPECTED_MESSAGES.length) + 1;
		
		assertEquals("logged messages should match",
		             messages,
		             s_messages.size());
		
		for(int i = 0; i < BEFORE_EXPECTED_MESSAGES.length; i++) {
			for(int j = 0; j < 3; j++) {
				assertEquals(BEFORE_EXPECTED_MESSAGES[i],
				             s_messages.get(i * 3 + j));
			}
		}
		
		int lastBeforeIndex = 3 * BEFORE_EXPECTED_MESSAGES.length;
		
		assertEquals("clinit was expected to execute",
		             CLINIT_EXECUTION_MESSAGE,
		             s_messages.get(lastBeforeIndex));
		
		lastBeforeIndex++;
		
		for(int i = 0; i < AFTER_EXPECTED_MESSAGES.length; i++) {
			for(int j = 0; j < 3; j++) {
				assertEquals(AFTER_EXPECTED_MESSAGES[i],
				             s_messages.get(lastBeforeIndex + (i * 3) + j));
			}
		}
	}
	
	private void checkStaticJoinPoints(Class clazz, List data) {
		assertEquals("staticjoinpoints number does not match",
		             12,
		             data.size()
		);
		
		Class signatureClass = StaticInitializerSignatureImpl.class;
		
		for(int i = 0; i < data.size(); i++) {
			StaticJoinPoint sjp = (StaticJoinPoint) data.get(i);
			
			assertEquals(clazz,
			             sjp.getCallerClass());
			
			assertEquals(clazz,
			             sjp.getCalleeClass());
			
			assertEquals(JoinPointType.STATIC_INITIALIZATION,
			             sjp.getType());
			
			Signature signature = sjp.getSignature();
			assertNotNull(signature);
			
			assertEquals(signatureClass,
			             signature.getClass());
			
			assertEquals(clazz,
			             signature.getDeclaringType());
			
			EnclosingStaticJoinPoint esjp = sjp.getEnclosingStaticJoinPoint();
			
			assertNotNull(esjp);
			
			assertEquals(JoinPointType.STATIC_INITIALIZATION,
			             esjp.getType());
			
			Signature enclSig = esjp.getSignature();
			
			assertNotNull(enclSig);
			
			assertEquals(signatureClass,
			             enclSig.getClass());
			
			assertEquals(clazz,
			             enclSig.getDeclaringType());

		}
	}
	
	private void checkJoinPoints(Class clazz) {
		assertEquals("joinpoints number does not match",
		             12,
		             s_staticJoinPoints.size()
		);
		
		Class siRtti = StaticInitializationRttiImpl.class;
		
		for(int i = 0; i < s_joinPoints.size(); i++) {
			JoinPoint jp = (JoinPoint) s_joinPoints.get(i);
			
			assertNull(jp.getCaller());
			
			assertNull(jp.getThis());
			
			assertNull(jp.getCallee());
			
			assertNull(jp.getTarget());
			
			Rtti rtti = jp.getRtti();
			
			assertNotNull(rtti);
			
			assertEquals(siRtti,
			             rtti.getClass());
			
			assertEquals(clazz,
			             rtti.getDeclaringType()
			);
			
			assertNull(rtti.getThis());
			
			assertNull(rtti.getTarget());
		}
	}
	
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(StaticInitializationTest.class);
    }
}
