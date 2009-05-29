/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect;

import java.util.Collections;
import java.util.List;

import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.backport175.reader.Annotation;
import org.codehaus.backport175.reader.bytecode.AnnotationElement;
import org.objectweb.asm.Opcodes;


/**
 * Sole implementation of <CODE>StaticInitializationInfo</CODE>.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 */
public class StaticInitializationInfoImpl implements StaticInitializationInfo {
	protected ClassInfo m_declaringType;
	
	public StaticInitializationInfoImpl(final ClassInfo classInfo) {
		m_declaringType = classInfo;
	}
	
	/**
	 * @see org.codehaus.aspectwerkz.reflect.MemberInfo#getDeclaringType()
	 */
	public ClassInfo getDeclaringType() {
		return m_declaringType;
	}

	/**
	 * @see org.codehaus.aspectwerkz.reflect.ReflectionInfo#getName()
	 */
	public String getName() {
		return TransformationConstants.CLINIT_METHOD_NAME;
	}

	/**
	 * @see org.codehaus.aspectwerkz.reflect.ReflectionInfo#getSignature()
	 */
	public String getSignature() {
		return TransformationConstants.CLINIT_METHOD_SIGNATURE;
	}

	/**
	 * @see org.codehaus.aspectwerkz.reflect.ReflectionInfo#getModifiers()
	 */
	public int getModifiers() {
		return Opcodes.ACC_STATIC;
	}

	/**
	 * @see org.codehaus.aspectwerkz.reflect.ReflectionInfo#getAnnotations()
	 */
	public AnnotationElement.Annotation[] getAnnotations() {
		return ClassInfo.EMPTY_ANNOTATION_ARRAY;
	}

}
