/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.joinpoint.impl;

import org.codehaus.aspectwerkz.joinpoint.FieldSignature;
import org.codehaus.backport175.reader.Annotation;
import org.codehaus.backport175.reader.Annotations;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Implementation for the field signature.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class FieldSignatureImpl implements FieldSignature {
    private final Class m_declaringType;

    private final Field m_field;

    /**
     * @param field
     * @param declaringType
     */
    public FieldSignatureImpl(final Class declaringType, final Field field) {
        m_declaringType = declaringType;
        m_field = field;
        m_field.setAccessible(true);
    }

    /**
     * Returns the declaring class.
     *
     * @return the declaring class
     */
    public Class getDeclaringType() {
        return m_declaringType;
    }

    /**
     * Returns the modifiers for the signature. <p/>Could be used like this:
     * <p/>
     * <pre>
     * boolean isPublic = java.lang.reflect.Modifier.isPublic(signature.getModifiers());
     * </pre>
     *
     * @return the mofifiers
     */
    public int getModifiers() {
        return m_field.getModifiers();
    }

    /**
     * Returns the name (f.e. name of method of field).
     *
     * @return the name
     */
    public String getName() {
        return m_field.getName();
    }

    /**
     * Returns the field.
     *
     * @return the field
     */
    public Field getField() {
        return m_field;
    }

    /**
     * Returns the field type.
     *
     * @return the field type
     */
    public Class getFieldType() {
        return m_field.getType();
    }

    /**
     * Return the annotation with a specific class.
     *
     * @param annotationClass the annotation class
     * @return the annotation or null
     */
    public Annotation getAnnotation(final Class annotationClass) {
        return Annotations.getAnnotation(annotationClass, m_field);
    }

    /**
     * Return all the annotations.
     *
     * @return a list with the annotations
     */
    public Annotation[] getAnnotations() {
        return Annotations.getAnnotations(m_field);
    }

    /**
     * Returns a string representation of the signature.
     *
     * @return a string representation
     */
    public String toString() {
        return m_field.toString();
    }
}