/************1**************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.annotation;

import org.codehaus.aspectwerkz.definition.MixinDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.DefinitionParserHelper;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.expression.ExpressionNamespace;
import org.codehaus.aspectwerkz.DeploymentModel;
import org.codehaus.aspectwerkz.DeploymentModel;

import java.util.Iterator;
import java.util.List;

/**
 * Extracts the mixin annotations from the class files and creates a meta-data representation of them.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class MixinAnnotationParser {

    /**
     * The sole instance.
     */
    private final static MixinAnnotationParser INSTANCE = new MixinAnnotationParser();

    /**
     * Private constructor to prevent subclassing.
     */
    private MixinAnnotationParser() {
    }

    /**
     * Parse the attributes and create and return a meta-data representation of them.
     *
     * @param classInfo the class to extract attributes from
     * @param mixinDef  the mixin definition
     */
    public static void parse(final ClassInfo classInfo, final MixinDefinition mixinDef) {
        INSTANCE.doParse(classInfo, mixinDef);
    }

    /**
     * Parse the attributes and create and return a meta-data representation of them.
     *
     * @param classInfo the class to extract attributes from
     * @param mixinDef  the mixin definition
     */
    private void doParse(final ClassInfo classInfo, final MixinDefinition mixinDef) {
        if (classInfo == null) {
            throw new IllegalArgumentException("class to parse can not be null");
        }
        if (classInfo == null) {
            throw new IllegalArgumentException("class can not be null");
        }
        final SystemDefinition systemDef = mixinDef.getSystemDefinition();
        Mixin annotation = (Mixin) AsmAnnotations.getAnnotation(AnnotationConstants.MIXIN, classInfo);
        if (annotation != null) {
            String expression = AspectAnnotationParser.getExpressionElseValue(
                    annotation.value(), annotation.pointcut()
            );
            final ExpressionInfo expressionInfo = new ExpressionInfo(expression, systemDef.getUuid());
            ExpressionNamespace.getNamespace(systemDef.getUuid()).addExpressionInfo(
                    DefinitionParserHelper.EXPRESSION_PREFIX + expression.hashCode(),
                    expressionInfo
            );
            mixinDef.addExpressionInfo(expressionInfo);
            mixinDef.setTransient(annotation.isTransient());
            if (annotation.deploymentModel() != null) {
                mixinDef.setDeploymentModel(DeploymentModel.getDeploymentModelFor(annotation.deploymentModel()));
            }
        }
    }
}