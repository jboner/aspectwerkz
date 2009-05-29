/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect.impl.asm;

import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ConstructorInfo;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.backport175.reader.bytecode.AnnotationElement;
import org.objectweb.asm.Type;

/**
 * ASM implementation of the ConstructorInfo interface.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AsmConstructorInfo extends AsmMemberInfo implements ConstructorInfo {

    /**
     * A list with the parameter type names.
     */
    private String[] m_parameterTypeNames = null;

    /**
     * A list with the exception type names.
     */
    private String[] m_exceptionTypeNames = null;

    /**
     * A list with the parameter types.
     */
    private ClassInfo[] m_parameterTypes = null;

    /**
     * A list with the exception types.
     */
    private ClassInfo[] m_exceptionTypes = null;

    /**
     * Creates a new method meta data instance.
     *
     * @param method
     * @param declaringType
     * @param loader
     */
    AsmConstructorInfo(final MethodStruct method, final String declaringType, final ClassLoader loader) {
        super(method, declaringType, loader);
        Type[] argTypes = Type.getArgumentTypes(method.desc);
        m_parameterTypeNames = new String[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            m_parameterTypeNames[i] = argTypes[i].getClassName();
        }
        // FIXME: how to do exceptions?
        m_exceptionTypeNames = new String[]{};
    }

    /**
     * Returns the constructor info for the constructor specified.
     *
     * @param constructorDesc
     * @param bytecode
     * @param loader
     * @return the constructor info
     */
    public static ConstructorInfo getConstructorInfo(final String constructorDesc,
                                                     final byte[] bytecode,
                                                     final ClassLoader loader) {
        String className = AsmClassInfo.retrieveClassNameFromBytecode(bytecode);
        AsmClassInfoRepository repository = AsmClassInfoRepository.getRepository(loader);
        ClassInfo classInfo = repository.getClassInfo(className);
        if (classInfo == null) {
            classInfo = AsmClassInfo.getClassInfo(bytecode, loader);
        }
        return classInfo.getConstructor(AsmHelper.calculateConstructorHash(constructorDesc));
    }

    /**
     * Returns the signature for the element.
     *
     * @return the signature for the element
     */
    public String getSignature() {
        return AsmHelper.getConstructorDescriptor(this);
    }

    /**
     * Returns the parameter types.
     *
     * @return the parameter types
     */
    public ClassInfo[] getParameterTypes() {
        if (m_parameterTypes == null) {
            m_parameterTypes = new ClassInfo[m_parameterTypeNames.length];
            for (int i = 0; i < m_parameterTypeNames.length; i++) {
                m_parameterTypes[i] = AsmClassInfo.getClassInfo(
                        m_parameterTypeNames[i],
                        (ClassLoader) m_loaderRef.get()
                );
            }
        }
        return m_parameterTypes;
    }

    /**
     * Returns the exception types.
     *
     * @return the exception types
     */
    public ClassInfo[] getExceptionTypes() {
        if (m_exceptionTypes == null) {
            m_exceptionTypes = new ClassInfo[m_exceptionTypeNames.length];
            for (int i = 0; i < m_exceptionTypeNames.length; i++) {
                m_exceptionTypes[i] = AsmClassInfo.getClassInfo(
                        m_exceptionTypeNames[i],
                        (ClassLoader) m_loaderRef.get()
                );
            }
        }
        return m_exceptionTypes;
    }

    /**
     * Returns the annotations.
     *
     * @return the annotations
     */
    public AnnotationElement.Annotation[] getAnnotations() {
        return getDeclaringType().getAnnotationReader().getConstructorAnnotationElements(m_member.desc);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstructorInfo)) {
            return false;
        }
        ConstructorInfo constructorInfo = (ConstructorInfo) o;
        if (!m_declaringTypeName.equals(constructorInfo.getDeclaringType().getName())) {
            return false;
        }
        if (!m_member.name.equals(constructorInfo.getName())) {
            return false;
        }
        ClassInfo[] parameterTypes = constructorInfo.getParameterTypes();
        if (m_parameterTypeNames.length != parameterTypes.length) {//check on names length for lazyness optim
            return false;
        }
        for (int i = 0; i < m_parameterTypeNames.length; i++) {
            if (!m_parameterTypeNames[i].equals(parameterTypes[i].getName())) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = 29;
        result = (29 * result) + m_declaringTypeName.hashCode();
        result = (29 * result) + m_member.name.hashCode();
        for (int i = 0; i < m_parameterTypeNames.length; i++) {
            result = (29 * result) + m_parameterTypeNames[i].hashCode();
        }
        return result;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(m_declaringTypeName);
        sb.append('.').append(m_member.name);
        sb.append(m_member.desc);
        return sb.toString();
    }
}