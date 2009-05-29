/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package yapbaop.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.codehaus.aspectwerkz.aspect.AdviceType;
import org.codehaus.aspectwerkz.definition.AdviceDefinition;
import org.codehaus.aspectwerkz.definition.AspectDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ReflectHelper;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaClassInfo;
import org.codehaus.aspectwerkz.transform.aopalliance.AopAllianceAspectModel;
import org.codehaus.aspectwerkz.transform.inlining.AspectModelManager;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class Yapbaop extends AopAllianceAspectModel {//extend not required but wants to acess constants

    // register the AOP Alliance model (do not require the -D option of AW ext container)
    static {
        System.setProperty("aspectwerkz.extension.aspectmodels", AopAllianceAspectModel.class.getName().replace('/', '.'));
        AspectModelManager.getModels();
    }

    // the AOP Alliance aspect for method
    private final static Method AOP_ALLIANCE_METHOD_AROUND;

    static {
        try {
            AOP_ALLIANCE_METHOD_AROUND = MethodInterceptor.class.getDeclaredMethod("invoke",
                    new Class[]{MethodInvocation.class});
        } catch (Throwable t) {
            throw new Error(t.toString());
        }
    }

    public static Handle bindAspect(Class aopAllianceAspect, String pointcut) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        ClassInfo aspectInfo = JavaClassInfo.getClassInfo(aopAllianceAspect);

        // check some
        if (aspectInfo.getMethod(ReflectHelper.calculateHash(AOP_ALLIANCE_METHOD_AROUND)) == null) {
            throw new RuntimeException("Aspect is not AOP Alliance compatible - " + aopAllianceAspect.getName());
        }

        SystemDefinition def = SystemDefinitionContainer.getVirtualDefinitionAt(cl);
        AspectDefinition aspectDef = new AspectDefinition(aspectInfo.getName(), aspectInfo, def);
        AdviceDefinition adviceDef = AdviceDefinition.newInstance("invoke", // as per AOPAlliance
                AdviceType.AROUND,
                "execution(" + pointcut + ")",
                null,
                aspectInfo.getName(),
                aspectInfo.getName(),
                aspectInfo.getMethod(ReflectHelper.calculateHash(AOP_ALLIANCE_METHOD_AROUND)),
                aspectDef);

        // make it an AOP Alliance aspect for the compiler
        aspectDef.setAspectModel(ASPECT_MODEL_TYPE);

        // add the advice
        aspectDef.addAroundAdviceDefinition(adviceDef);

        // add the aspect
        def.addAspect(aspectDef);

        return new Handle(adviceDef);
    }

    public static class Handle {
        private AdviceDefinition m_definition;

        private Handle(AdviceDefinition def) {
            m_definition = def;
        }

        public void unbind() {
            m_definition.setExpressionInfo(null);
        }
    }
}
