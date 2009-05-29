/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.weaver;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Type;
import org.objectweb.asm.Label;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.joinpoint.management.JoinPointType;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.FieldInfo;
import org.codehaus.aspectwerkz.reflect.MemberInfo;
import org.codehaus.aspectwerkz.reflect.impl.asm.AsmClassInfo;
import org.codehaus.aspectwerkz.transform.Context;
import org.codehaus.aspectwerkz.transform.TransformationUtil;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.transform.inlining.compiler.AbstractJoinPointCompiler;
import org.codehaus.aspectwerkz.transform.inlining.ContextImpl;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.aspectwerkz.transform.inlining.EmittedJoinPoint;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;

/**
 * Instruments method SET and GET join points by replacing PUTFIELD and GETFIELD instructions with invocations
 * of the compiled join point.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class FieldSetFieldGetVisitor extends ClassAdapter implements TransformationConstants {

    private final ContextImpl m_ctx;
    private final ClassLoader m_loader;
    private final ClassInfo m_callerClassInfo;

    private Label m_lastLabelForLineNumber = EmittedJoinPoint.NO_LINE_NUMBER;

    /**
     * Creates a new instance.
     *
     * @param cv
     * @param loader
     * @param classInfo
     * @param ctx
     */
    public FieldSetFieldGetVisitor(final ClassVisitor cv,
                                   final ClassLoader loader,
                                   final ClassInfo classInfo,
                                   final Context ctx) {
        super(cv);
        m_loader = loader;
        m_callerClassInfo = classInfo;
        m_ctx = (ContextImpl) ctx;
    }

    /**
     * Visits the caller methods.
     *
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    public MethodVisitor visitMethod(final int access,
                                   final String name,
                                   final String desc,
                                   final String signature,
                                   final String[] exceptions) {

        if (name.startsWith(WRAPPER_METHOD_PREFIX)) {
            return super.visitMethod(access, name, desc, signature,exceptions);
        }

        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return mv == null ? null : new ReplacePutFieldAndGetFieldInstructionCodeAdapter(
                mv,
                m_loader,
                m_callerClassInfo,
                m_ctx.getClassName(),
                name,
                desc
        );
    }

    /**
     * Replaces PUTFIELD and GETFIELD instructions with a call to the compiled JoinPoint instance.
     *
     * @author <a href="mailto:jboner@codehaus.org">Jonas BonÈr </a>
     * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
     */
    public class ReplacePutFieldAndGetFieldInstructionCodeAdapter extends AfterObjectInitializationCodeAdapter {

        private final ClassLoader m_loader;
        private final ClassInfo m_callerClassInfo;
        private final String m_callerClassName;
        private final String m_callerMethodName;
        private final String m_callerMethodDesc;
        private final MemberInfo m_callerMemberInfo;

        /**
         * Creates a new instance.
         *
         * @param ca
         * @param loader
         * @param callerClassInfo
         * @param callerClassName
         * @param callerMethodName
         * @param callerMethodDesc
         */
        public ReplacePutFieldAndGetFieldInstructionCodeAdapter(final MethodVisitor ca,
                                                                final ClassLoader loader,
                                                                final ClassInfo callerClassInfo,
                                                                final String callerClassName,
                                                                final String callerMethodName,
                                                                final String callerMethodDesc) {
            super(ca, callerMethodName);
            m_loader = loader;
            m_callerClassInfo = callerClassInfo;
            m_callerClassName = callerClassName;
            m_callerMethodName = callerMethodName;
            m_callerMethodDesc = callerMethodDesc;

            if (CLINIT_METHOD_NAME.equals(m_callerMethodName)) {
                m_callerMemberInfo = m_callerClassInfo.staticInitializer();
            } else if (INIT_METHOD_NAME.equals(m_callerMethodName)) {
                int hash = AsmHelper.calculateConstructorHash(m_callerMethodDesc);
                m_callerMemberInfo = m_callerClassInfo.getConstructor(hash);
            } else {
                int hash = AsmHelper.calculateMethodHash(m_callerMethodName, m_callerMethodDesc);
                m_callerMemberInfo = m_callerClassInfo.getMethod(hash);
            }
            if (m_callerMemberInfo == null) {
                System.err.println(
                        "AW::WARNING " +
                        "metadata structure could not be build for method ["
                        + m_callerClassInfo.getName().replace('/', '.')
                        + '.' + m_callerMethodName + ':' + m_callerMethodDesc + ']'
                );
            }
        }

        /**
         * Label
         *
         * @param label
         */
        public void visitLabel(Label label) {
            m_lastLabelForLineNumber = label;
            super.visitLabel(label);
        }

        /**
         * Visits PUTFIELD and GETFIELD instructions.
         *
         * @param opcode
         * @param className
         * @param fieldName
         * @param fieldDesc
         */
        public void visitFieldInsn(final int opcode,
                                   final String className,
                                   final String fieldName,
                                   final String fieldDesc) {

            if (className.endsWith(AbstractJoinPointCompiler.JOIN_POINT_CLASS_SUFFIX) ||
                fieldName.startsWith(ASPECTWERKZ_PREFIX) ||
                fieldName.startsWith(SYNTHETIC_MEMBER_PREFIX) || // synthetic field
                fieldName.equals(SERIAL_VERSION_UID_FIELD_NAME) // can have been added by the weaver (not safe)
            ) {
                super.visitFieldInsn(opcode, className, fieldName, fieldDesc);
                return;
            }

            // if within ctor, make sure object initialization has been reached
            if (!m_isObjectInitialized) {
                super.visitFieldInsn(opcode, className, fieldName, fieldDesc);
                return;
            }


            final Type fieldType = Type.getType(fieldDesc);
            final int joinPointHash = AsmHelper.calculateFieldHash(fieldName, fieldDesc);
            final ClassInfo classInfo = AsmClassInfo.getClassInfo(className, m_loader);
            final FieldInfo fieldInfo = getFieldInfo(classInfo, className, fieldName, fieldDesc, joinPointHash);

            if (opcode == PUTFIELD || opcode == PUTSTATIC) {
                handleFieldModification(fieldInfo, opcode, className, fieldName, fieldDesc, joinPointHash);
            } else if (opcode == GETFIELD || opcode == GETSTATIC) {
                handleFieldAccess(fieldInfo, opcode, className, fieldName, fieldDesc, joinPointHash, fieldType);
            } else {
                super.visitFieldInsn(opcode, className, fieldName, fieldDesc);
            }
        }

        /**
         * Handles field access.
         *
         * @param fieldInfo
         * @param opcode
         * @param className
         * @param fieldName
         * @param fieldDesc
         * @param joinPointHash
         * @param fieldType
         */
        private void handleFieldAccess(final FieldInfo fieldInfo,
                                       final int opcode,
                                       final String className,
                                       final String fieldName,
                                       final String fieldDesc,
                                       int joinPointHash,
                                       final Type fieldType) {
            if (m_callerMemberInfo == null) {
                super.visitFieldInsn(opcode, className, fieldName, fieldDesc);
                return;
            }

            ExpressionContext ctx = new ExpressionContext(PointcutType.GET, fieldInfo, m_callerMemberInfo);

            if (fieldFilter(m_ctx.getDefinitions(), ctx, fieldInfo)) {
                super.visitFieldInsn(opcode, className, fieldName, fieldDesc);
            } else {
                m_ctx.markAsAdvised();

                String joinPointClassName = TransformationUtil.getJoinPointClassName(
                        m_callerClassName,
                        m_callerMethodName,
                        m_callerMethodDesc,
                        className,
                        JoinPointType.FIELD_GET_INT,
                        joinPointHash
                );

                // no param to field, so pass a default value to the invoke method
                AsmHelper.addDefaultValue(this, fieldType);

                // if static context load NULL else 'this'
                if (Modifier.isStatic(m_callerMemberInfo.getModifiers())) {
                    visitInsn(ACONST_NULL);
                } else {
                    visitVarInsn(ALOAD, 0);
                }

                // add the call to the join point
                super.visitMethodInsn(
                        INVOKESTATIC,
                        joinPointClassName,
                        INVOKE_METHOD_NAME,
                        TransformationUtil.getInvokeSignatureForFieldJoinPoints(
                                fieldInfo.getModifiers(), fieldDesc, m_callerClassName, className
                        )
                );

                // emit the joinpoint
                m_ctx.addEmittedJoinPoint(
                        new EmittedJoinPoint(
                                JoinPointType.FIELD_GET_INT,
                                m_callerClassName,
                                m_callerMethodName,
                                m_callerMethodDesc,
                                m_callerMemberInfo.getModifiers(),
                                className,
                                fieldName,
                                fieldDesc,
                                fieldInfo.getModifiers(),
                                joinPointHash,
                                joinPointClassName,
                                m_lastLabelForLineNumber
                        )
                );
            }
        }

        /**
         * Handles field modification.
         *
         * @param fieldInfo
         * @param opcode
         * @param className
         * @param fieldName
         * @param fieldDesc
         * @param joinPointHash
         */
        private void handleFieldModification(final FieldInfo fieldInfo,
                                             final int opcode,
                                             final String className,
                                             final String fieldName,
                                             final String fieldDesc,
                                             final int joinPointHash) {
            if (m_callerMemberInfo == null) {
                super.visitFieldInsn(opcode, className, fieldName, fieldDesc);
                return;
            }

            ExpressionContext ctx = new ExpressionContext(PointcutType.SET, fieldInfo, m_callerMemberInfo);

            if (fieldFilter(m_ctx.getDefinitions(), ctx, fieldInfo)) {
                super.visitFieldInsn(opcode, className, fieldName, fieldDesc);
            } else {
                m_ctx.markAsAdvised();

                String joinPointClassName = TransformationUtil.getJoinPointClassName(
                        m_callerClassName,
                        m_callerMethodName,
                        m_callerMethodDesc,
                        className,
                        JoinPointType.FIELD_SET_INT,
                        joinPointHash
                );

                // load the caller instance (this), or null if in a static context
                // note that callee instance [optional] and args are already on the stack
                if (Modifier.isStatic(m_callerMemberInfo.getModifiers())) {
                    visitInsn(ACONST_NULL);
                } else {
                    visitVarInsn(ALOAD, 0);
                }

                // add the call to the join point
                super.visitMethodInsn(
                        INVOKESTATIC,
                        joinPointClassName,
                        INVOKE_METHOD_NAME,
                        TransformationUtil.getInvokeSignatureForFieldJoinPoints(
                                fieldInfo.getModifiers(), fieldDesc, m_callerClassName, className
                        )
                );

                final int sort = Type.getType(fieldDesc).getSort();
                if (sort != Type.LONG && sort != Type.DOUBLE) {
                    super.visitInsn(POP);
                } else {
                    //AW-437
                    super.visitInsn(POP2);
                }

                // emit the joinpoint
                m_ctx.addEmittedJoinPoint(
                        new EmittedJoinPoint(
                                JoinPointType.FIELD_SET_INT,
                                m_callerClassName,
                                m_callerMethodName,
                                m_callerMethodDesc,
                                m_callerMemberInfo.getModifiers(),
                                className,
                                fieldName,
                                fieldDesc,
                                fieldInfo.getModifiers(),
                                joinPointHash,
                                joinPointClassName,
                                m_lastLabelForLineNumber
                        )
                );
            }
        }

        /**
         * Returns the field info.
         *
         * @param classInfo
         * @param className
         * @param fieldName
         * @param fieldDesc
         * @param joinPointHash
         * @return
         */
        private FieldInfo getFieldInfo(final ClassInfo classInfo,
                                       final String className,
                                       final String fieldName,
                                       final String fieldDesc,
                                       final int joinPointHash) {
            FieldInfo fieldInfo = classInfo.getField(joinPointHash);
            if (fieldInfo == null) {
                throw new Error(
                        "field info metadata structure could not be build for field: "
                        + className
                        + '.'
                        + fieldName
                        + ':'
                        + fieldDesc
                );
            }
            return fieldInfo;
        }

        /**
         * Filters out the fields that are not eligible for transformation.
         *
         * @param definitions
         * @param ctx
         * @param fieldInfo
         * @return boolean true if the field should be filtered out
         */
        public boolean fieldFilter(final Set definitions,
                                   final ExpressionContext ctx,
                                   final FieldInfo fieldInfo) {
            if (fieldInfo.getName().startsWith(ORIGINAL_METHOD_PREFIX)) {
                return true;
            }
            for (Iterator it = definitions.iterator(); it.hasNext();) {
                if (((SystemDefinition) it.next()).hasPointcut(ctx)) {
                    return false;
                } else {
                    continue;
                }
            }
            return true;
        }
    }
}