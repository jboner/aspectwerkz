/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.aspect.container;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.codehaus.aspectwerkz.aspect.AspectContainer;
import org.codehaus.aspectwerkz.definition.AspectDefinition;

/**
 * Simplest factory for perJVM aspects
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class PerJVMAspectFactoryCompiler extends AbstractAspectFactoryCompiler {

    public PerJVMAspectFactoryCompiler(String uuid, String aspectClassName, String aspectQualifiedName, String containerClassName, String rawParameters, ClassLoader loader) {
        super(uuid, aspectClassName, aspectQualifiedName, containerClassName, rawParameters, loader);
    }

    protected void createAspectOf() {
        m_cw.visitField(
                ACC_PRIVATE + ACC_STATIC,
                FACTORY_SINGLEASPECT_FIELD_NAME,
                m_aspectClassSignature,
                null,
                null
        );

        MethodVisitor cv = m_cw.visitMethod(
                ACC_PUBLIC + ACC_STATIC + ACC_FINAL,
                FACTORY_ASPECTOF_METHOD_NAME,
                "()" + m_aspectClassSignature,
                null,
                null
        );

        cv.visitFieldInsn(GETSTATIC, m_aspectFactoryClassName, FACTORY_SINGLEASPECT_FIELD_NAME, m_aspectClassSignature);
        Label ifNonNull = new Label();
        cv.visitJumpInsn(IFNONNULL, ifNonNull);
        if (m_hasAspectContainer) {
            cv.visitFieldInsn(GETSTATIC, m_aspectFactoryClassName, FACTORY_CONTAINER_FIELD_NAME, ASPECT_CONTAINER_CLASS_SIGNATURE);
            cv.visitMethodInsn(INVOKEINTERFACE, ASPECT_CONTAINER_CLASS_NAME, ASPECT_CONTAINER_ASPECTOF_METHOD_NAME, ASPECT_CONTAINER_ASPECTOF_PERJVM_METHOD_SIGNATURE);
            cv.visitTypeInsn(CHECKCAST, m_aspectClassName);
        } else {
            cv.visitTypeInsn(NEW, m_aspectClassName);
            cv.visitInsn(DUP);
            cv.visitMethodInsn(INVOKESPECIAL, m_aspectClassName, INIT_METHOD_NAME, NO_PARAM_RETURN_VOID_SIGNATURE);
        }
        cv.visitFieldInsn(PUTSTATIC, m_aspectFactoryClassName, FACTORY_SINGLEASPECT_FIELD_NAME, m_aspectClassSignature);
        cv.visitLabel(ifNonNull);
        cv.visitFieldInsn(GETSTATIC, m_aspectFactoryClassName, FACTORY_SINGLEASPECT_FIELD_NAME, m_aspectClassSignature);
        cv.visitInsn(ARETURN);
        cv.visitMaxs(0, 0);
    }

    protected void createHasAspect() {
        MethodVisitor cv = m_cw.visitMethod(
                ACC_STATIC + ACC_PUBLIC + ACC_FINAL,
                FACTORY_HASASPECT_METHOD_NAME,
                NO_PARAM_RETURN_BOOLEAN_SIGNATURE,
                null,
                null
        );

        cv.visitFieldInsn(GETSTATIC, m_aspectFactoryClassName, FACTORY_SINGLEASPECT_FIELD_NAME, m_aspectClassSignature);
        Label ifNonNull = new Label();
        cv.visitJumpInsn(IFNONNULL, ifNonNull);
        cv.visitInsn(ICONST_0);
        cv.visitInsn(IRETURN);
        cv.visitLabel(ifNonNull);
        cv.visitInsn(ICONST_1);
        cv.visitInsn(IRETURN);
        cv.visitMaxs(0, 0);
    }

    protected void createOtherArtifacts() {
        ;
    }
}
