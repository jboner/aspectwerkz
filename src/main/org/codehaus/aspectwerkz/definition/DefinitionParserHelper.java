/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.definition;

import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.expression.ExpressionNamespace;
import org.codehaus.aspectwerkz.expression.regexp.Pattern;
import org.codehaus.aspectwerkz.util.Strings;
import org.codehaus.aspectwerkz.aspect.AdviceType;
import org.codehaus.aspectwerkz.DeploymentModel;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.exception.DefinitionException;

import java.util.Iterator;
import java.util.Collection;

/**
 * Helper class for the attribute and the XML definition parsers.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class DefinitionParserHelper {
    public static final String EXPRESSION_PREFIX = "AW_";

    /**
     * Creates and adds pointcut definition to aspect definition.
     *
     * @param name
     * @param expression
     * @param aspectDef
     */
    public static void createAndAddPointcutDefToAspectDef(final String name,
                                                          final String expression,
                                                          final AspectDefinition aspectDef) {
        PointcutDefinition pointcutDef = new PointcutDefinition(expression);
        aspectDef.addPointcutDefinition(pointcutDef);

        // name can be the "pcName(paramType paramName)"
        // extract the parameter name to type map
        // and register the pointcut using its name
        //TODO: support for same pc name and different signature
        String pointcutName = name;
        String pointcutCallSignature = null;
        if (name.indexOf("(") > 0) {
            pointcutName = name.substring(0, name.indexOf("("));
            pointcutCallSignature = name.substring(name.indexOf("(") + 1, name.lastIndexOf(")"));
        }

        // do a lookup first to avoid infinite recursion when:
        // <pointcut name="pc" ...> [will be registered as pc]
        // <advice bind-to="pc" ...> [will be registered as pc and should not override previous one !]
        ExpressionNamespace namespace = ExpressionNamespace.getNamespace(aspectDef.getQualifiedName());
        ExpressionInfo info = namespace.getExpressionInfoOrNull(pointcutName);
        if (info == null) {
            info = new ExpressionInfo(expression, aspectDef.getQualifiedName());
            // extract the pointcut signature map
            if (pointcutCallSignature != null) {
                String[] parameters = Strings.splitString(pointcutCallSignature, ",");
                for (int i = 0; i < parameters.length; i++) {
                    String[] parameterInfo = Strings.splitString(
                            Strings.replaceSubString(parameters[i].trim(), "  ", " "),
                            " "
                    );
                    info.addArgument(parameterInfo[1], parameterInfo[0], aspectDef.getClassInfo().getClassLoader());
                }
            }
        }
        ExpressionNamespace.getNamespace(aspectDef.getQualifiedName()).addExpressionInfo(pointcutName, info);
    }

    /**
     * Creates and adds a prepared pointcut definition to virtual aspect definition.
     *
     * @param name
     * @param expression
     * @param systemDef
     */
    public static void createAndAddDeploymentScopeDef(final String name,//FIXME Depl scpope(prasen)field name not unique - need FQN
                                                      final String expression,
                                                      final SystemDefinition systemDef) {
        AspectDefinition aspectDef = systemDef.getAspectDefinition(Virtual.class.getName());
        aspectDef.addPointcutDefinition(new PointcutDefinition(expression));
        systemDef.addDeploymentScope(new DeploymentScope(name, expression));
    }

    /**
     * Creates and adds an advisable definition to virtual aspect definition.
     *
     * @param expression
     * @param systemDef
     */
    public static void createAndAddAdvisableDef(final String expression, final SystemDefinition systemDef) {
        AspectDefinition virtualAspectDef = systemDef.getAspectDefinition(Virtual.class.getName());
        virtualAspectDef.addPointcutDefinition(new PointcutDefinition(expression));

        AdviceDefinition virtualAdviceDef = (AdviceDefinition) virtualAspectDef.getBeforeAdviceDefinitions().get(0);
        ExpressionInfo oldExpressionInfo = virtualAdviceDef.getExpressionInfo();
        String newExpression;
        if (oldExpressionInfo != null) {
            String oldExpression = oldExpressionInfo.toString();
            newExpression = oldExpression + " || " + expression;
        } else {
            newExpression = expression;
        }

        virtualAdviceDef.setExpressionInfo(
                new ExpressionInfo(
                        newExpression,
                        virtualAspectDef.getQualifiedName()
                )
        );
    }

    /**
     * Attaches all deployment scopes in a system to the virtual advice.
     *
     * @param systemDef the system definition
     */
    public static void attachDeploymentScopeDefsToVirtualAdvice(final SystemDefinition systemDef) {
        final AspectDefinition virtualAspectDef = systemDef.getAspectDefinition(Virtual.class.getName());
        final AdviceDefinition virtualAdviceDef = (AdviceDefinition) virtualAspectDef.getBeforeAdviceDefinitions().get(
                0
        );

        final StringBuffer newExpression = new StringBuffer();
        final ExpressionInfo oldExpressionInfo = virtualAdviceDef.getExpressionInfo();
        if (oldExpressionInfo != null) {
            String oldExpression = oldExpressionInfo.toString();
            newExpression.append(oldExpression);
        }
        final Collection deploymentScopes = systemDef.getDeploymentScopes();
        if (deploymentScopes.size() != 0 && oldExpressionInfo != null) {
            newExpression.append(" || ");
        }
        for (Iterator it = deploymentScopes.iterator(); it.hasNext();) {
            DeploymentScope deploymentScope = (DeploymentScope) it.next();
            newExpression.append(deploymentScope.getExpression());
            if (it.hasNext()) {
                newExpression.append(" || ");
            }
        }
        if (newExpression.length() != 0) {
            virtualAdviceDef.setExpressionInfo(
                    new ExpressionInfo(
                            newExpression.toString(),
                            virtualAspectDef.getQualifiedName()
                    )
            );
        }
    }

    /**
     * Creates and add mixin definition to system definition.
     *
     * @param mixinClassInfo
     * @param expression
     * @param deploymentModel
     * @param isTransient
     * @param systemDef
     * @return the mixin definition
     */
    public static MixinDefinition createAndAddMixinDefToSystemDef(final ClassInfo mixinClassInfo,
                                                                  final String expression,
                                                                  final DeploymentModel deploymentModel,
                                                                  final boolean isTransient,
                                                                  final SystemDefinition systemDef) {
        final MixinDefinition mixinDef = createMixinDefinition(
                mixinClassInfo,
                expression,
                deploymentModel,
                isTransient,
                systemDef
        );

        // check doublons - TODO change ArrayList to HashMap since NAME is a key
        MixinDefinition doublon = null;
        for (Iterator intros = systemDef.getMixinDefinitions().iterator(); intros.hasNext();) {
            MixinDefinition intro = (MixinDefinition) intros.next();
            if (intro.getMixinImpl().getName().equals(mixinDef.getMixinImpl().getName())) {
                doublon = intro;
                intro.addExpressionInfos(mixinDef.getExpressionInfos());
                break;
            }
        }
        if (doublon == null) {
            systemDef.addMixinDefinition(mixinDef);
        }
        return mixinDef;
    }

    /**
     * Creates and add interface introduction definition to aspect definition.
     *
     * @param expression
     * @param introductionName
     * @param interfaceClassName
     * @param aspectDef
     */
    public static void createAndAddInterfaceIntroductionDefToAspectDef(final String expression,
                                                                       final String introductionName,
                                                                       final String interfaceClassName,
                                                                       final AspectDefinition aspectDef) {
        // Introduction name is unique within an aspectDef only
        InterfaceIntroductionDefinition introDef = createInterfaceIntroductionDefinition(
                introductionName,
                expression,
                interfaceClassName,
                aspectDef
        );
        aspectDef.addInterfaceIntroductionDefinition(introDef);
    }

    /**
     * Creates a new advice definition.
     *
     * @param adviceName          the advice name
     * @param adviceType          the advice type
     * @param expression          the advice expression
     * @param specialArgumentType the arg
     * @param aspectName          the aspect name
     * @param aspectClassName     the aspect class name
     * @param methodInfo          the advice methodInfo
     * @param aspectDef           the aspect definition
     * @return the new advice definition
     */
    public static AdviceDefinition createAdviceDefinition(final String adviceName,
                                                          final AdviceType adviceType,
                                                          final String expression,
                                                          final String specialArgumentType,
                                                          final String aspectName,
                                                          final String aspectClassName,
                                                          final MethodInfo methodInfo,
                                                          final AspectDefinition aspectDef) {
        ExpressionInfo expressionInfo = new ExpressionInfo(
                expression,
                aspectDef.getQualifiedName()
        );

        // support for pointcut signature
        String adviceCallSignature = null;
        String resolvedSpecialArgumentType = specialArgumentType;
        if (adviceName.indexOf('(') > 0) {
            adviceCallSignature = adviceName.substring(adviceName.indexOf('(') + 1, adviceName.lastIndexOf(')'));
            String[] parameters = Strings.splitString(adviceCallSignature, ",");
            for (int i = 0; i < parameters.length; i++) {
                String[] parameterInfo = Strings.splitString(
                        Strings.replaceSubString(parameters[i].trim(), "  ", " "),
                        " "
                );
                // Note: for XML defined aspect, we support anonymous parameters like
                // advice(JoinPoint, Rtti) as well as abbreviations, so we have to assign
                // them a name here, as well as their real type
                String paramName, paramType = null;
                if (parameterInfo.length == 2) {
                    paramName = parameterInfo[1];
                    paramType = parameterInfo[0];
                } else {
                    paramName = "anonymous_" + i;
                    paramType = (String) Pattern.ABBREVIATIONS.get(parameterInfo[0]);
                }
                // skip the parameter if this ones is a after returning / throwing binding
                if (paramName.equals(specialArgumentType)) {
                    resolvedSpecialArgumentType = paramType;
                    expressionInfo.setSpecialArgumentName(paramName);
                } else {
                    expressionInfo.addArgument(paramName, paramType, aspectDef.getClassInfo().getClassLoader());
                }
            }
        }

        // check that around advice return Object else the compiler will fail
        if (adviceType.equals(AdviceType.AROUND)) {
            if (!"java.lang.Object".equals(methodInfo.getReturnType().getName())) {
                throw new DefinitionException(
                        "around advice must return java.lang.Object : " + aspectClassName + "." + methodInfo.getName()
                );
            }
        }

        final AdviceDefinition adviceDef = new AdviceDefinition(
                adviceName,
                adviceType,
                resolvedSpecialArgumentType,
                aspectName,
                aspectClassName,
                expressionInfo,
                methodInfo,
                aspectDef
        );
        return adviceDef;
    }

    /**
     * Creates an introduction definition.
     *
     * @param mixinClassInfo
     * @param expression
     * @param deploymentModel
     * @param isTransient
     * @param systemDef
     * @return
     */
    public static MixinDefinition createMixinDefinition(final ClassInfo mixinClassInfo,
                                                        final String expression,
                                                        final DeploymentModel deploymentModel,
                                                        final boolean isTransient,
                                                        final SystemDefinition systemDef) {
        final MixinDefinition mixinDef = new MixinDefinition(mixinClassInfo, deploymentModel, isTransient, systemDef);
        if (expression != null) {
            ExpressionInfo expressionInfo = new ExpressionInfo(expression, systemDef.getUuid());

            // auto-name the pointcut which is anonymous for introduction
            ExpressionNamespace.getNamespace(systemDef.getUuid()).addExpressionInfo(
                    EXPRESSION_PREFIX + expression.hashCode(),
                    expressionInfo
            );
            mixinDef.addExpressionInfo(expressionInfo);
        }
        return mixinDef;
    }

    /**
     * Creates a new interface introduction definition.
     *
     * @param introductionName   the introduction name
     * @param expression         the pointcut expression
     * @param interfaceClassName the class name of the interface
     * @param aspectDef          the aspect definition
     * @return the new introduction definition
     */
    public static InterfaceIntroductionDefinition createInterfaceIntroductionDefinition(final String introductionName,
                                                                                        final String expression,
                                                                                        final String interfaceClassName,
                                                                                        final AspectDefinition aspectDef) {
        final InterfaceIntroductionDefinition introDef = new InterfaceIntroductionDefinition(
                introductionName, interfaceClassName
        );
        if (expression != null) {
            ExpressionInfo expressionInfo = new ExpressionInfo(expression, aspectDef.getQualifiedName());

            // auto-name the pointcut which is anonymous for introduction
            ExpressionNamespace.getNamespace(aspectDef.getQualifiedName()).addExpressionInfo(
                    EXPRESSION_PREFIX + expression.hashCode(),
                    expressionInfo
            );
            introDef.addExpressionInfo(expressionInfo);
        }
        return introDef;
    }

}