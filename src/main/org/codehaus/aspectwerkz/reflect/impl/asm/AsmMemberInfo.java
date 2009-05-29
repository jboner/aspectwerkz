/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect.impl.asm;

import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.MemberInfo;
import org.codehaus.backport175.reader.Annotation;
import org.codehaus.backport175.reader.bytecode.AnnotationElement;
import org.codehaus.backport175.reader.bytecode.AnnotationReader;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * ASM implementation of the MemberInfo interface.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public abstract class AsmMemberInfo implements MemberInfo {

    /**
     * The member info.
     */
    protected final MemberStruct m_member;

    /**
     * The class loader wrapped in a weak ref.
     */
    protected final WeakReference m_loaderRef;

    /**
     * The declaring type name.
     */
    protected final String m_declaringTypeName;

    /**
     * The declaring type.
     */
    protected ClassInfo m_declaringType;

    /**
     * The class info repository.
     */
    protected final AsmClassInfoRepository m_classInfoRepository;

    /**
     * Creates a new member meta data instance.
     *
     * @param member
     * @param declaringType
     * @param loader
     */
    AsmMemberInfo(final MemberStruct member, final String declaringType, final ClassLoader loader) {
        if (member == null) {
            throw new IllegalArgumentException("member can not be null");
        }
        if (declaringType == null) {
            throw new IllegalArgumentException("declaring type can not be null");
        }
        m_member = member;
        m_loaderRef = new WeakReference(loader);
        m_declaringTypeName = declaringType.replace('/', '.');
        m_classInfoRepository = AsmClassInfoRepository.getRepository(loader);
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    public String getName() {
        return m_member.name;
    }

    /**
     * Returns the modifiers.
     *
     * @return the modifiers
     */
    public int getModifiers() {
        return m_member.modifiers;
    }

    /**
     * Returns the declaring type.
     *
     * @return the declaring type
     */
    public ClassInfo getDeclaringType() {
        if (m_declaringType == null) {
            m_declaringType = m_classInfoRepository.getClassInfo(m_declaringTypeName);
        }
        return m_declaringType;
    }
}