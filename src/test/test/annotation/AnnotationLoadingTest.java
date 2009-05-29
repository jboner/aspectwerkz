/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.annotation;

import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.reflect.impl.asm.AsmClassInfo;
import org.codehaus.aspectwerkz.annotation.AnnotationInfo;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AnnotationLoadingTest extends TestCase {

    public void testAnnotationLoadingWithClassRef() throws Throwable {
        System.out.println("START");
        ClassInfo klass = AsmClassInfo.getClassInfo("test.annotation.AnnotationLoadingTarget", AnnotationLoadingTest.class.getClassLoader());
        MethodInfo m = klass.getMethods()[0];
        //FIXME - change classInfo to return Annotation DP and not elements
//        AnnotationInfo annInfo = (AnnotationInfo) m.getAnnotations().get(0);
//
//        System.out.println("DONE");
//        System.out.println(annInfo.getAnnotation());
//        AnnotationParserTest.Complex c = ((AnnotationParserTest.Complex)annInfo.getAnnotation());
//        System.out.println(c.klass().getName());
//        System.out.println(c.toString());
//        System.out.println(c.klass2()[0]);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AnnotationLoadingTest.class);
    }

}
