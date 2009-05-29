/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.aspect.container;

import org.codehaus.aspectwerkz.util.ContextClassLoader;
import org.codehaus.aspectwerkz.DeploymentModel;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AspectFactoryManager {

    public static String getAspectFactoryClassName(String aspectClassName, String aspectQualifiedName) {
        return aspectClassName.replace('.', '/') + "$" + aspectQualifiedName.replace('.', '/').replace('/', '_') + "_AWFactory";
    }

    // TODO refactor for offline stuff -genjp
    /**
     * Ensure that the aspect factory is loaded.
     *
     * @param aspectFactoryJavaClassName
     * @param aspectClassName
     * @param aspectQualifiedName
     * @param containerClassName
     * @param rawParameters
     * @param loader
     * @param deploymentModelAsString
     */
    public static void loadAspectFactory(
        String aspectFactoryJavaClassName,
        String uuid,
        String aspectClassName,
        String aspectQualifiedName,
        String containerClassName,
        String rawParameters,
        ClassLoader loader,
        String deploymentModelAsString) {

        try {
            ContextClassLoader.forName(loader, aspectFactoryJavaClassName);
        } catch (ClassNotFoundException e) {
            // compile it
            DeploymentModel deploymentModel = DeploymentModel.getDeploymentModelFor(deploymentModelAsString);
            AbstractAspectFactoryCompiler compiler;
            if (DeploymentModel.PER_JVM.equals(deploymentModel)) {
                compiler = new PerJVMAspectFactoryCompiler(
                        uuid,
                        aspectClassName,
                        aspectQualifiedName,
                        containerClassName,
                        rawParameters,
                        loader
                );
            } else if (DeploymentModel.PER_CLASS.equals(deploymentModel)) {
                compiler = new LazyPerXFactoryCompiler.PerClassAspectFactoryCompiler(
                        uuid,
                        aspectClassName,
                        aspectQualifiedName,
                        containerClassName,
                        rawParameters,
                        loader
                );
            } else if (DeploymentModel.PER_INSTANCE.equals(deploymentModel)) {
                compiler = new PerObjectFactoryCompiler.PerInstanceFactoryCompiler(
                        uuid,
                        aspectClassName,
                        aspectQualifiedName,
                        containerClassName,
                        rawParameters,
                        loader
                );
            } else if (DeploymentModel.PER_TARGET.equals(deploymentModel)
                       || DeploymentModel.PER_THIS.equals(deploymentModel)) {
                compiler = new PerObjectFactoryCompiler(
                        uuid,
                        aspectClassName,
                        aspectQualifiedName,
                        containerClassName,
                        rawParameters,
                        loader
                );
            } else if (DeploymentModel.PER_CFLOW.equals(deploymentModel)
                       || DeploymentModel.PER_CFLOWBELOW.equals(deploymentModel)) {
                compiler = new PerCflowXAspectFactoryCompiler(
                        uuid,
                        aspectClassName,
                        aspectQualifiedName,
                        containerClassName,
                        rawParameters,
                        loader
                );
            } else {
                //FIXME perThread
                throw new Error("Unimplemented " + deploymentModel);
            }

            // define the entity
            Artifact artifact = compiler.compile();
            AsmHelper.defineClass(loader, artifact.bytecode, artifact.className);
        }
    }

}
