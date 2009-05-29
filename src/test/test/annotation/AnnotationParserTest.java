/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.annotation;


import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 */
public class AnnotationParserTest extends TestCase {

    public void testSome() {
        //TODO rename and remove from AllTest
        // caution: the inner class are used as annotation and thus sensitive to refactoring..
    }

	public static interface VoidTyped {
	}

	public static interface Simple {

		public String val();

		public String s();
	}

	public static interface SimpleNested {

		public Simple nested();
	}

	public static interface SimpleDefaultNested {
		public DefaultString nested();
	}

	public static interface SimpleValueDefaultNested {
		public DefaultString value();
	}

	public static interface SimpleStringArrayNested {
		public StringArray nested();
	}

	public static interface ComplexNested {

		public Simple[] nesteds();
	}

	public static interface DefaultString {

		public String value();
	}

    public static interface PackagedDefaultString {

        public String value();
    }

	public static interface DefaultInt {

		public int value();
	}

	public static interface Complex {

		public int i();

		public long[] ls();

		public Class klass();

		public Class[] klass2();

		public int field();

	}

	public static interface StringArray {
		public int i();

		public String[] ss();
	}

	public static interface Untyped {
		public String value();
	}
}