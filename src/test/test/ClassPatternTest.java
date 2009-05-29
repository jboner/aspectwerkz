/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.expression.SubtypePatternType;
import org.codehaus.aspectwerkz.expression.regexp.Pattern;
import org.codehaus.aspectwerkz.expression.regexp.TypePattern;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ClassPatternTest extends TestCase {
    public void testMatchMethodName1() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "foo.bar.SomeClass",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertFalse(classPattern.matches("SomeClass"));
        assertTrue(classPattern.matches("foo.bar.SomeClass"));
        assertFalse(classPattern.matches("Class"));
        assertFalse(classPattern.matches(""));
    }

    public void testMatchMethodName2() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "foo.bar.*",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.bar.SomeClass"));
        assertTrue(classPattern.matches("foo.bar.SomeOtherClass"));
        assertFalse(classPattern.matches("SomeClass"));
        assertFalse(classPattern.matches(""));
    }

    public void testMatchMethodName3() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "foo.*.bar.SomeClass",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.hey.bar.SomeClass"));
        assertTrue(classPattern.matches("foo.there.bar.SomeClass"));
        assertFalse(classPattern.matches("SomeClass"));
        assertFalse(classPattern.matches(""));
    }

    public void testMatchMethodName4() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "foo.ba*.*",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.bag.SomeClass"));
        assertTrue(classPattern.matches("foo.bar.SomeClass"));
        assertTrue(classPattern.matches("foo.ba.SomeClass"));
        assertFalse(classPattern.matches("foo.bear.SomeClass"));
        assertFalse(classPattern.matches("foo"));
    }

    public void testMatchClassName5() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "foo..",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.hey.bar.SomeClass"));
        assertTrue(classPattern.matches("foo.SomeClass"));
        assertTrue(classPattern.matches("foo.bar.SomeClass"));
        assertFalse(classPattern.matches("foo"));
    }

    public void testMatchClassName6() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "*",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.hey.bar.SomeClass"));
        assertTrue(classPattern.matches("foo.SomeClass"));
        assertTrue(classPattern.matches("foo.bar.SomeClass"));
        assertTrue(classPattern.matches("foo"));
    }

    public void testMatchClassName7() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "..",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.hey.bar.SomeClass"));
        assertTrue(classPattern.matches("foo.SomeClass"));
        assertTrue(classPattern.matches("foo.bar.SomeClass"));
        assertTrue(classPattern.matches("foo"));
    }

    public void testMatchClassName8() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "foo.bar..*",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.bar.SomeClass"));
        assertTrue(classPattern.matches("foo.bar.baz.SomeClass"));
        assertTrue(classPattern.matches("foo.bar.baz.buzz.SomeClass"));
    }

    public void testMatchClassName9() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "foo.bar.Baz$Buzz",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.bar.Baz$Buzz"));
        assertFalse(classPattern.matches("foo.bar.Baz"));
    }

    public void testMatchClassName10() {
        TypePattern classPattern = Pattern.compileTypePattern(
                "foo.bar..$Buzz",
                SubtypePatternType.NOT_HIERARCHICAL
        );
        assertTrue(classPattern.matches("foo.bar.Baz$Buzz"));
        assertTrue(classPattern.matches("foo.bar.Baz.Buz$Buzz"));
        assertFalse(classPattern.matches("foo.bar.Baz.Buz$Buz"));
        assertFalse(classPattern.matches("foo.bar.Baz"));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ClassPatternTest.class);
    }
}