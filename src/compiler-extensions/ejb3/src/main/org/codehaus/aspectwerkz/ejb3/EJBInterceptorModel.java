/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.ejb3;

import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel;
import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel.AroundClosureClassInfo;
import org.codehaus.aspectwerkz.transform.inlining.AspectInfo;
import org.codehaus.aspectwerkz.transform.inlining.AdviceMethodInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilationInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilerInput;
import org.codehaus.aspectwerkz.transform.JoinPointCompiler;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.definition.AspectDefinition;
import org.codehaus.aspectwerkz.joinpoint.MethodSignature;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class EJBInterceptorModel implements AspectModel, Opcodes, TransformationConstants {

    public final static String TYPE = EJBInterceptorModel.class.getName();
    
    private final static AroundClosureClassInfo EJB3_CLOSURE = new AroundClosureClassInfo(
            "java.lang.Object",
            new String[]{"javax.ejb.InvocationContext"}
    );

    /**
     * Unique model ID
     * @return
     */
    public String getAspectModelType() {
        return TYPE;
    }

    /**
     * Singleton instance is enough
     * 
     * @param compilationModel
     * @return
     */ 
    public AspectModel getInstance(CompilationInfo.Model compilationModel) {
        return this;
    }

    /**
     * Called at definition time.
     *
     * @param aspectClassInfo
     * @param aspectDef
     * @param loader
     */
    public void defineAspect(ClassInfo aspectClassInfo, AspectDefinition aspectDef, ClassLoader loader) {
        //TODO if we want to reuse the interceptor in some aop.xml
    }

    public AroundClosureClassInfo getAroundClosureClassInfo() {
        return EJB3_CLOSURE;
    }

    public void createInvocationOfAroundClosureSuperClass(MethodVisitor cv) {
        ;//not needed, InvocationContext is an interface
    }

    public void createAndStoreStaticAspectInstantiation(ClassVisitor cw, MethodVisitor cv, AspectInfo aspectInfo, String joinPointClassName) {
        // the spec does not define the interceptor life cycle
        // this impl makes em one per EJB CLASS
        cw.visitField(
                ACC_PRIVATE + ACC_STATIC,
                aspectInfo.getAspectFieldName(),
                aspectInfo.getAspectClassSignature(),
                null,
                null
        );

        // new it and store it as part of the static initialization
        cv.visitTypeInsn(NEW, aspectInfo.getAspectClassName());
        cv.visitInsn(DUP);
        cv.visitMethodInsn(INVOKESPECIAL, aspectInfo.getAspectClassName(), INIT_METHOD_NAME, NO_PARAM_RETURN_VOID_SIGNATURE);
        cv.visitFieldInsn(PUTSTATIC, joinPointClassName, aspectInfo.getAspectFieldName(), aspectInfo.getAspectClassSignature());
    }

    public void loadAspect(MethodVisitor cv, CompilerInput input, AspectInfo aspectInfo) {
        // interceptor was stored as a static field
        cv.visitFieldInsn(GETSTATIC, input.joinPointClassName, aspectInfo.getAspectFieldName(), aspectInfo.getAspectClassSignature());
    }

    public void createBeforeOrAfterAdviceArgumentHandling(MethodVisitor cv, CompilerInput input, Type[] joinPointArgumentTypes, AdviceMethodInfo adviceMethodInfo, int specialArgIndex) {
        ;// cannot happen so lets skip it
    }

    public void createAroundAdviceArgumentHandling(MethodVisitor cv, CompilerInput input, Type[] joinPointArgumentTypes, AdviceMethodInfo adviceMethodInfo) {
        // interceptor only accept the closure as sole argument ie "this" within proceed()
        cv.visitVarInsn(ALOAD, 0);
    }

    public void createMandatoryMethods(ClassWriter cw, JoinPointCompiler compiler) {
        //getBean
        MethodVisitor getBean = cw.visitMethod(
                ACC_PUBLIC,
                "getBean",
                "()Ljava/lang/Object;",
                null,
                EMPTY_STRING_ARRAY
        );
        getBean.visitVarInsn(ALOAD, 0);
        getBean.visitFieldInsn(
                GETFIELD,
                compiler.getJoinPointClassName(),
                CALLER_INSTANCE_FIELD_NAME,
                compiler.getCallerClassSignature()
        );
        getBean.visitInsn(ARETURN);
        getBean.visitMaxs(0, 0);

        // getParameters
        MethodVisitor getParameters = cw.visitMethod(
                ACC_PUBLIC,
                "getParameters",
                "()[Ljava/lang/Object;",
                null,
                EMPTY_STRING_ARRAY
        );
        compiler.createArgumentArrayAt(getParameters, 1);
        getParameters.visitVarInsn(ALOAD, 1);
        getParameters.visitInsn(ARETURN);
        getParameters.visitMaxs(0, 0);

        // getMethod
        MethodVisitor getMethod = cw.visitMethod(
                ACC_PUBLIC,
                "getMethod",
                "()Ljava/lang/reflect/Method;",
                null,
                EMPTY_STRING_ARRAY
        );
        getMethod.visitFieldInsn(
                GETSTATIC,
                compiler.getJoinPointClassName(),
                "SIGNATURE",
                METHOD_SIGNATURE_IMPL_CLASS_SIGNATURE
        );
        getMethod.visitMethodInsn(
                INVOKEINTERFACE,
                Type.getInternalName(MethodSignature.class),
                "getMethod",
                "()Ljava/lang/reflect/Method;"
        );
        getMethod.visitInsn(ARETURN);
        getMethod.visitMaxs(0, 0);

        // TODO supposely setParameters, getEJBContext etc
    }

    public void createAndStoreRuntimeAspectInstantiation(MethodVisitor cv, CompilerInput input, AspectInfo aspectInfo) {
        ;// nothing needed
    }

    public boolean requiresReflectiveInfo() {
        return true;// we want a new InvocationContext at each join point invocation
    }
}
