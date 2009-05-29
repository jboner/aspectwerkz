/*
 * $Id: AdvisedClassFilterExpressionTest.java,v 1.3 2005/03/24 14:51:20 avasseur Exp $
 * $Date: 2005/03/24 14:51:20 $
 */
package test.expression;


import org.codehaus.aspectwerkz.exception.DefinitionException;
import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.expression.ExpressionNamespace;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ConstructorInfo;
import org.codehaus.aspectwerkz.reflect.FieldInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaClassInfo;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaMethodInfo;

import junit.framework.TestSuite;

/**
 * Test unit for AdvisedClassFilterExpressionVisitor
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 * @version $Revision: 1.3 $
 */
public class AdvisedClassFilterExpressionTest extends AnnotationExpressionTest {

    static {
        try {
            setMeUp();
        } catch (Throwable t) {
            throw new Error(t.toString());
        }
    }

	public void testAdvisedConstructor() {
		assertTrue(
				new ExpressionInfo(
						"call(@test.expression.IConstructor test.*.AnnotationTarget.new())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);

		assertFalse(
				new ExpressionInfo(
						"call(@OtherConstructor test.*.AnnotationTarget.new())", 
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);
		
		assertTrue(
				new ExpressionInfo(
						"call(@OtherConstructor test.*.AnnotationTarget.new())", 
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);
		
		assertTrue(
				new ExpressionInfo(
						"call(@test.expression.IConstructor test.expression.AnnotationTarget.new())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);

		assertFalse(
				new ExpressionInfo(
						"call(@OtherConstructor test.expression.AnnotationTarget.new())", 
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);
		
		assertTrue(
				new ExpressionInfo(
						"call(@OtherConstructor test.expression.AnnotationTarget.new())", 
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"call(@OtherConstructor)", 
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);
		
		// no type info
		assertTrue(
				new ExpressionInfo(
						"call(@OtherConstructor)", 
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"call(@test.expression.IConstructor test.expression.AnnotationTargetWRONG.new())",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.CALL, s_constructor, null)
				)
		);

		assertTrue(
				new ExpressionInfo(
						"within(test.expression.AnnotationTarget) && execution(@test.expression.IConstructor)",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, s_declaringType)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"within(test.expression.AnnotationTarget) && execution(@OtherConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, s_declaringType)
				)
		);
		
		assertTrue(
				new ExpressionInfo(
						"within(test.expression.AnnotationTarget) && execution(@OtherConstructor)",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, s_declaringType)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"within(test.expressionFAKE.*) && execution(@test.expression.IConstructor)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, s_declaringType)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"within(test.expressionFAKE.*) && execution(@test.expression.IConstructor)",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_constructor, s_declaringType)
				)
		);
		
		
		assertTrue(
				new ExpressionInfo(
						"within(@test.expression.IService test.expression.*) && execution(@test.expression.IConstructor test.expression.*.new())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_innerConstructor, s_innerType)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"within(!@test.expression.IService test.expression.*) && execution(@test.expression.IConstructor test.expression.*.new())",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_innerConstructor, s_innerType)
				)
		);

		assertTrue(
				new ExpressionInfo(
						"withincode(@test.expression.IConstructor test.expression.AnnotationTarget$ClassLevelAnnotation.new())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.WITHIN, s_innerConstructor, s_innerConstructor)
				)
		);
	}
	
	public void testAdvisedMethod() {
		assertTrue(
				new ExpressionInfo(
						"call(@test.expression.IAsynchronous void test.*.AnnotationTarget.methodOneAsynch())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_method, null)
				)
		);

		assertFalse(
				new ExpressionInfo(
						"call(@Synch void test.*.AnnotationTarget.methodOneAsynch())", 
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_method, null)
				)
		);
		
		assertTrue(
				new ExpressionInfo(
						"call(@Synch int test.*.AnnotationTarget.methodOneAsynch())", 
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.CALL, s_method, null)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"call(@Synch)", 
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.CALL, s_method, null)
				)
		);
		
		// no type info
		assertTrue(
				new ExpressionInfo(
						"call(@Synch)", 
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.CALL, s_method, null)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"call(@test.expression.IAsynchronous * test.expression.AnnotationTargetWRONG.methodOneAsynch())",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.CALL, s_method, null)
				)
		);

		assertFalse(
				new ExpressionInfo(
						"within(test.expression.AnnotationTarget) && execution(@Synch)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_method, s_declaringType)
				)
		);
		
		// HINT no type in execution
		assertTrue(
				new ExpressionInfo(
						"within(test.expression.AnnotationTarget) && execution(@Synch)",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_method, s_declaringType)
				)
		);
		
		assertTrue(
				new ExpressionInfo(
						"within(@test.expression.IService test.expression.AnnotationTarget$ClassLevelAnnotation) && execution(@test.expression.IAsynchronous void innerMethodAsynch())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_innerMethod, s_innerType)
				)
		);
		
		assertFalse(
				new ExpressionInfo(
						"within(!@test.expression.IService test.expression.AnnotationTarget$ClassLevelAnnotation) && execution(@test.expression.IAsynchronous void innerMethodAsynch())",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.EXECUTION, s_innerMethod, s_innerType)
				)
		);
		
		assertTrue(
				new ExpressionInfo(
						"withincode(@test.expression.IAsynchronous void test.expression.AnnotationTarget$ClassLevelAnnotation.innerMethodAsynch())",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.WITHIN, s_innerMethod, s_innerMethod)
				)
		);
		
		// HINT annotation and return not considered
		assertTrue(
				new ExpressionInfo(
						"withincode(!@test.expression.IAsynchronous int test.expression.AnnotationTarget$ClassLevelAnnotation.innerMethodAsynch())",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.WITHIN, s_innerMethod, s_innerMethod)
				)
		);
	}
	
	public void testAdvisedHasMethod() {
        assertTrue(
                new ExpressionInfo(
                        "hasmethod(@test.expression.IAsynchronous void test.expression.AnnotationTarget.methodOneAsynch())",
                        NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.EXECUTION, s_method, null)
				)
        );
        
        assertFalse(
                new ExpressionInfo(
                        "hasmethod(!@test.expression.IAsynchronous int test.expression.AnnotationTarget.methodOneAsynch())",
                        NAMESPACE).getAdvisedClassFilterExpression().match(
                new ExpressionContext(PointcutType.EXECUTION, s_method, null)
				)
        );
        
        assertTrue(
                new ExpressionInfo(
                        "hasmethod(@test.expression.IConstructor test.expression.*.new())",
                        NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.EXECUTION, s_innerConstructor, null)
				)
        );
        
        assertFalse(
                new ExpressionInfo(
                        "hasmethod(!@test.expression.IConstructor test.expression.*.new())",
                        NAMESPACE).getAdvisedClassFilterExpression().match(
                new ExpressionContext(PointcutType.EXECUTION, s_innerConstructor, null)
				)
        );
	}

    public void testAdvisedHasField() throws Exception {
        assertTrue(
                new ExpressionInfo(
                        "hasfield(@test.expression.IPersistable java.lang.Object+ test.expression.AnnotationTarget.m_annotatedField)",
                        NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.GET, s_field, s_declaringType)
				)
        );
        
        assertFalse(
                new ExpressionInfo(
                        "hasfield(!@test.expression.IPersistable int test.expression.AnnotationTarget.m_annotatedField)",
                        NAMESPACE).getAdvisedClassFilterExpression().match(
                new ExpressionContext(PointcutType.GET, s_field, s_declaringType)
				)
        );
    }
    
    public void testAdvisedFieldAttributes() {
    	assertTrue(
                new ExpressionInfo(
                		"set(@test.expression.IPersistable java.lang.Object+ test.expression.AnnotationTarget.m_annotatedField)",
						NAMESPACE).getExpression().match(
                new ExpressionContext(PointcutType.SET, s_field, null)
				)
        );
    	
    	assertTrue(
                new ExpressionInfo(
                		"set(!@test.expression.IPersistable int test.expression.AnnotationTarget.m_annotatedField)",
						NAMESPACE).getAdvisedClassFilterExpression().match(
                new ExpressionContext(PointcutType.SET, s_field, null)
				)
        );
    	
    	// HINT wrong field type
    	assertFalse(
    			new ExpressionInfo(
    					"get(@test.expression.IPersistable int test.expression.AnnotationTarget.m_annotatedField) && !within(@test.expression.IService)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.GET, s_field, s_declaringType)
				)
		);
    	
    	// HINT field type ignored
    	assertTrue(
    			new ExpressionInfo(
    					"get(@test.expression.IPersistable int test.expression.AnnotationTarget.m_annotatedField) && !within(@test.expression.IService)",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.GET, s_field, s_declaringType)
				)
		);

    	assertFalse(
    			new ExpressionInfo(
    					"get(@test.expression.IPersistable java.lang.Object m_innerField) && within(!@test.expression.IService test.expression.*)",
						NAMESPACE).getExpression().match(
				new ExpressionContext(PointcutType.GET, s_innerField, s_innerType)
				)
		);
    	
    	// HINT annotations and types ignored
    	assertTrue(
    			new ExpressionInfo(
    					"get(!@test.expression.IPersistable String m_innerField) && within(@test.expression.IService test.expression.*)",
						NAMESPACE).getAdvisedClassFilterExpression().match(
				new ExpressionContext(PointcutType.GET, s_innerField, s_innerType)
				)
		);
    }
    
	public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AdvisedClassFilterExpressionTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}