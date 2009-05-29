/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.aspectwerkz.util.ContextClassLoader;
import org.codehaus.aspectwerkz.util.ContextClassLoader;
import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel;
import org.codehaus.aspectwerkz.transform.inlining.compiler.AspectWerkzAspectModel;
import org.codehaus.aspectwerkz.definition.AspectDefinition;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.exception.DefinitionException;

/**
 * Manages the different aspect model implementations that is registered.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class AspectModelManager {

    public static final String ASPECT_MODELS_VM_OPTION = "aspectwerkz.extension.aspectmodels";
    private static final String DELIMITER = ":";

    /**
     * The aspects models that are registered
     */
    private static List ASPECT_MODELS = new ArrayList(1);

    static {
        ASPECT_MODELS.add(new AspectWerkzAspectModel());
        registerAspectModels(System.getProperty(ASPECT_MODELS_VM_OPTION, null));

    }

    /**
     * Returns an array with all the aspect models that has been registered.
     *
     * @return an array with the aspect models
     */
    public static AspectModel[] getModels() {
        return (AspectModel[]) ASPECT_MODELS.toArray(new AspectModel[]{});
    }

    /**
     * Returns the advice model for a specific aspect model type id.
     *
     * @param type the aspect model type id
     * @return the aspect model
     */
    public static AspectModel getModelFor(String type) {
        for (Iterator iterator = ASPECT_MODELS.iterator(); iterator.hasNext();) {
            AspectModel aspectModel = (AspectModel) iterator.next();
            if (aspectModel.getAspectModelType().equals(type)) {
                return aspectModel;
            }
        }
        return null;
    }

    /**
     * Let all aspect models try to define the aspect (only one should succeed).
     *
     * @param aspectClassInfo
     * @param aspectDef
     * @param loader
     */
    public static void defineAspect(final ClassInfo aspectClassInfo,
                                    final AspectDefinition aspectDef,
                                    final ClassLoader loader) {
        for (Iterator iterator = ASPECT_MODELS.iterator(); iterator.hasNext();) {
            AspectModel aspectModel = (AspectModel) iterator.next();
            aspectModel.defineAspect(aspectClassInfo, aspectDef, loader);
        }
    }

    /**
     * Registers aspect models.
     *
     * @param aspectModels the class names of the aspect models to register concatenated and separated with a ':'.
     */
    public static void registerAspectModels(final String aspectModels) {
        if (aspectModels != null) {
            StringTokenizer tokenizer = new StringTokenizer(aspectModels, DELIMITER);
            while (tokenizer.hasMoreTokens()) {
                final String className = tokenizer.nextToken();
                try {
                    final Class modelClass = ContextClassLoader.forName(className);
                    ASPECT_MODELS.add((AspectModel) modelClass.newInstance());
                } catch (ClassNotFoundException e) {
                    throw new DefinitionException(
                            "aspect model implementation class not found [" +
                            className + "]: " + e.toString()
                    );
                } catch (Exception e) {
                    throw new DefinitionException(
                            "aspect model implementation class could not be instantiated [" +
                            className +
                            "] - make sure it has a default no argument constructor: " +
                            e.toString()
                    );
                }
            }
        }
    }
}
