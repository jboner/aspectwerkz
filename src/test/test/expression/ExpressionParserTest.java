/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.expression;

import junit.framework.TestCase;

import org.codehaus.aspectwerkz.expression.ast.ExpressionParser;
import org.codehaus.aspectwerkz.expression.ast.ParseException;
import org.codehaus.aspectwerkz.expression.ast.TokenMgrError;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;


/**
 * Unit test for expression parser.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 */
public class ExpressionParserTest extends TestCase {
	private static final ExpressionParser PARSER = ExpressionInfo.getParser();
	
	public void testMethodPatternJP() {
		for(int i = 0; i < s_methodLikeExpressions.length; i++) {
			parse("call(" + s_methodLikeExpressions[i] + ")");
			parse("!call(" + s_methodLikeExpressions[i] + ")");
			parse("execution(" + s_methodLikeExpressions[i] + ")");
			parse("!execution(" + s_methodLikeExpressions[i] + ")");
			parse("withincode(" + s_methodLikeExpressions[i] + ")");
			parse("! withincode(" + s_methodLikeExpressions[i] + ")");
		}
	}
	
	public void testHasmethodJP() {
		for(int i = 0; i < s_methodLikeExpressions.length; i++) {
			parse("! hasmethod(" + s_methodLikeExpressions[i] + ")");
		}

		for(int i = 0; i < s_methodLikeExpressions.length; i++) {
			parse("hasmethod(" + s_methodLikeExpressions[i] + ")");
		}

	}

	public void testComplexMethodLevelJP() {
		for(int i = 0; i < s_methodLikeExpressions.length; i++) {
			for(int j = 0; j < METHOD_EXPRESSIONS.length; j++) {
				parse("call(" + s_methodLikeExpressions[i] + ") AND "
						+ "hasmethod(" + METHOD_EXPRESSIONS[j] + ")");
				
				parse("call(" + s_methodLikeExpressions[i] + ") AND "
						+ "!hasmethod(" + METHOD_EXPRESSIONS[j] + ")");
				
				parse("call(" + s_methodLikeExpressions[i] + ") && "
						+ "!hasmethod(" + METHOD_EXPRESSIONS[j] + ")");
				
				parse("call(" + s_methodLikeExpressions[i] + ") OR "
						+ "!hasmethod(" + METHOD_EXPRESSIONS[j] + ")");
				
				parse("call(" + s_methodLikeExpressions[i] + ") || "
						+ "!hasmethod(" + METHOD_EXPRESSIONS[j] + ")");
			}
		}
	}
	
	public void testFieldPatternJP() {
		for(int i = 0; i < FIELD_EXPRESSIONS.length; i++) {
			parse("set(" + FIELD_EXPRESSIONS[i] + ")");
			
			parse("get(" + FIELD_EXPRESSIONS[i] + ")");
			
			parse("hasfield(" + FIELD_EXPRESSIONS[i] + ")");
		}
	}
	
	public void testSpecial() {
		parse("(set(* foo.bar.*) || get(* foo.bar.*)) && withincode(* foo.bar.Buzz.*(..))");
		
		parse("execution(* foo.bar.Baz.*(..)) || call(* foo.bar.Baz.*(..))");
		
		parse("handler(java.lang.Exception+)");
		
		parse("!cflow(call(* foo.bar.Buzz.*(..)))");
		
		parse("handler(java.lang.Exception+) && !cflow(call(* foo.bar.Buzz.*(..)))");
		
		parse("call(!public !static * *..*.*(..))");
	}
	
	public void testClassPatternJP() {
		for(int i = 0; i < TYPE_EXPRESSIONS.length; i++) {
			parse("within(" + TYPE_EXPRESSIONS[i] + ")");
			
			parse("staticinitialization(" + TYPE_EXPRESSIONS[i] + ")");
		}
	}

	/* FIXME ArgParameters()
	public void testArgsAndPointcutrefs() {
		assertNull("args()", parseString("args()"));
		
		assertNotNull("args(,, String)", parseString("args(,, String)"));
		
		assertNotNull("args(, , String, , int)", parseString("args(, , String, , int)"));
		
		assertNull("pointcutref()", parseString("pointcutref()"));
		
		assertNull("pointcutref(arg1)", parseString("pointcutref(arg1)"));
		
		assertNull("pointcutref(..)", parseString("pointcutref(..)"));
		
		assertNull("pointcutref(arg1, ..)", parseString("pointcutref(arg1, ..)"));
		
		assertNotNull("pointcutref(, arg2)", parseString("pointcutref(, arg2)"));
		
		assertNotNull("pointcutref(, arg2, , arg4)", parseString("pointcutref(, arg2, , arg4)"));
	}
	*/
	
	
	public void testWithincodeStaticinitialization() {
	    for(int i = 0; i < TYPE_EXPRESSIONS.length; i++) {
	        Throwable cause = parseString("withincode(staticinitialization(" 
	                + TYPE_EXPRESSIONS[i] + "))");
	        if(null != cause) {
	            cause.printStackTrace();
	        }
			assertNull(TYPE_EXPRESSIONS[i] + cause, 
			           cause);
		}
	}
	
	private void parse(String expression) {
		try {
			PARSER.parse(expression);
		} catch(ParseException e) {
			fail("parsing [" + expression +"] failed because:\n" + e.getMessage());
		}
	}
	
	private Throwable parseString(String expression) {
		try {
			PARSER.parse(expression);
		} catch(ParseException pe) {
			return pe;
		} catch(TokenMgrError tokenerr) {
			return tokenerr;
		} catch(Exception ex) {
			return ex;
		}
		
		return null;
	}
	
	private static final String[] METHOD_EXPRESSIONS = {
			"int foo.*.Bar.method()",
			"int *.method(*)",
			"* method(..)",
			"int foo.*.*.method(*,int)",
			"int foo.*.Bar.method(..)",
			"int foo.*.Bar.method(int,..)",
			"int foo.*.Bar.method(java.lang.*)",
			"int foo.*.Bar.me*o*()",
			"* foo.*.Bar.method()", 
			"java.lang.* foo.*.Bar.method()",
			"static int foo.*.Bar.method()",
			"synchronized static int foo.*.Bar.method()",
			"!synchronized !static int foo.*.Bar.method()",
			"@Transaction * foo.*.*.*(..)",
			"@Transaction static * foo.*.*.*(..)",
			"@Transaction",
			"! @Transaction",
			"@Transaction !static * foo.*.*.*(..)",
			"!@Transaction !static * foo.*.*.*(..)"
	};
			
	private static final String[] CONSTRUCTOR_EXPRESSIONS = {
			"foo.*.Bar.new()",
			"*.new(*)",
			"foo.*.*.new(*,int)",
			"foo.*.Bar.new(..)",
			"foo.*.Bar.new(int,..)",
			"foo.*.Bar.new(java.lang.*)",
			"foo.*.Bar.new()",
			"@Transaction foo.*.*.new(..)",
	};

	private static final String[] FIELD_EXPRESSIONS = {
			"int foo.*.Bar.m_foo",
			"* m_field",
			"* foo.*.Bar.m_foo",
			"java.lang.* foo.*.Bar.m_foo",
			"int foo.*.Bar.m_*",
			"int foo.*.Bar.m_*oo*",
			"int foo.bar.Baz.*",
			"int foo.*.Bar+.m_foo",
			"* foo.*.Bar+.m_foo",
			"java.lang.* foo.*.Bar+.m_foo",
			"private static final String foo.*.bar....m_field",
			"private static final String foo.*.bar.*.m_field",
			"private static final String foo.*.bar.*.*",
			"@Persistent",
			"@Persistent * m_field",
			"@Persistent int foo.*.Bar.m_foo",
			"@Persistent * foo.*.Bar.m_foo",
			"@Persistent java.lang.* foo.*.Bar.m_foo",
			"@Persistent int foo.*.Bar.m_*",
			"@Persistent int foo.*.Bar.m_*oo*",
			"@Persistent int foo.bar.Baz.*",
			"@Persistent * foo.bar.Baz.*",
			"@Persistent private static final String foo.*.bar....m_field",
			"@Persistent private static final String foo.*.bar.*.m_field",
			"@Persistent private static final String foo.*.bar.*.*",
			"@Persistent @Instrumentable",
			"@Persistent @Instrumentable * m_field",
			"@Persistent @Instrumentable int foo.*.Bar.m_foo",
			"@Persistent @Instrumentable * foo.*.Bar.m_foo",
			"@Persistent @Instrumentable java.lang.* foo.*.Bar.m_foo",
			"@Persistent @Instrumentable int foo.*.Bar.m_*",
			"@Persistent @Instrumentable int foo.*.Bar.m_*oo*",
			"@Persistent @Instrumentable int foo.bar.Baz.*",
			"@Persistent @Instrumentable int foo.bar.Baz+.*",
			"@Persistent @Instrumentable * foo.bar.Baz.*",
			"@Persistent @Instrumentable private static final String foo.*.bar....m_field",
			"@Persistent @Instrumentable private static final String foo.*.bar.*.m_field",
			"@Persistent @Instrumentable private static final String foo.*.bar.*.*"

	};
	
	private static final String[] TYPE_EXPRESSIONS = {
			"foo.bar.*",
			"foo.*.FooBar",
			"foo.*.FooB*",
			"foo..",
			"public foo.bar.*",
			"@Session foo.bar.*",
			"@Session",
			"@Session @test.expression.IService foo....bar.*",
			"@Session @test.expression.IService public abstract foo....bar.*"
			
	};
	
	private static String[] s_methodLikeExpressions;
	
	static {
		s_methodLikeExpressions = new String[METHOD_EXPRESSIONS.length 
														 + CONSTRUCTOR_EXPRESSIONS.length];
		for(int i = 0; i < METHOD_EXPRESSIONS.length; i++) {
			s_methodLikeExpressions[i] = METHOD_EXPRESSIONS[i];
		}
		for(int i = 0; i < CONSTRUCTOR_EXPRESSIONS.length; i++) {
			s_methodLikeExpressions[METHOD_EXPRESSIONS.length + i]
											= CONSTRUCTOR_EXPRESSIONS[i];
		}
	}


    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ExpressionParserTest.class);
    }

}
