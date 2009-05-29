/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.ejb3;

import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel;
import org.codehaus.aspectwerkz.transform.inlining.AspectModelManager;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.reflect.impl.asm.AsmClassInfo;
import org.codehaus.aspectwerkz.definition.AspectDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.AdviceDefinition;
import org.codehaus.aspectwerkz.aspect.AdviceType;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;

import javax.ejb.Interceptor;
import javax.ejb.Interceptors;

/**
 * A programmatic deployer for registering SLSB EJBs in the system
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class EJBInterceptorDeployer {

    static {
        AspectModelManager.registerAspectModels(EJBIsTheAspectModel.class.getName());
        AspectModelManager.registerAspectModels(EJBInterceptorModel.class.getName());
    }
    final static AspectModel EJB_ISTHEASPECT_MODEL = AspectModelManager.getModelFor(EJBIsTheAspectModel.TYPE);
    final static AspectModel EJB_INTERCEPTOR_MODEL = AspectModelManager.getModelFor(EJBInterceptorModel.TYPE);

    /**
     * Deploy a new EJB. The EJB gets registered in the AOP model if it is having an @AroundInvoke method.
     *
     * @param ejbClassName
     * @param loader
     */
    public static void deploy(String ejbClassName, ClassLoader loader) {
        ClassInfo ejbClassInfo = AsmClassInfo.getClassInfo(ejbClassName, loader);
        SystemDefinition sysDef = SystemDefinitionContainer.getVirtualDefinitionAt(loader);

        // note: one could handle precedence here

        // deploy @Interceptor and @Interceptors entries
        if (ejbClassInfo.getAnnotationReader().isAnnotationPresent("javax.ejb.Interceptor")) {
            Interceptor interceptor = (Interceptor) ejbClassInfo.getAnnotationReader().getAnnotation("javax.ejb.Interceptor");
            String interceptorClassName = interceptor.value();
            deployTo(AsmClassInfo.getClassInfo(interceptorClassName, loader), ejbClassInfo, sysDef, false, loader);
        }

        if (ejbClassInfo.getAnnotationReader().isAnnotationPresent("javax.ejb.Interceptors")) {
            Interceptors interceptors = (Interceptors) ejbClassInfo.getAnnotationReader().getAnnotation("javax.ejb.Interceptors");
            String[] interceptorClassNames = interceptors.value();
            for (int i = 0; i < interceptorClassNames.length; i++) {
                String interceptorClassName = interceptorClassNames[i];
                deployTo(AsmClassInfo.getClassInfo(interceptorClassName, loader), ejbClassInfo, sysDef, false, loader);
            }
        }

        // deploy local interceptors last.
        deployTo(ejbClassInfo, ejbClassInfo, sysDef, true, loader);
    }

    private static void deployTo(ClassInfo interceptorClassInfo, ClassInfo ejbClassInfo,
                                 SystemDefinition sysDef, boolean isBeanTheAspect, ClassLoader loader) {

        System.out.println("AW EJB3 - Deploying " + interceptorClassInfo.getName() + " for " + ejbClassInfo.getName());

        //TODO right now limited to support only one local interceptor as per spec
        for (int i = 0; i < interceptorClassInfo.getMethods().length; i++) {
            MethodInfo methodInfo = interceptorClassInfo.getMethods()[i];
            if ("java.lang.Object".equals(methodInfo.getReturnType().getName())
                && methodInfo.getParameterTypes().length == 1
                && "javax.ejb.InvocationContext".equals(methodInfo.getParameterTypes()[0].getName())
                && interceptorClassInfo.getAnnotationReader().isMethodAnnotationPresent(
                        "javax.ejb.AroundInvoke",
                        methodInfo.getName(),
                        methodInfo.getSignature())
            ) {
                // we found an interceptor
                AspectDefinition aspect = new AspectDefinition(
                        interceptorClassInfo.getName(),
                        interceptorClassInfo,
                        sysDef
                );
                aspect.addAroundAdviceDefinition(
                        new AdviceDefinition(
                                methodInfo.getName(),
                                AdviceType.AROUND,
                                null,
                                interceptorClassInfo.getName(),
                                interceptorClassInfo.getName(),
                                //TODO ones should exclude EJB bean method / callbacks / whatever spec says.
                                //TODO vendor extension: would be easy to refine the pointcut
                                new ExpressionInfo(
                                        buildPointcut(ejbClassInfo.getName(), methodInfo, loader),
                                        interceptorClassInfo.getName()),
                                methodInfo,
                                aspect
                        )
                );
                sysDef.addAspect(aspect);

                if (isBeanTheAspect) {
                    // register it as er the EJB MODEL
                    aspect.setAspectModel(EJBIsTheAspectModel.TYPE);
                    EJB_ISTHEASPECT_MODEL.defineAspect(interceptorClassInfo, aspect, loader);
                } else {
                    aspect.setAspectModel(EJBInterceptorModel.TYPE);
                    EJB_INTERCEPTOR_MODEL.defineAspect(interceptorClassInfo, aspect, loader);
                }
            }
        }
    }

    private static String buildPointcut(String ejbClassName, MethodInfo interceptorMethod, ClassLoader loader) {
        StringBuffer pointcut = new StringBuffer();
        pointcut.append("execution(!@javax.ejb.AroundInvoke public !static * ").append(ejbClassName).append(".*(..))");
        //TODO exclude non business method like life cycle callbacks etc

        // handles vendor extension @AroundInvokeAOP(..pointcut..)
        if (interceptorMethod.getDeclaringType().getAnnotationReader().isMethodAnnotationPresent(
                "org.codehaus.aspectwerkz.ejb3.AroundInvokeAOP",
                interceptorMethod.getName(),
                interceptorMethod.getSignature())) {
            pointcut.append(" && ");
            pointcut.append(
                    ((AroundInvokeAOP)interceptorMethod.getDeclaringType().getAnnotationReader().getMethodAnnotation(
                            "org.codehaus.aspectwerkz.ejb3.AroundInvokeAOP",
                            interceptorMethod.getName(),
                            interceptorMethod.getSignature(),
                            loader
                    )).value()
            );
        }

        return pointcut.toString();
    }
}
