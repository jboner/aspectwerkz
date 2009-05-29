/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect;

import org.codehaus.backport175.reader.Annotation;
import org.codehaus.backport175.reader.bytecode.AnnotationReader;
import org.codehaus.backport175.reader.bytecode.AnnotationElement;

import java.util.List;
import java.util.ArrayList;

/**
 * Interface for the class info implementations.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface ClassInfo extends ReflectionInfo {

    final static AnnotationElement.Annotation[] EMPTY_ANNOTATION_ARRAY = new AnnotationElement.Annotation[0];

    /**
     * Returns a constructor info by its hash.
     * Looks up in the hierarchy
     *
     * @param hash
     * @return
     */
    ConstructorInfo getConstructor(int hash);

    /**
     * Returns the constructors info.
     * Does not looks up in the hierarchy
     *
     * @return the constructors info
     */
    ConstructorInfo[] getConstructors();

    /**
     * Returns a method info by its hash.
     * Looks up in the hierarchy
     *
     * @param hash
     * @return
     */
    MethodInfo getMethod(int hash);

    /**
     * Returns the methods info.
     * Does not looks up in the hierarchy
     *
     * @return the methods info
     */
    MethodInfo[] getMethods();

    /**
     * Returns a field info by its hash.
     * Looks up in the hierarchy
     *
     * @param hash
     * @return
     */
    FieldInfo getField(int hash);

    /**
     * Returns the fields info.
     * Does not looks up in the hierarchy
     *
     * @return the fields info
     */
    FieldInfo[] getFields();

    /**
     * Returns the class loader that loaded this class.
     *
     * @return the class loader
     */
    ClassLoader getClassLoader();

    /**
     * Checks if the class has a static initalizer.
     *
     * @return
     */
    boolean hasStaticInitializer();

    /**
     * Returns the static initializer info of the current underlying class if any.
     * 
     * @return
     */
    StaticInitializationInfo staticInitializer();
    
    /**
     * Returns the interfaces.
     *
     * @return the interfaces
     */
    ClassInfo[] getInterfaces();

    /**
     * Returns the super class, or null (superclass of java.lang.Object)
     *
     * @return the super class
     */
    ClassInfo getSuperclass();

    /**
     * Returns the component type if array type else null.
     *
     * @return the component type
     */
    ClassInfo getComponentType();

    /**
     * Is the class an interface.
     *
     * @return
     */
    boolean isInterface();

    /**
     * Is the class a primitive type.
     *
     * @return
     */
    boolean isPrimitive();

    /**
     * Is the class an array type.
     *
     * @return
     */
    boolean isArray();

    /**
     *
     * @return
     */
    AnnotationReader getAnnotationReader();

    public static class NullClassInfo implements ClassInfo {

        public ConstructorInfo getConstructor(int hash) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public ConstructorInfo[] getConstructors() {
            return new ConstructorInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        public MethodInfo getMethod(int hash) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public MethodInfo[] getMethods() {
            return new MethodInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        public FieldInfo getField(int hash) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public FieldInfo[] getFields() {
            return new FieldInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean hasStaticInitializer() {
            return false;
        }

		/**
		 * @see org.codehaus.aspectwerkz.reflect.ClassInfo#staticInitializer()
		 */
		public StaticInitializationInfo staticInitializer() {
			return null;
		}

        public ClassInfo[] getInterfaces() {
            return new ClassInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        public ClassInfo getSuperclass() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public ClassLoader getClassLoader() {
            return null;
        }

        public ClassInfo getComponentType() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean isInterface() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean isPrimitive() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean isArray() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getName() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getSignature() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public int getModifiers() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public AnnotationElement.Annotation[] getAnnotations() {
            return EMPTY_ANNOTATION_ARRAY;
        }

        public AnnotationReader getAnnotationReader() {
            return null;
        }
    }
}