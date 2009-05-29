/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.enclosingsjp;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.aspectwerkz.joinpoint.ConstructorSignature;
import org.codehaus.aspectwerkz.joinpoint.EnclosingStaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodSignature;
import org.codehaus.aspectwerkz.joinpoint.Signature;
import org.codehaus.aspectwerkz.joinpoint.management.JoinPointType;

import junit.framework.TestCase;


public class EnclosingSJPTest extends TestCase {
	private static List s_enclosingStaticJPList = new ArrayList();
	
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		s_enclosingStaticJPList.clear();
	}

	public static void registerEnclosingSJP(EnclosingStaticJoinPoint esjp) {
		s_enclosingStaticJPList.add(esjp);
	}
	
	public void testConstructorEnclosing() throws NoSuchMethodException {
		EnclosingTarget et = new EnclosingTarget();
		
		Class[] expectedSignaturesTypes = new Class[] {
				ConstructorSignature.class,
				ConstructorSignature.class,
				ConstructorSignature.class,
				ConstructorSignature.class,
				ConstructorSignature.class,
				ConstructorSignature.class,
				ConstructorSignature.class,
				ConstructorSignature.class
		};
		
		JoinPointType[] expectedJPTypes = new JoinPointType[] {
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION
		};
		
		check(expectedSignaturesTypes,
		      expectedJPTypes,
		      s_enclosingStaticJPList);
		
		Constructor ctor = et.getClass().getConstructor(new Class[0]);
		
		for(int i = 0; i < s_enclosingStaticJPList.size(); i++) {
			ConstructorSignature ctorSig = 
				(ConstructorSignature) ((EnclosingStaticJoinPoint) s_enclosingStaticJPList.get(i)).getSignature();
			
			assertTrue("" + ctor.toString(),
					ctor.equals(ctorSig.getConstructor()));
		}
	}
	
	public void testHandlerEnclosing() throws NoSuchMethodException {
		try {
			throw new IllegalAccessException("msg1");
		} catch(IllegalAccessException iae) {
			;
		}
		
		EnclosingTarget et = new EnclosingTarget(1);
		
		Class[] expectedSignaturesTypes = new Class[] {
				MethodSignature.class,
				MethodSignature.class,
				ConstructorSignature.class,
				ConstructorSignature.class
		};
		
		JoinPointType[] expectedJPTypes = new JoinPointType[] {
				JoinPointType.METHOD_EXECUTION,
				JoinPointType.METHOD_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION
		};
		
		check(expectedSignaturesTypes,
		      expectedJPTypes,
		      s_enclosingStaticJPList);
		
		Constructor ctor = et.getClass().getConstructor(new Class[] {int.class});
		Method		meth = getClass().getMethod("testHandlerEnclosing", (Class[])null);

		assertTrue(
				meth.toString(),
				meth.equals(
				((MethodSignature) ((EnclosingStaticJoinPoint) s_enclosingStaticJPList.get(0))
						.getSignature()).getMethod())
		);
		assertTrue(
				meth.toString(),
				meth.equals(
				((MethodSignature) ((EnclosingStaticJoinPoint) s_enclosingStaticJPList.get(1))
						.getSignature()).getMethod())
		);
		assertTrue(
				ctor.toString(),
				ctor.equals(
				((ConstructorSignature) ((EnclosingStaticJoinPoint) s_enclosingStaticJPList.get(2))
						.getSignature()).getConstructor())
		);
		assertTrue(
				ctor.toString(),
				ctor.equals(
	            ((ConstructorSignature) ((EnclosingStaticJoinPoint) s_enclosingStaticJPList.get(3))
	            		.getSignature()).getConstructor())
		);
	}
	
	public void testGetSet() throws NoSuchMethodException {
		EnclosingTarget et = new EnclosingTarget(new Object());
		
		Class[] expectedSignatureTypes = new Class[] {
				ConstructorSignature.class,
				ConstructorSignature.class,
				ConstructorSignature.class,
				MethodSignature.class,
				MethodSignature.class,
				MethodSignature.class,
				MethodSignature.class,
				MethodSignature.class,
				MethodSignature.class
		};
		
		JoinPointType[] expectedJPTypes = new JoinPointType[] {
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.CONSTRUCTOR_EXECUTION,
				JoinPointType.METHOD_EXECUTION,
				JoinPointType.METHOD_EXECUTION,
				JoinPointType.METHOD_EXECUTION,
				JoinPointType.METHOD_EXECUTION,
				JoinPointType.METHOD_EXECUTION,
				JoinPointType.METHOD_EXECUTION
		};
		
		check(expectedSignatureTypes,
				expectedJPTypes,
				s_enclosingStaticJPList);
		
		Constructor ctor = et.getClass().getConstructor(new Class[] {Object.class});
		Method setMethod = PointcutTarget.class.getMethod("setFieldValue", new Class[] {Object.class});
		Method getMethod = PointcutTarget.class.getMethod("getFieldValue", (Class[])null);
		
		for(int i = 0; i < 3; i++) {
			EnclosingStaticJoinPoint esjp = (EnclosingStaticJoinPoint) s_enclosingStaticJPList.get(i);
			Constructor enclosingCtor = ((ConstructorSignature) esjp.getSignature()).getConstructor();
			
			assertTrue(
					ctor.toString(),
					ctor.equals(enclosingCtor));
		}
		
		for(int i = 3; i < 6; i++) {
			EnclosingStaticJoinPoint esjp = (EnclosingStaticJoinPoint) s_enclosingStaticJPList.get(i);
			Method method = ((MethodSignature) esjp.getSignature()).getMethod();
			
			assertTrue(
					setMethod.toString(),
					setMethod.equals(method));
		}
		
		for(int i = 6; i < s_enclosingStaticJPList.size(); i++) {
			EnclosingStaticJoinPoint esjp = (EnclosingStaticJoinPoint) s_enclosingStaticJPList.get(i);
			Method method = ((MethodSignature) esjp.getSignature()).getMethod();
			
			assertTrue(
					getMethod.toString(),
					getMethod.equals(method));
		}

	}
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(EnclosingSJPTest.class);
	}

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(EnclosingSJPTest.class);
    }

	private void check(
			Class[] signatureClass,
			JoinPointType[] enclosingTypes, 
			List esjpList) {
		
		assertEquals(enclosingTypes.length, esjpList.size());
		
		for(int i = 0; i < enclosingTypes.length; i++) {
			EnclosingStaticJoinPoint esjp = (EnclosingStaticJoinPoint) esjpList.get(i);
			
			assertNotNull("EnclosingStaticJoinPoint should never be null", esjp);
			
			assertNotNull("Signature should not be null", esjp.getSignature());
					
			Signature sig = esjp.getSignature();
			
			if(sig instanceof ConstructorSignature) {
				assertNotNull(((ConstructorSignature) sig).getConstructor());
			} else if(sig instanceof MethodSignature) {
				assertNotNull(((MethodSignature) sig).getMethod());
			} else {
				fail("unexpected signature type: " + sig.getClass().getName());
			}
			
			assertEquals("expectation on enclosing JP type failed", 
					enclosingTypes[i], 
					esjp.getType());
			
			assertTrue("expectation on enclosing Signature class failed", 
					(signatureClass[i].isAssignableFrom(esjp.getSignature().getClass())));

		}
	}
}
