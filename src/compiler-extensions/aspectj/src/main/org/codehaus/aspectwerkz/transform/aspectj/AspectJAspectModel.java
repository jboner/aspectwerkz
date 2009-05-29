/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.aspectj;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.generic.Type;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.patterns.KindedPointcut;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.SignaturePattern;
import org.codehaus.aspectwerkz.DeploymentModel;
import org.codehaus.aspectwerkz.aspect.AdviceType;
import org.codehaus.aspectwerkz.definition.AdviceDefinition;
import org.codehaus.aspectwerkz.definition.AspectDefinition;
import org.codehaus.aspectwerkz.definition.DefinitionParserHelper;
import org.codehaus.aspectwerkz.exception.DefinitionException;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;
import org.codehaus.aspectwerkz.org.objectweb.asm.ClassVisitor;
import org.codehaus.aspectwerkz.org.objectweb.asm.ClassWriter;
import org.codehaus.aspectwerkz.org.objectweb.asm.MethodVisitor;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.transform.JoinPointCompiler;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.transform.inlining.AdviceMethodInfo;
import org.codehaus.aspectwerkz.transform.inlining.AspectInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.AspectWerkzAspectModel;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilationInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilerInput;
import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the AspectModel interface for the AspectJ framework.
 * <p/>
 * Provides methods for definition of aspects and framework specific bytecode generation
 * used by the join point compiler.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AspectJAspectModel implements AspectModel, TransformationConstants {

    private static final String ASPECT_MODEL_TYPE = "aspectj";
    private static final String ASPECTJ_AROUND_CLOSURE_CLASS_NAME = "org/aspectj/runtime/internal/AroundClosure";
    private static final String ASPECTJ_AROUND_CLOSURE_RUN_METHOD_NAME = "run";
    private static final String ASPECTJ_AROUND_CLOSURE_RUN_METHOD_SIGNATURE = "([Ljava/lang/Object;)Ljava/lang/Object;";
    private static final String PREFIX_AROUND_ADVICE = "ajc$around$";
    private static final String PREFIX_BEFORE_ADVICE = "ajc$before$";
    private static final String PREFIX_AFTER_FINALLY_ADVICE = "ajc$after$";
    private static final String PREFIX_AFTER_THROWING_ADVICE = "ajc$afterThrowing$";
    private static final String PREFIX_AFTER_RETURNING_ADVICE = "ajc$afterReturning$";
    private static final String PROCEED_SUFFIX = "proceed";

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

        JavaClass javaClass = null;
        String classFileName = classInfo.getName().replace('.', '/') + ".class";
        try {
            InputStream classStream = loader.getResourceAsStream(classFileName);
            ClassParser classParser = new ClassParser(classStream, classFileName);
            javaClass = classParser.parse();
        } catch (IOException e) {
            throw new WrappedRuntimeException(e);
        }

        List attributes = getAspectAttributes(javaClass);
        if (attributes.size() == 0) {
            return; // not an AspectJ aspect
        }

        aspectDef.setAspectModel(getAspectModelType());

        for (Iterator it = attributes.iterator(); it.hasNext();) {
            AjAttribute attr = (AjAttribute) it.next();
            if (attr instanceof AjAttribute.PointcutDeclarationAttribute) {
                AjAttribute.PointcutDeclarationAttribute pcAttr = (AjAttribute.PointcutDeclarationAttribute) attr;
                Pointcut pointcut = pcAttr.reify().getPointcut();
                if (pointcut instanceof KindedPointcut) {
                    try {
                        Field sigField = KindedPointcut.class.getDeclaredField("signature");
                        sigField.setAccessible(true);
                        SignaturePattern signature = (SignaturePattern) sigField.get(pointcut);
                        DefinitionParserHelper.createAndAddPointcutDefToAspectDef(signature.getName().toString(), pointcut.toString(), aspectDef);
                    } catch (Exception e) {
                        throw new WrappedRuntimeException(e);
                    }
                }
            }
        }
        for (Iterator iterator = getAroundAdviceInfo(javaClass).iterator(); iterator.hasNext();) {
            AdviceInfo ajAdviceInfo = (AdviceInfo) iterator.next();
            AdviceDefinition adviceDef = handleAdviceInfo(classInfo, aspectDef, ajAdviceInfo);
            aspectDef.addAroundAdviceDefinition(adviceDef);
        }
        for (Iterator iterator = getBeforeAdviceInfo(javaClass).iterator(); iterator.hasNext();) {
            AdviceInfo ajAdviceInfo = (AdviceInfo) iterator.next();
            AdviceDefinition adviceDef = handleAdviceInfo(classInfo, aspectDef, ajAdviceInfo);
            aspectDef.addBeforeAdviceDefinition(adviceDef);
        }
        for (Iterator iterator = getAfterFinallyAdviceInfo(javaClass).iterator(); iterator.hasNext();) {
            AdviceInfo ajAdviceInfo = (AdviceInfo) iterator.next();
            AdviceDefinition adviceDef = handleAdviceInfo(classInfo, aspectDef, ajAdviceInfo);
            aspectDef.addAfterAdviceDefinition(adviceDef);
        }
        for (Iterator iterator = getAfterReturningAdviceInfo(javaClass).iterator(); iterator.hasNext();) {
            AdviceInfo ajAdviceInfo = (AdviceInfo) iterator.next();
            AdviceDefinition adviceDef = handleAdviceInfo(classInfo, aspectDef, ajAdviceInfo);
            aspectDef.addAfterAdviceDefinition(adviceDef);
        }
        for (Iterator iterator = getAfterThrowingAdviceInfo(javaClass).iterator(); iterator.hasNext();) {
            AdviceInfo ajAdviceInfo = (AdviceInfo) iterator.next();
            AdviceDefinition adviceDef = handleAdviceInfo(classInfo, aspectDef, ajAdviceInfo);
            aspectDef.addAfterAdviceDefinition(adviceDef);
        }
    }

    /**
     * Returns the instance to use for the given compilation
     * Singleton is enough.
     *
     * @param model
     * @return
     */
    public AspectModel getInstance(CompilationInfo.Model model) {
        return this;
    }

    /**
     * AspectJ is not in need for reflective information, passes contextual info through args() binding etc.
     * or handles it itself using 'thisJoinPoint'.
     *
     * @return true
     */
    public boolean requiresReflectiveInfo() {
        return false;
    }

    /**
     * Returns info about the closure class, name and type (interface or class).
     *
     * @return the closure class info
     */
    public AroundClosureClassInfo getAroundClosureClassInfo() {
        return new AspectModel.AroundClosureClassInfo(ASPECTJ_AROUND_CLOSURE_CLASS_NAME, new String[]{});
    }

    /**
     * Creates the methods required to implement or extend to implement the closure for the specific aspect model type.
     *
     * @param classWriter
     * @param joinPointCompiler
     */
    public void createMandatoryMethods(ClassWriter classWriter, JoinPointCompiler joinPointCompiler) {
        MethodVisitor cv = classWriter.visitMethod(ACC_PUBLIC, ASPECTJ_AROUND_CLOSURE_RUN_METHOD_NAME,
                ASPECTJ_AROUND_CLOSURE_RUN_METHOD_SIGNATURE,
                null,
                new String[]{THROWABLE_CLASS_NAME});
        cv.visitVarInsn(ALOAD, 0);
        cv.visitMethodInsn(INVOKEVIRTUAL, joinPointCompiler.getJoinPointClassName(), PROCEED_METHOD_NAME, PROCEED_METHOD_SIGNATURE);
        cv.visitInsn(ARETURN);
        cv.visitMaxs(0, 0);
    }

    /**
     * Creates an invocation of the around closure class' constructor.
     *
     * @param mv
     */
    public void createInvocationOfAroundClosureSuperClass(final MethodVisitor mv) {
        mv.visitInsn(ACONST_NULL);
        mv.visitMethodInsn(INVOKESPECIAL,
                ASPECTJ_AROUND_CLOSURE_CLASS_NAME,
                INIT_METHOD_NAME,
                "([Ljava/lang/Object;)V");
    }

    /**
     * Creates a field to host the aspectj aspect instance and instantiate them if possible
     * <p/>
     *
     * @param cw
     * @param mv
     * @param aspectInfo
     * @param joinPointClassName
     */
    public void createAndStoreStaticAspectInstantiation(ClassVisitor cw, MethodVisitor mv, AspectInfo aspectInfo, String joinPointClassName) {
        String aspectClassSignature = aspectInfo.getAspectClassSignature();
        String aspectClassName = aspectInfo.getAspectClassName();
        DeploymentModel deploymentModel = aspectInfo.getDeploymentModel();

        if (deploymentModel.equals(DeploymentModel.PER_JVM)) {
            cw.visitField(ACC_PRIVATE + ACC_STATIC, aspectInfo.getAspectFieldName(), aspectClassSignature, null, null);
            mv.visitMethodInsn(INVOKESTATIC,
                    aspectClassName,
                    "aspectOf",
                    "()" + aspectClassSignature);
            mv.visitFieldInsn(PUTSTATIC, joinPointClassName, aspectInfo.getAspectFieldName(), aspectClassSignature);
        } else {
            throw new UnsupportedOperationException("unsupported deployment model for the AspectJAspectModel - " +
                    aspectClassName + " " +
                    deploymentModel);
        }
    }

    public void createAndStoreRuntimeAspectInstantiation(MethodVisitor methodVisitor, CompilerInput compilerInput, AspectInfo aspectInfo) {
        //TODO support for non perJVM aspects
        ;
    }

    public void loadAspect(MethodVisitor methodVisitor, CompilerInput compilerInput, AspectInfo aspectInfo) {
        s_modelHelper.loadAspect(methodVisitor,
                compilerInput,
                aspectInfo);
    }

    /**
     * Handles the arguments to an around advice. Same as in AW.
     */
    public void createAroundAdviceArgumentHandling(MethodVisitor methodVisitor, CompilerInput compilerInput, org.codehaus.aspectwerkz.org.objectweb.asm.Type[] types, AdviceMethodInfo adviceMethodInfo) {
        //TODO - likely to fail if we don't cast the "this" to the closure ?
        s_modelHelper.createAroundAdviceArgumentHandling(methodVisitor,
                compilerInput,
                types,
                adviceMethodInfo);
    }

    /**
     * Handles the arguments to a before/after advice. Same as in AW.
     */
    public void createBeforeOrAfterAdviceArgumentHandling(MethodVisitor codeVisitor, CompilerInput compilerInput, org.codehaus.aspectwerkz.org.objectweb.asm.Type[] types, AdviceMethodInfo adviceMethodInfo, int specialArgIndex) {
        s_modelHelper.createBeforeOrAfterAdviceArgumentHandling(codeVisitor,
                compilerInput,
                types,
                adviceMethodInfo,
                specialArgIndex);
    }

    /**
     * Handles the AspectJ advice info.
     *
     * @param classInfo
     * @param aspectDef
     * @param ajAdviceInfo
     * @return the new advice definition
     */
    private AdviceDefinition handleAdviceInfo(final ClassInfo classInfo,
                                              final AspectDefinition aspectDef,
                                              final AdviceInfo ajAdviceInfo) {

        MethodInfo adviceMethod = null;
        MethodInfo[] methods = classInfo.getMethods();
        for (int i = 0; i < methods.length; i++) {
            MethodInfo method = methods[i];
            if (method.getName().equals(ajAdviceInfo.adviceMethodName)) {
                adviceMethod = method;
                break;
            }
        }
        if (adviceMethod == null) {
            throw new Error("advice method [" + ajAdviceInfo.adviceMethodName +
                    "] could not be found in class [" + classInfo.getName() + "]");
        }
        String specialArgType = null;
        if (ajAdviceInfo.extraParameterFlags != 0) {
            specialArgType = ajAdviceInfo.parameterTypes[0];
        }
        return AdviceDefinition.newInstance(createFullAspectJAdviceMethodName(ajAdviceInfo),
                ajAdviceInfo.type,
                ajAdviceInfo.pointcut,
                specialArgType,
                classInfo.getName(),
                classInfo.getName(),
                adviceMethod,
                aspectDef);
    }

    /**
     * Creates a full AspectJ advice method name.
     *
     * @param ajAdviceInfo
     * @return the method name
     */
    private String createFullAspectJAdviceMethodName(final AdviceInfo ajAdviceInfo) {
        StringBuffer fullAdviceMethodName = new StringBuffer();
        fullAdviceMethodName.append(ajAdviceInfo.adviceMethodName).append('(');

        // FIXME support args() target() and this()
//        for (int i = 0; i < ajAdviceInfo.parameterTypes.length; i++) {
//            String type = ajAdviceInfo.parameterTypes[i];
//            fullAdviceMethodName.append(type).append(" arg").append(i);
//            if (i < ajAdviceInfo.parameterTypes.length - 1) {
//                fullAdviceMethodName.append(',');
//            }
//        }
        fullAdviceMethodName.append(')');
        return fullAdviceMethodName.toString();
    }

    /**
     * Returns the around advice infos.
     *
     * @param javaClass
     * @return
     */
    private List getAroundAdviceInfo(final JavaClass javaClass) {
        List advice = new ArrayList();
        Method[] methods = javaClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String adviceName = method.getName();
            if (adviceName.startsWith(PREFIX_AROUND_ADVICE) && !adviceName.endsWith(PROCEED_SUFFIX)) {
                advice.add(createAdviceInfo(javaClass, AdviceType.AROUND, method));
            }
        }
        return advice;
    }

    /**
     * Returns the before advice infos.
     *
     * @param javaClass
     * @return
     */
    private List getBeforeAdviceInfo(final JavaClass javaClass) {
        List advice = new ArrayList();
        Method[] methods = javaClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String adviceName = method.getName();
            if (adviceName.startsWith(PREFIX_BEFORE_ADVICE)) {
                advice.add(createAdviceInfo(javaClass, AdviceType.BEFORE, method));
            }
        }
        return advice;
    }

    /**
     * Returns the around advice infos.
     *
     * @param javaClass
     * @return
     */
    private List getAfterFinallyAdviceInfo(final JavaClass javaClass) {
        List advice = new ArrayList();
        Method[] methods = javaClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String adviceName = method.getName();
            if (adviceName.startsWith(PREFIX_AFTER_FINALLY_ADVICE)) {
                advice.add(createAdviceInfo(javaClass, AdviceType.AFTER_FINALLY, method));
            }
        }
        return advice;
    }

    /**
     * Returns the after throwing advice infos.
     *
     * @param javaClass
     * @return
     */
    private List getAfterThrowingAdviceInfo(final JavaClass javaClass) {
        List advice = new ArrayList();
        Method[] methods = javaClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String adviceName = method.getName();
            if (adviceName.startsWith(PREFIX_AFTER_THROWING_ADVICE)) {
                advice.add(createAdviceInfo(javaClass, AdviceType.AFTER_THROWING, method));
            }
        }
        return advice;
    }

    /**
     * Returns the after returning advice infos.
     *
     * @param javaClass
     * @return
     */
    private List getAfterReturningAdviceInfo(final JavaClass javaClass) {
        List advice = new ArrayList();
        Method[] methods = javaClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String adviceName = method.getName();
            if (adviceName.startsWith(PREFIX_AFTER_RETURNING_ADVICE)) {
                advice.add(createAdviceInfo(javaClass, AdviceType.AFTER_RETURNING, method));
            }
        }
        return advice;
    }

    /**
     * Creates advice info for the advice method.
     *
     * @param adviceType
     * @param method
     * @return
     */
    private AdviceInfo createAdviceInfo(final JavaClass javaClassfinal,
                                        final AdviceType adviceType,
                                        final Method method) {
        Attribute[] attributes = method.getAttributes();
        List ajAttributes = readAjAttributes(attributes, null);
        AdviceInfo adviceInfo = new AdviceInfo();
        for (Iterator it = ajAttributes.iterator(); it.hasNext();) {
            AjAttribute attr = (AjAttribute) it.next();
            if (attr instanceof AjAttribute.AdviceAttribute) {
                AjAttribute.AdviceAttribute adviceAttr = (AjAttribute.AdviceAttribute) attr;
                adviceInfo.type = adviceType;
                adviceInfo.aspectClassName = javaClassfinal.getClassName().replace('.', '/');
                adviceInfo.adviceMethodName = method.getName();
                String pointcut = adviceAttr.getPointcut().toString();
                if (pointcut.startsWith("execution(") ||
                        pointcut.startsWith("call(") ||
                        pointcut.startsWith("set(") ||
                        pointcut.startsWith("get(") ||
                        pointcut.startsWith("handler(") ||
                        pointcut.startsWith("adviceexecution(") ||
                        pointcut.startsWith("within(") ||
                        pointcut.startsWith("withincode(") ||
                        pointcut.startsWith("cflow(") ||
                        pointcut.startsWith("cflowbelow(") ||
                        pointcut.startsWith("if(") ||
                        pointcut.startsWith("this(") ||
                        pointcut.startsWith("target(") ||
                        pointcut.startsWith("args(") ||
                        pointcut.startsWith("initialization(") ||
                        pointcut.startsWith("staticinitialization(") ||
                        pointcut.startsWith("preinitialization(")) {
                    adviceInfo.pointcut = pointcut;
                } else if (pointcut.endsWith("()")) {
                    adviceInfo.pointcut = pointcut.substring(0, pointcut.length() - 2);
                } else {
                    throw new DefinitionException("pointcuts of type [" + pointcut + " are not yet supported");
                }
                adviceInfo.extraParameterFlags = adviceAttr.getExtraParameterFlags();
                int nrArgs = method.getArgumentTypes().length;
                String[] parameterTypes = new String[nrArgs];
                for (int i = 0; i < nrArgs; i++) {
                    Type type = method.getArgumentTypes()[i];
                    parameterTypes[i] = type.toString().replace('.', '/');
                }
                adviceInfo.parameterTypes = parameterTypes;
            }
        }
        return adviceInfo;
    }

    /**
     * Returns the aspect attributes.
     *
     * @param javaClass
     * @return
     */
    private List getAspectAttributes(final JavaClass javaClass) {
        return readAjAttributes(javaClass.getAttributes(), null);
    }

    /**
     * Reads in the AjAttributes from the bytecode.
     *
     * @param attrs
     * @param context
     * @return
     */
    private static List readAjAttributes(final Attribute[] attrs, final ISourceContext context) {
        List ajAttrs = new ArrayList();
        for (int i = attrs.length - 1; i >= 0; i--) {
            Attribute a = attrs[i];
            if (a instanceof Unknown) {
                Unknown u = (Unknown) a;
                String name = u.getName();
                if (name.startsWith(AjAttribute.AttributePrefix)) {
                    ajAttrs.add(AjAttribute.read(name, u.getBytes(), context));
                }
            }
        }
        return ajAttrs;
    }
}
