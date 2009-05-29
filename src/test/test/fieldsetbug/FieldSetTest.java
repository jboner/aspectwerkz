/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.fieldsetbug;


import test.ClassInfoTest;
import junit.framework.TestCase;

/**
 * AW-437 set pc and around advice
 * 
 * @author <a href='mailto:the_mindstorm@evolva.ro'>Alexandru Popescu</a>
 */
public class FieldSetTest extends TestCase {
    public void testNonLongDoublePublicFieldSet() {
        TargetClass instance1ofA = new TargetClass();

        instance1ofA.publicIntField = 2;
        assertEquals("should not have access to the field", 1, instance1ofA.publicIntField);

        instance1ofA.publicCharField = 'b';
        assertEquals("should not have access to the field", 'a', instance1ofA.publicCharField);

        try {
            mayThrowException();
        } catch(Exception e) {
            ;
        }
    }

    public void testLongDoublePublicFieldSet() {
		TargetClass tc = new TargetClass();

		tc.publicLongField = 2L;
		assertEquals("should not have access to the field", 1L, tc.publicLongField);

		tc.publicDoubleField = 2D;
        assertEquals("should not have access to the field", 1D, tc.publicLongField, 0D);
	}
    
    /**
     * java.lang.VerifyError: (class: test/fieldsetbug/FieldSetTest, method: testLongDoublePublicFieldSet signature: ()V) 
     * Inconsistent stack height 0 != 2
     */
    public void testLongDoublePublicFieldSetWithExceptionHandling() {
		TargetClass instance1ofA = new TargetClass();

		instance1ofA.publicLongField = 2L;
		assertEquals("should not have access to the field", 1L, instance1ofA.publicLongField);

		instance1ofA.publicDoubleField = 2D;
        assertEquals("should not have access to the field", 1D, instance1ofA.publicLongField, 0D);

		try {
			mayThrowException();
		} catch(Exception e) {
			;
		}
	}
    
    public void testCtorAssignNonLongDoublePublicFieldSet() {
        TargetClass tc = new TargetClass(2);
        assertEquals("should have access to the field", 2, tc.publicIntField);

        tc = new TargetClass('b');
        assertEquals("should not have access to the field", 'b', tc.publicCharField);

        try {
            mayThrowException();
        } catch(Exception ex) {
            ;
        }
    }

    public void testCtorAssignLongDoublePublicFieldSet() {
        TargetClass tc = new TargetClass(2L);
        assertEquals("should have access to the field", 2L, tc.publicLongField);

        tc = new TargetClass(2D);
        assertEquals("should have access to the field", 2D, tc.publicDoubleField, 0D);

        try {
            mayThrowException();
        } catch(Exception e) {
            ;
        }
    }

	public void testCtorAndAssignLongPublicFieldSet() {
		TargetClass tc = new TargetClass(2L);
        assertEquals("should have access to the field", 2L, tc.publicLongField);

		tc.publicLongField = 3L;
        assertEquals("should not have access to the field", 2L, tc.publicLongField);
	}

	public void testCtorAndAssignNonLongWithExceptionHandling() {
		TargetClass tc = new TargetClass(2);
		assertEquals("should have access to the field", 2, tc.publicIntField);

		tc.publicIntField = 3;
        assertEquals("should not have access to the field", 2, tc.publicIntField);

		try {
			mayThrowException();
		} catch(Exception ex) {
			;
		}
	}

    /**
     * java.lang.VerifyError: (class: test/fieldsetbug/FieldSetTest, method: testCtorAndAssignLongPublicFieldSetThreadSleep signature: ()V) 
     * Inconsistent stack height 0 != 2
     */
	public void testCtorAndAssignLongWithExceptionHandling() {
		TargetClass tc = new TargetClass(2L);
        assertEquals("should have access to the field", 2L, tc.publicLongField);

		tc.publicLongField = 3L;
        assertEquals("should have access to the field", 2L, tc.publicLongField);

		try {
			mayThrowException();
		} catch(Exception e) {
			;
		}
	}
	
	private void mayThrowException() throws Exception {
	}
	
    //-- JUnit
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(FieldSetTest.class);
    }
}
