/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.ejb3;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassVisitor;
import org.codehaus.aspectwerkz.transform.inlining.AspectInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilationInfo;
import org.codehaus.aspectwerkz.transform.inlining.compiler.CompilerInput;
import org.codehaus.aspectwerkz.transform.inlining.compiler.AbstractJoinPointCompiler;
import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel;
import org.codehaus.aspectwerkz.transform.JoinPointCompiler;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.definition.AspectDefinition;

/**
 * A specific model where the bean is the aspect
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class EJBIsTheAspectModel extends EJBInterceptorModel {

    public final static String TYPE = EJBIsTheAspectModel.class.getName();

    public String getAspectModelType() {
        return TYPE;
    }

    public AspectModel getInstance(CompilationInfo.Model compilationModel) {
        return this;
    }

    public void defineAspect(ClassInfo aspectClassInfo, AspectDefinition aspectDef, ClassLoader loader) {
        //TODO if we want to reuse the ejb as interceptor in some aop.xml
    }

    public void createAndStoreStaticAspectInstantiation(ClassVisitor cw, MethodVisitor cv, AspectInfo aspectInfo, String joinPointClassName) {
        ;// since the EJB is the interceptor, we don't need any static level bookeeping
    }

    public void loadAspect(MethodVisitor cv, CompilerInput input, AspectInfo aspectInfo) {
        // the bean is the aspect
        AbstractJoinPointCompiler.loadCaller(cv, input);
    }

    public void createMandatoryMethods(ClassWriter cw, JoinPointCompiler compiler) {
        // since we define it as a sub model, we need to ensure that added methods are added only once
        AspectModel[] allModels = compiler.getAspectModels();
        for (int i = 0; i < allModels.length; i++) {
            AspectModel model = allModels[i];
            if (EJBInterceptorModel.TYPE.equals(model.getAspectModelType())) {
                // no extra things to add
                return;
            }
        }
        // not found, so delegate
        super.createMandatoryMethods(cw, compiler);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
