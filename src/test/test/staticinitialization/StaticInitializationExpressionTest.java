/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.staticinitialization;

import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.expression.ExpressionNamespace;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaClassInfo;

import junit.framework.TestCase;
import test.CallerSideAdviceTest;


/**
 * Expression parser test for staticinitialization.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a> 
 */
public class StaticInitializationExpressionTest extends TestCase {
	private static final String NAMESPACE = "TESTING";
    
	static {
		Object some = new Object();
	}
	
	public void testStaticInitializerExpression() {
		ClassInfo declaringType = JavaClassInfo.getClassInfo(StaticInitializationExpressionTest.class);
		
		assertTrue("should found <clinit>", declaringType.hasStaticInitializer());
		
		assertTrue(
				new ExpressionInfo(
						"staticinitialization(test.staticinitialization.StaticInitializationExpressionTest)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.STATIC_INITIALIZATION, declaringType.staticInitializer(), null))
		);


		assertTrue(
				new ExpressionInfo(
						"staticinitialization(test.*.*) && within(test.staticinitialization.StaticInitializationExpressionTest)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.STATIC_INITIALIZATION, declaringType.staticInitializer(), declaringType))
		);
						
        assertTrue(
                new ExpressionInfo(
                        "staticinitialization(test.staticinitialization.StaticInitializationExpressionTest)",
                        NAMESPACE).getAdvisedClassFilterExpression().match(
                new ExpressionContext(PointcutType.STATIC_INITIALIZATION, null, declaringType))
        );

        assertTrue(
                new ExpressionInfo(
                        "staticinitialization(test.staticinitialization.StaticInitializationExpressionTest)",
                        NAMESPACE).getAdvisedClassFilterExpression().match(
                new ExpressionContext(PointcutType.STATIC_INITIALIZATION, null, declaringType.staticInitializer()))
        );
	}
	
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(StaticInitializationExpressionTest.class);
    }
}
