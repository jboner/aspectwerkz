/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.expression;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ConstructorInfo;
import org.codehaus.aspectwerkz.reflect.FieldInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.reflect.ReflectHelper;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaClassInfo;

/**
 * Unit test for annotation matching.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 * @version $Revision: 1.4 $
 */
public class AnnotationExpressionTest extends TestCase {
	protected static final String NAMESPACE = "TESTING";

	protected static ClassInfo s_declaringType = JavaClassInfo.getClassInfo(AnnotationTarget.class);
	protected static ClassInfo s_innerType     = JavaClassInfo.getClassInfo(
			AnnotationTarget.ClassLevelAnnotation.class);
	
	protected static ConstructorInfo s_constructor;
	protected static ConstructorInfo s_innerConstructor;
	
	protected static MethodInfo s_method;
	protected static MethodInfo s_innerMethod;
	
	protected static FieldInfo s_field;
	protected static FieldInfo s_innerField;

    static {
        try {
            setMeUp();
        } catch (Throwable t) {
            throw new Error(t.toString());
        }
    }


	public void testConstructor() {
		assertTrue(
				new ExpressionInfo("call(@test.expression.IConstructor)", NAMESPACE).getExpression()
				.match(
						new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);
		
		assertTrue(
				new ExpressionInfo("call(@test.expression.IConstructor new())", NAMESPACE).getExpression()
						.match(
								new ExpressionContext(PointcutType.CALL, s_constructor, null)
								));

		assertTrue(
				new ExpressionInfo("call(@test.expression.IConstructor new(..))", NAMESPACE).getExpression()
						.match(
								new ExpressionContext(PointcutType.CALL, s_constructor, null)
								));

		assertTrue(
				new ExpressionInfo("call(@test.expression.IConstructor *.new())", NAMESPACE).getExpression()
				.match(
						new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);
		
		assertTrue(
				new ExpressionInfo("call(@test.expression.IConstructor test.*.AnnotationTarget.new())", NAMESPACE)
					.getExpression().match(
							new ExpressionContext(PointcutType.CALL, s_constructor, null)
							)
		);

		assertTrue(
				new ExpressionInfo("call(@test.expression.IConstructor test.expression.AnnotationTarget.new())", NAMESPACE)
					.getExpression().match(
							new ExpressionContext(PointcutType.CALL, s_constructor, null)
							)
		);

		assertFalse(
				new ExpressionInfo("call(@OtherConstructor)", NAMESPACE).getExpression().match(
						new ExpressionContext(PointcutType.CALL, s_constructor, null)));
		
		assertFalse(
				new ExpressionInfo("call(@test.expression.IConstructor new(*))", NAMESPACE).getExpression()
				.match(
						new ExpressionContext(PointcutType.CALL, s_constructor, null)));
		
		assertFalse(
				new ExpressionInfo("call(@test.expression.IConstructor test.expression.AnnotationTargetWRONG.new())", NAMESPACE)
					.getExpression().match(
							new ExpressionContext(PointcutType.CALL, s_constructor, null)
							)
		);

		assertTrue(
				new ExpressionInfo(
						"within(test.expression.AnnotationTarget) && execution(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, s_declaringType))
				);
		
		assertTrue(
				new ExpressionInfo(
						"within(test.expression.*) && execution(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, s_declaringType))
				);
		
		assertTrue(
				new ExpressionInfo(
						"within(@test.expression.IService) && execution(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_innerConstructor, s_innerType))
				);

		// HINT: constructor on AnnotationTarget
		assertTrue(
				new ExpressionInfo(
						"!within(@test.expression.IService) && execution(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, s_declaringType))
				);
		
		// HINT: constructor of inner (@test.expression.IService)
		assertFalse(
				new ExpressionInfo(
						"!within(@test.expression.IService) && execution(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_innerConstructor, s_innerType))
				);
				
		assertTrue(
				new ExpressionInfo(
						"withincode(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.WITHIN, s_constructor, s_constructor))
				);
		
		assertTrue(
				new ExpressionInfo(
						"withincode(@test.expression.IConstructor test.expression.AnnotationTarget.new())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.WITHIN, s_constructor, s_constructor))
				);
		
		assertTrue(
				new ExpressionInfo(
						"call(@test.expression.IAsynchronous) && withincode(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_innerMethod, s_innerConstructor))
				);
						
	}
	
	public void testHasMethod() {
        assertTrue(
                new ExpressionInfo(
                        "hasmethod(@test.expression.IAsynchronous)",
                        NAMESPACE
                ).getExpression().match(new ExpressionContext(PointcutType.EXECUTION, s_method, null))
        );
        
        assertTrue(
                new ExpressionInfo(
                        "hasmethod(@test.expression.IAsynchronous void methodOneAsynch())",
                        NAMESPACE
                ).getExpression().match(new ExpressionContext(PointcutType.CALL, s_method, s_method))
        );
        
        // HINT hasmethod on constructor
        assertTrue(
        		new ExpressionInfo(
        				"hasmethod(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, null))
				);

        assertTrue(
        		new ExpressionInfo(
        				"hasmethod(@test.expression.IConstructor new())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, s_constructor)
				)
		);
	}

    public void testHasField() throws Exception {
        assertTrue(
                new ExpressionInfo(
                        "hasfield(@test.expression.IPersistable)",
                        NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.EXECUTION, s_field, null))
        );
        
        assertTrue(
                new ExpressionInfo(
                        "hasfield(@test.expression.IPersistable)",
                        NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.CALL, s_field, s_field))
        );
        
        assertTrue(
                new ExpressionInfo(
                        "hasfield(@test.expression.IPersistable) && !within(@test.expression.IService)",
                        NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.CALL, s_field, s_declaringType))
        );
        
        assertFalse(
                new ExpressionInfo(
                        "hasfield(@test.expression.IPersistable) && !within(@test.expression.IService)",
                        NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.CALL, s_field, s_innerType))
        );
        
        assertTrue(
                new ExpressionInfo(
                        "hasfield(@test.expression.IPersistable) && within(@test.expression.IService)",
                        NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.CALL, s_innerField, s_innerType))
        );        
    }
    
    public void testFieldAttributes() {
    	assertTrue(
                new ExpressionInfo(
                		"set(@test.expression.IPersistable)",
						NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.SET, s_field, null))
        );
    	
    	assertTrue(
    			new ExpressionInfo(
    					"get(@test.expression.IPersistable) && !within(@test.expression.IService)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.GET, s_field, s_declaringType))
				);
    	
    	assertTrue(
    			new ExpressionInfo(
    					"set(@test.expression.IPersistable) && within(@test.expression.IService)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.SET, s_innerField, s_innerType))
				);
    	
    	assertFalse(
    			new ExpressionInfo(
    					"get(@test.expression.IPersistable) && !within(@test.expression.IService)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.GET, s_field, s_innerType))
				);
    	
    	assertFalse(
    			new ExpressionInfo(
    					"get(@test.expression.IPersistable) && !within(@test.expression.IService)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.GET, s_innerField, s_innerType))
				);
    }
    
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AnnotationExpressionTest.class);
	}

    protected static void setMeUp() throws Exception {
        Class clazz = AnnotationTarget.class;
        Class[] noParams = new Class[0];

        Method method = clazz.getMethod("methodOneAsynch", noParams);
        s_method = s_declaringType.getMethod(ReflectHelper.calculateHash(method));

        Constructor constructor = clazz.getConstructor(noParams);
        s_constructor = s_declaringType.getConstructor(
                ReflectHelper.calculateHash(constructor));

        Field field = clazz.getDeclaredField("m_annotatedField");
        s_field = s_declaringType.getField(ReflectHelper.calculateHash(field));

        clazz = AnnotationTarget.ClassLevelAnnotation.class;
        method = clazz.getMethod("innerMethodAsynch", noParams);
        s_innerMethod = s_innerType.getMethod(ReflectHelper.calculateHash(method));

        constructor = clazz.getConstructor(noParams);
        s_innerConstructor = s_innerType.getConstructor(ReflectHelper.calculateHash(constructor));

        field = clazz.getDeclaredField("m_innerField");
        s_innerField = s_innerType.getField(ReflectHelper.calculateHash(field));
    }
}