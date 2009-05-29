/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.spring;

import org.codehaus.aspectwerkz.transform.aopalliance.AopAllianceAspectModel;
import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel;
import org.codehaus.aspectwerkz.transform.inlining.AdviceMethodInfo;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilerInput;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.definition.AspectDefinition;

import org.aopalliance.intercept.MethodInterceptor;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

import org.codehaus.aspectwerkz.org.objectweb.asm.MethodVisitor;
import org.codehaus.aspectwerkz.org.objectweb.asm.Type;
import org.codehaus.aspectwerkz.aspect.AdviceType;

/**
 * Implementation of the AspectModel interface for Spring framework.
 * <p/>
 * Provides methods for definition of aspects and framework specific bytecode generation
 * used by the join point compiler.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class SpringAspectModel extends AopAllianceAspectModel {

    protected static final String ASPECT_MODEL_TYPE = "spring";

    /**
     * Returns the aspect model type, which is an id for the the special aspect model, can be anything as long
     * as it is unique.
     *
     * @return the aspect model type id
     */
    public String getAspectModelType() {
        return ASPECT_MODEL_TYPE;
    }

    /**
     * Defines the aspect.
     *
     * @param classInfo
     * @param aspectDef
     * @param loader
     */
    public void defineAspect(final ClassInfo classInfo,
                             final AspectDefinition aspectDef,
                             final ClassLoader loader) {
        ClassInfo[] interfaces = classInfo.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            ClassInfo anInterface = interfaces[i];
            if (anInterface.getName().equals(MethodInterceptor.class.getName()) ||
                anInterface.getName().equals(MethodBeforeAdvice.class.getName()) ||
                anInterface.getName().equals(AfterReturningAdvice.class.getName()) ||
                anInterface.getName().equals(ThrowsAdvice.class.getName())) {
                aspectDef.setAspectModel(ASPECT_MODEL_TYPE);
                aspectDef.setContainerClassName(null);
                return;
            }
        }
    }

    /**
     * Returns info about the closure class, name and type (interface or class).
     *
     * @return the closure class info
     */
    public AroundClosureClassInfo getAroundClosureClassInfo() {
        return new AspectModel.AroundClosureClassInfo(
                null,
                new String[]{
                    AOP_ALLIANCE_CLOSURE_CLASS_NAME,
                    MethodBeforeAdvice.class.getName().replace('.', '/'),
                    AfterReturningAdvice.class.getName().replace('.', '/')
                }
        );
    }

    public void createBeforeOrAfterAdviceArgumentHandling(MethodVisitor methodVisitor, CompilerInput compilerInput, Type[] types, AdviceMethodInfo adviceMethodInfo, int i) {
        if (AdviceType.BEFORE.equals(adviceMethodInfo.getAdviceInfo().getType())) {
            createBeforeAdviceArgumentHandling(
                    methodVisitor,
                    adviceMethodInfo,
                    compilerInput.joinPointInstanceIndex
            );
        } else {
            // after advice no matter what
            createAfterAdviceArgumentHandling(
                    methodVisitor,
                    adviceMethodInfo,
                    compilerInput.joinPointInstanceIndex
            );
        }
    }

    /**
     * Handles the arguments to the before advice.
     *
     * @param cv
     * @param adviceMethodInfo
     * @param joinPointInstanceIndex
     */
    public void createBeforeAdviceArgumentHandling(final MethodVisitor cv, final AdviceMethodInfo adviceMethodInfo, final int joinPointInstanceIndex) {
        final String joinPointClassName = adviceMethodInfo.getJoinPointClassName();
        final int joinPointIndex = joinPointInstanceIndex;
        cv.visitFieldInsn(
                GETSTATIC,
                joinPointClassName,
                SIGNATURE_FIELD_NAME,
                METHOD_SIGNATURE_IMPL_CLASS_SIGNATURE
        );
        cv.visitMethodInsn(
                INVOKEVIRTUAL,
                METHOD_SIGNATURE_IMPL_CLASS_NAME,
                GET_METHOD_METHOD_NAME,
                GET_METHOD_METHOD_SIGNATURE
        );

        if (Type.getArgumentTypes(adviceMethodInfo.getCalleeMemberDesc()).length == 0) {
            cv.visitInsn(ACONST_NULL);
        } else {
            cv.visitVarInsn(ALOAD, joinPointIndex);
            cv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    joinPointClassName,
                    GET_RTTI_METHOD_NAME,
                    GET_RTTI_METHOD_SIGNATURE
            );
            cv.visitTypeInsn(CHECKCAST, METHOD_RTTI_IMPL_CLASS_NAME);
            cv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    METHOD_RTTI_IMPL_CLASS_NAME,
                    GET_PARAMETER_VALUES_METHOD_NAME,
                    GET_ARGUMENTS_METHOD_SIGNATURE
            );
        }
        cv.visitVarInsn(ALOAD, joinPointIndex);
        cv.visitFieldInsn(
                GETFIELD,
                joinPointClassName,
                CALLEE_INSTANCE_FIELD_NAME,
                adviceMethodInfo.getCalleeClassSignature()
        );
    }

    /**
     * Handles the arguments to the after advice.
     *
     * @param cv
     * @param adviceMethodInfo
     * @param joinPointInstanceIndex
     */
    public void createAfterAdviceArgumentHandling(final MethodVisitor cv, final AdviceMethodInfo adviceMethodInfo, final int joinPointInstanceIndex) {
        final String joinPointClassName = adviceMethodInfo.getJoinPointClassName();
        final int joinPointIndex = joinPointInstanceIndex;
        final String specArgDesc = adviceMethodInfo.getSpecialArgumentTypeDesc();
        if (specArgDesc == null) {
            cv.visitInsn(ACONST_NULL);
        } else {
            cv.visitVarInsn(ALOAD, adviceMethodInfo.getSpecialArgumentIndex());
            AsmHelper.wrapPrimitiveType(cv, Type.getType(specArgDesc));
        }
        cv.visitFieldInsn(
                GETSTATIC,
                joinPointClassName,
                SIGNATURE_FIELD_NAME,
                METHOD_SIGNATURE_IMPL_CLASS_SIGNATURE
        );
        cv.visitMethodInsn(
                INVOKEVIRTUAL,
                METHOD_SIGNATURE_IMPL_CLASS_NAME,
                GET_METHOD_METHOD_NAME,
                GET_METHOD_METHOD_SIGNATURE
        );

        if (Type.getArgumentTypes(adviceMethodInfo.getCalleeMemberDesc()).length == 0) {
            cv.visitInsn(ACONST_NULL);
        } else {
            cv.visitVarInsn(ALOAD, joinPointIndex);
            cv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    joinPointClassName,
                    GET_RTTI_METHOD_NAME,
                    GET_RTTI_METHOD_SIGNATURE
            );
            cv.visitTypeInsn(CHECKCAST, METHOD_RTTI_IMPL_CLASS_NAME);
            cv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    METHOD_RTTI_IMPL_CLASS_NAME,
                    GET_PARAMETER_VALUES_METHOD_NAME,
                    GET_ARGUMENTS_METHOD_SIGNATURE
            );
        }

        cv.visitVarInsn(ALOAD, joinPointIndex);
        cv.visitFieldInsn(
                GETFIELD,
                joinPointClassName,
                CALLEE_INSTANCE_FIELD_NAME,
                adviceMethodInfo.getCalleeClassSignature()
        );
    }
}
