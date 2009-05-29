/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.reflection;

import junit.framework.TestCase;

import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ClassInfoHelper;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaClassInfo;


public class ClassInfoHelperTest extends TestCase {

    public void testInterfaceImplements() {
        ClassInfo ci = JavaClassInfo.getClassInfo(ClassInfoHelperTest.Intf2.class);
        assertTrue(ClassInfoHelper.implementsInterface(ci, ClassInfoHelperTest.Intf1.class.getName()));
    }
    
    public void testClassImplements() {
        ClassInfo ci = JavaClassInfo.getClassInfo(ClassInfoHelperTest.ClassImpl.class);
        
        assertTrue(ClassInfoHelper.implementsInterface(ci, ClassInfoHelperTest.Intf2.class.getName()));
        
        assertTrue(ClassInfoHelper.implementsInterface(ci, ClassInfoHelperTest.Intf1.class.getName()));
    }
    
    public void testInterfaceImplementsItself() {
        ClassInfo ci = JavaClassInfo.getClassInfo(ClassInfoHelperTest.Intf2.class);
        
        assertFalse(ClassInfoHelper.implementsInterface(ci, ClassInfoHelperTest.Intf2.class.getName()));
    }
    

    public static class ClassImpl implements Intf2 {
    }
    
    public static interface Intf2 extends Intf1 {
    }
    
    public static interface Intf1 {
    }
    
    // -- JUnit
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ClassInfoHelperTest.class);
    }
}
