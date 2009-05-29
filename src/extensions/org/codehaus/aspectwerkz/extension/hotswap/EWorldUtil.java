/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.extension.hotswap;

import org.codehaus.aspectwerkz.definition.*;
import org.codehaus.aspectwerkz.hook.impl.ClassPreProcessorHelper;
import org.codehaus.aspectwerkz.transform.AspectWerkzPreProcessor;
import org.codehaus.aspectwerkz.transform.ClassCacheTuple;
import org.codehaus.aspectwerkz.transform.inlining.deployer.Deployer;
import org.codehaus.aspectwerkz.transform.inlining.deployer.DeploymentHandle;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * eworld/wlw/aop
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class EWorldUtil {

    private static final Map s_weaveStatus = new HashMap();

    public static boolean isWeaved(final String uuid, final String aspectName) {
        Map aspects = (Map) s_weaveStatus.get(uuid);
        if (aspects == null || aspects.keySet().size() == 0) {
            return false;
        } else {
            Boolean status = (Boolean) aspects.get(aspectName);
            if (status == null) {
                return false;
            } else {
                return status.booleanValue();
            }
        }
    }

    public static void activate(final String uuid,
                                final String aspectName,
                                final String adviceName,
                                final String expression,
                                final String pointcutName) {
        ClassLoader loader = EWorldUtil.class.getClassLoader();
        DeploymentScope scope = SystemDefinitionContainer.getDefinitionFor(loader, uuid).getDeploymentScope("demo");

        try {
            Class aspect = Class.forName(aspectName, false, loader);
            Deployer.deploy(aspect,
                            "<aspect class=\""+aspectName+"\">"
                            + "<advice name=\""+adviceName+"\" type=\"around\" bind-to=\""+expression+"\"/>"
                            + "</aspect>",
                            scope);
            setStatus(uuid, aspectName, Boolean.TRUE);
        } catch (Throwable t) {
            t.printStackTrace();
        }


        //        System.out.println(
        //                "activate = " + uuid + "," + aspectName + "." + adviceName + " @ " + expression + "," +
        // pointcutName
        //        );
        //        SystemDefinition sysDef = SystemDefinitionContainer.getSystemDefinition(
        //                ClassLoader.getSystemClassLoader(), uuid
        //        );
        //        if (sysDef == null) {
        //            return;
        //        }
        //        AspectDefinition aspectDef = sysDef.getAspectDefinition(aspectName);
        //
        //        Expression pcExpression =
        // ExpressionNamespace.getExpressionNamespace(aspectDef).createExpression(
        //                expression,
        //                "",
        //                pointcutName
        //        );
        //
        //        AdviceDefinition newDef = null;
        //        boolean found = false;
        //        for (Iterator arounds = aspectDef.getAroundAdviceDefinitions().iterator(); arounds.hasNext();) {
        //            AdviceDefinition around = (AdviceDefinition)arounds.next();
        //            if (around.getName().equals(aspectName + "." + adviceName)) {
        //                // copy the logMethod advice
        //                // note: we could add a totally new advice as well
        //                newDef = around.copyAt(pcExpression);
        //
        //                // take care of the runtime Pointcut mirror if any
        //                CflowStack as = SystemLoader.getCflowStack(ClassLoader.getSystemClassLoader());
        //                AspectManager am = as.getAspectManager(uuid);
        //                Pointcut pc =
        // am.getPointcutManager(aspectDef.getName()).getPointcut(newDef.getExpression().getExpression());
        //                if (pc!=null) {
        //                    pc.addAroundAdvice(aspectDef.getName() + "/" + around.getName());
        //                }
        //
        //                System.out.println("<adding> " + around.getName() + " at " + pointcutName);
        //                found = true;
        //                break;
        //            }
        //        }
        //        if (!found) {
        //            System.err.println(" advice not found");
        //        }
        //        else {
        //            aspectDef.addAroundAdvice(newDef);
        //            StartupManager.reinitializeSystem(ClassLoader.getSystemClassLoader(), sysDef);
        //        }
        //        setStatus(uuid, aspectName, Boolean.TRUE);
    }

    public static void deactivate(final String uuid,
                                  final String aspectName,
                                  final String adviceName,
                                  final String pointcutName) {
        ClassLoader loader = EWorldUtil.class.getClassLoader();
        DeploymentScope scope = SystemDefinitionContainer.getDefinitionFor(loader, uuid).getDeploymentScope("demo");

        try {
            Class aspect = Class.forName(aspectName, false, loader);
            Deployer.undeploy(aspect);
            setStatus(uuid, aspectName, Boolean.FALSE);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //
        //        System.out.println("deactivate = " + uuid + "," + aspectName + "." + adviceName + " @ " +
        // pointcutName);
        //        SystemDefinition sysDef = SystemDefinitionContainer.getSystemDefinition(
        //                ClassLoader.getSystemClassLoader(), uuid
        //        );
        //        if (sysDef == null) {
        //            return;
        //        }
        //        AspectDefinition aspectDef = sysDef.getAspectDefinition(aspectName);
        //
        //        List removedAdviceDefs = new ArrayList();
        //        boolean found = false;
        //        for (Iterator arounds = aspectDef.getAroundAdviceDefinitions().iterator(); arounds.hasNext();) {
        //            AdviceDefinition around = (AdviceDefinition)arounds.next();
        //            if (around.getName().equals(aspectName + "." + adviceName)) {
        //                found = true;
        //                if (pointcutName.equals(around.getExpression().getName()) ||
        //                    pointcutName.equals(around.getExpression().getExpression())) {
        //
        //                    // take care of the runtime Pointcut mirror if any
        //                    CflowStack as = SystemLoader.getCflowStack(ClassLoader.getSystemClassLoader());
        //                    AspectManager am = as.getAspectManager(uuid);
        //                    Pointcut pc =
        // am.getPointcutManager(aspectDef.getName()).getPointcut(around.getExpression().getExpression());
        //                    pc.removeAroundAdvice(aspectDef.getName() + "/" + around.getName());
        //
        //                    System.out.println("<removing> " + around.getName() + " at " + pointcutName);
        //                    removedAdviceDefs.add(around);
        //                }
        //            }
        //        }
        //        if (!found) {
        //            System.err.println(" advice not found");
        //        }
        //        for (Iterator arounds = removedAdviceDefs.iterator(); arounds.hasNext();) {
        //            aspectDef.removeAroundAdvice((AdviceDefinition)arounds.next());
        //        }
        //        StartupManager.reinitializeSystem(ClassLoader.getSystemClassLoader(), sysDef);
        //
        //        setStatus(uuid, aspectName, Boolean.FALSE);
    }

    public static void activateCache(String expression, String pointcutName) {
        activate(
                "eworld/wlw/aop",
                "examples.caching.CachingAspect",
                "cache",
                expression,
                pointcutName
        );
    }

    public static void deactivateCache(String pointcutName) {
        deactivate("eworld/wlw/aop", "examples.caching.CachingAspect", "cache", pointcutName);
    }

    public static void activateTrace(String expression, String pointcutName) {
        activate(
                "eworld/wlw/aop",
                "examples.logging.LoggingAspect",
                "logMethod",
                expression,
                pointcutName
        );
    }

    public static void deactivateTrace(String pointcutName) {
        deactivate("eworld/wlw/aop", "examples.logging.LoggingAspect", "logMethod", pointcutName);
    }

    public static void hotswap(String classPattern) {
        ClassLoader loader = EWorldUtil.class.getClassLoader();
        DeploymentScope scope = SystemDefinitionContainer.getDefinitionFor(loader, "eworld/wlw/aop").getDeploymentScope("demo");
        //throw new UnsupportedOperationException("not supported in AW 2.0");
    }

    public static void dumpSystemDefinitions(ClassLoader loader) {
        java.io.PrintStream out = System.out;
        out.println("dumpSystemDefinitions [ " + loader + " ]");
        Set defs = SystemDefinitionContainer.getDefinitionsFor(loader);
        for (Iterator sysDefs = defs.iterator(); sysDefs.hasNext();) {
            SystemDefinition sysDef = (SystemDefinition) sysDefs.next();
            out.print(sysDef.getUuid());
            out.println("");
            for (Iterator prepares = sysDef.getPreparePackages().iterator(); prepares.hasNext();) {
                out.print("[Prepare] " + prepares.next());
                out.println("");
            }
            for (Iterator aspectDefs = sysDef.getAspectDefinitions().iterator(); aspectDefs
                    .hasNext();) {
                AspectDefinition aspectDef = (AspectDefinition) aspectDefs.next();
                out.print("[Aspect] " + aspectDef.getName());
                out.println("");
                for (Iterator arounds = aspectDef.getAroundAdviceDefinitions().iterator(); arounds.hasNext();) {
                    AdviceDefinition around = (AdviceDefinition) arounds.next();
                    out.print("  [AroundAdvice] " + around.getName());
                    out.print("  ");
                    out.print(around.getExpressionInfo().toString());
                    out.println("");
                }
                out.println("\n-");
            }
            out.println("\n----");
        }
    }

    private static void setStatus(final String uuid, final String aspectName, final Boolean status) {
        Map aspects = (Map) s_weaveStatus.get(uuid);
        if (aspects == null) {
            aspects = new HashMap();
            s_weaveStatus.put(uuid, aspects);
        }
        aspects.put(aspectName, status);
    }
}