/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.aopalliance;

import org.codehaus.aspectwerkz.definition.AspectDefinition;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel;
import org.codehaus.aspectwerkz.transform.inlining.AdviceMethodInfo;
import org.codehaus.aspectwerkz.transform.inlining.AspectInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilationInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilerInput;
import org.codehaus.aspectwerkz.transform.inlining.compiler.AspectWerkzAspectModel;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.transform.JoinPointCompiler;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.ConstructorInterceptor;

import org.codehaus.aspectwerkz.org.objectweb.asm.MethodVisitor;
import org.codehaus.aspectwerkz.org.objectweb.asm.ClassWriter;
import org.codehaus.aspectwerkz.org.objectweb.asm.ClassVisitor;
import org.codehaus.aspectwerkz.org.objectweb.asm.Type;

/**
 * TODO support ConstructorInvocation (1h work) (plus tests)
 * <p/>
 * Implementation of the AspectModel interface for AOP Alliance based frameworks (e.g. Spring, dynaop etc.).
 * <p/>
 * Provides methods for definition of aspects and framework specific bytecode generation
 * used by the join point compiler.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AopAllianceAspectModel implements AspectModel, TransformationConstants {

    protected static final String ASPECT_MODEL_TYPE = "aop-alliance";
    protected static final String AOP_ALLIANCE_CLOSURE_CLASS_NAME = "org/aopalliance/intercept/MethodInvocation";
    protected static final String AOP_ALLIANCE_CLOSURE_PROCEED_METHOD_NAME = "invoke";
    protected static final String AOP_ALLIANCE_CLOSURE_PROCEED_METHOD_SIGNATURE = "(Lorg/aopalliance/intercept/MethodInvocation;)Ljava/lang/Object;";
    protected static final String GET_METHOD_METHOD_NAME = "getMethod";
    protected static final String GET_METHOD_METHOD_SIGNATURE = "()Ljava/lang/reflect/Method;";
    protected static final String GET_STATIC_PART_METHOD_NAME = "getStaticPart";
    protected static final String GET_STATIC_PART_METHOD_SIGNATURE = "()Ljava/lang/reflect/AccessibleObject;";
    protected static final String GET_PARAMETER_VALUES_METHOD_NAME = "getParameterValues";
    protected static final String GET_ARGUMENTS_METHOD_SIGNATURE = "()[Ljava/lang/Object;";
    protected static final String GET_ARGUMENTS_METHOD_NAME = "getArguments";

    /**
     * Use an AspectWerkzAspectModel to delegate common things to it.
     */
    private final static AspectModel s_modelHelper = new AspectWerkzAspectModel();

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
                anInterface.getName().equals(ConstructorInterceptor.class.getName())) {
                aspectDef.setAspectModel(ASPECT_MODEL_TYPE);
                aspectDef.setContainerClassName(null);//no container. Inlined factory is enough
                return;
            }
        }
    }

    /**
     * AOP Alliance is a reflection based model and therefore in need of RTTI info: returns true.
     *
     * @return true
     */
    public boolean requiresReflectiveInfo() {
        return true;
    }

    /**
     * Returns info about the closure class, name and type (interface or class).
     *
     * @return the closure class info
     */
    public AroundClosureClassInfo getAroundClosureClassInfo() {
        return new AspectModel.AroundClosureClassInfo(null, new String[] {AOP_ALLIANCE_CLOSURE_CLASS_NAME});
    }

    /**
     * Returns an instance for a given compilation. Singleton is enough.
     *
     * @param compilationModel
     * @return
     */
    public AspectModel getInstance(CompilationInfo.Model compilationModel) {
        return this;
    }

    /**
     * Creates the methods required to implement or extend to implement the closure for the specific
     * aspect model type.
     *
     * @param cw
     * @param compiler
     */
    public void createMandatoryMethods(final ClassWriter cw, final JoinPointCompiler compiler) {
        final String className = compiler.getJoinPointClassName();
        MethodVisitor cv;

        // invoke
        {
            cv = cw.visitMethod(
                    ACC_PUBLIC,
                    AOP_ALLIANCE_CLOSURE_PROCEED_METHOD_NAME,
                    AOP_ALLIANCE_CLOSURE_PROCEED_METHOD_SIGNATURE,
                    null,
                    new String[]{THROWABLE_CLASS_NAME}
            );
            cv.visitVarInsn(ALOAD, 0);
            cv.visitMethodInsn(INVOKEVIRTUAL, className, PROCEED_METHOD_NAME, PROCEED_METHOD_SIGNATURE);
            cv.visitInsn(ARETURN);
            cv.visitMaxs(0, 0);
        }

        // getStaticPart
        {
            cv = cw.visitMethod(
                    ACC_PUBLIC,
                    GET_STATIC_PART_METHOD_NAME,
                    GET_STATIC_PART_METHOD_SIGNATURE,
                    null, null
            );
            cv.visitFieldInsn(GETSTATIC, className, SIGNATURE_FIELD_NAME, METHOD_SIGNATURE_IMPL_CLASS_SIGNATURE);
            cv.visitTypeInsn(CHECKCAST, METHOD_SIGNATURE_IMPL_CLASS_NAME);
            cv.visitMethodInsn(
                    INVOKEVIRTUAL, METHOD_SIGNATURE_IMPL_CLASS_NAME, GET_METHOD_METHOD_NAME,
                    GET_METHOD_METHOD_SIGNATURE
            );
            cv.visitInsn(ARETURN);
            cv.visitMaxs(1, 1);
        }

        // getMethod
        {
            cv =
            cw.visitMethod(
                    ACC_PUBLIC,
                    GET_METHOD_METHOD_NAME,
                    GET_METHOD_METHOD_SIGNATURE,
                    null, null
            );
            cv.visitFieldInsn(GETSTATIC, className, SIGNATURE_FIELD_NAME, METHOD_SIGNATURE_IMPL_CLASS_SIGNATURE);
            cv.visitTypeInsn(CHECKCAST, METHOD_SIGNATURE_IMPL_CLASS_NAME);
            cv.visitMethodInsn(
                    INVOKEVIRTUAL, METHOD_SIGNATURE_IMPL_CLASS_NAME, GET_METHOD_METHOD_NAME,
                    GET_METHOD_METHOD_SIGNATURE
            );
            cv.visitInsn(ARETURN);
            cv.visitMaxs(1, 1);
        }

        // getArguments
        {
            cv = cw.visitMethod(
                    ACC_PUBLIC,
                    GET_ARGUMENTS_METHOD_NAME,
                    GET_ARGUMENTS_METHOD_SIGNATURE,
                    null, null
            );
            cv.visitVarInsn(ALOAD, 0);
            cv.visitMethodInsn(INVOKESPECIAL, className, GET_RTTI_METHOD_NAME, GET_RTTI_METHOD_SIGNATURE);
            cv.visitTypeInsn(CHECKCAST, METHOD_RTTI_IMPL_CLASS_NAME);
            cv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    METHOD_RTTI_IMPL_CLASS_NAME,
                    GET_PARAMETER_VALUES_METHOD_NAME,
                    GET_ARGUMENTS_METHOD_SIGNATURE
            );
            cv.visitInsn(ARETURN);
            cv.visitMaxs(1, 1);
        }

    }

    /**
     * Creates an invocation of the around closure class' constructor.
     *
     * @param cv
     */
    public void createInvocationOfAroundClosureSuperClass(final MethodVisitor cv) {
        ;// just an interface
    }

    /**
     * Creates host of the aop alliance aspect instance by invoking aspectOf().
     *
     */
    public void createAndStoreStaticAspectInstantiation(ClassVisitor classVisitor, MethodVisitor methodVisitor, AspectInfo aspectInfo, String joinPointClassName) {
        // we use static field and handle instantiation thru perJVM factory
        s_modelHelper.createAndStoreStaticAspectInstantiation(
                classVisitor, methodVisitor, aspectInfo, joinPointClassName
        );
    }

    public void createAndStoreRuntimeAspectInstantiation(MethodVisitor methodVisitor, CompilerInput compilerInput, AspectInfo aspectInfo) {
        ;// does not happen
    }

    public void loadAspect(MethodVisitor methodVisitor, CompilerInput compilerInput, AspectInfo aspectInfo) {
        s_modelHelper.loadAspect(methodVisitor, compilerInput, aspectInfo);
    }

    public void createAroundAdviceArgumentHandling(MethodVisitor methodVisitor, CompilerInput compilerInput, Type[] types, AdviceMethodInfo adviceMethodInfo) {
        // push jp ie AOP Alliance MethodInvocation
        methodVisitor.visitVarInsn(ALOAD, 0);

    }

    public void createBeforeOrAfterAdviceArgumentHandling(MethodVisitor methodVisitor, CompilerInput compilerInput, Type[] types, AdviceMethodInfo adviceMethodInfo, int i) {
        ;//does not happen
    }
}
