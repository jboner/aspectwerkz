/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.definition;

import org.codehaus.aspectwerkz.util.Strings;
import org.codehaus.aspectwerkz.aspect.AdviceType;
import org.codehaus.aspectwerkz.DeploymentModel;
import org.codehaus.aspectwerkz.intercept.AdvisableImpl;
import org.codehaus.aspectwerkz.reflect.impl.asm.AsmClassInfo;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaMethodInfo;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaClassInfo;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ClassInfoHelper;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.expression.regexp.Pattern;
import org.codehaus.aspectwerkz.expression.ExpressionNamespace;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.annotation.AspectAnnotationParser;
import org.codehaus.aspectwerkz.annotation.MixinAnnotationParser;
import org.codehaus.aspectwerkz.exception.DefinitionException;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.transform.inlining.AspectModelManager;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * Parses the XML definition using <tt>dom4j</tt>.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class DocumentParser {

    /**
     * Parses aspect class names.
     *
     * @param document the defintion as a document
     * @return the aspect class names
     */
    public static List parseAspectClassNames(final Document document) {
        final List aspectClassNames = new ArrayList();
        for (Iterator it1 = document.getRootElement().elementIterator("system"); it1.hasNext();) {
            Element system = (Element) it1.next();
            final String basePackage = getBasePackage(system);
            for (Iterator it11 = system.elementIterator("aspect"); it11.hasNext();) {
                String className = null;
                Element aspect = (Element) it11.next();
                for (Iterator it2 = aspect.attributeIterator(); it2.hasNext();) {
                    Attribute attribute = (Attribute) it2.next();
                    final String name = attribute.getName().trim();
                    final String value = attribute.getValue().trim();
                    if (name.equalsIgnoreCase("class")) {
                        className = value;
                    }
                }
                aspectClassNames.add(basePackage + className);
            }
            for (Iterator it11 = system.elementIterator("package"); it11.hasNext();) {
                final Element packageElement = ((Element) it11.next());
                final String packageName = getPackage(packageElement);
                for (Iterator it12 = packageElement.elementIterator("aspect"); it12.hasNext();) {
                    String className = null;
                    Element aspect = (Element) it12.next();
                    for (Iterator it2 = aspect.attributeIterator(); it2.hasNext();) {
                        Attribute attribute = (Attribute) it2.next();
                        final String name = attribute.getName().trim();
                        final String value = attribute.getValue().trim();
                        if (name.equalsIgnoreCase("class")) {
                            className = value;
                        }
                    }
                    aspectClassNames.add(packageName + className);
                }
            }
        }
        aspectClassNames.add(Virtual.class.getName());

        return aspectClassNames;
    }

    /**
     * Parses the definition DOM document.
     *
     * @param document    the defintion as a document
     * @param systemDef   the system definition
     * @param aspectClass the aspect class
     * @return the definition
     */
    public static AspectDefinition parseAspectDefinition(final Document document,
                                                         final SystemDefinition systemDef,
                                                         final Class aspectClass) {

        final Element aspect = document.getRootElement();

        if (!aspect.getName().equals("aspect")) {
            throw new DefinitionException("XML definition for aspect is not well-formed: " + document.asXML());
        }
        String specialAspectName = null;
        String className = null;
        String deploymentModelAsString = null;
        String containerClassName = null;
        for (Iterator it2 = aspect.attributeIterator(); it2.hasNext();) {
            Attribute attribute = (Attribute) it2.next();
            final String name = attribute.getName().trim();
            final String value = attribute.getValue().trim();
            if (name.equalsIgnoreCase("class")) {
                className = value;
            } else if (name.equalsIgnoreCase("deployment-model")) {
                deploymentModelAsString = value;
            } else if (name.equalsIgnoreCase("name")) {
                specialAspectName = value;
            } else if (name.equalsIgnoreCase("container")) {
                containerClassName = value;
            }
        }
        if (specialAspectName == null) {
            specialAspectName = className;
        }

        final ClassInfo classInfo = JavaClassInfo.getClassInfo(aspectClass);
        final ClassLoader loader = aspectClass.getClassLoader();

        // create the aspect definition
        final AspectDefinition aspectDef = new AspectDefinition(specialAspectName, classInfo, systemDef);
        //TODO: if this XML centric deployment is supposed to PRESERVE @Aspect values, then it is broken
        aspectDef.setContainerClassName(containerClassName);
        aspectDef.setDeploymentModel(DeploymentModel.getDeploymentModelFor(deploymentModelAsString));

        parsePointcutElements(aspect, aspectDef); //needed to support undefined named pointcut in Attributes AW-152

        // load the different aspect model and let them define their aspects
        AspectModelManager.defineAspect(classInfo, aspectDef, loader);

        // parse the aspect info
        parseParameterElements(aspect, aspectDef);
        parsePointcutElements(aspect, aspectDef); //reparse pc for XML override (AW-152)
        parseAdviceElements(aspect, aspectDef, JavaClassInfo.getClassInfo(aspectClass));
        parseIntroduceElements(aspect, aspectDef, "", aspectClass.getClassLoader());

        systemDef.addAspect(aspectDef);
        return aspectDef;
    }

    /**
     * Parses the definition DOM document.
     *
     * @param loader   the current class loader
     * @param document the defintion as a document
     * @return the definitions
     */
    public static Set parse(final ClassLoader loader, final Document document) {
        final Element root = document.getRootElement();

        // parse the transformation scopes
        return parseSystemElements(loader, root);
    }

    /**
     * Parses the <tt>system</tt> elements.
     *
     * @param loader the current class loader
     * @param root   the root element
     */
    private static Set parseSystemElements(final ClassLoader loader, final Element root) {
        final Set systemDefs = new HashSet();
        for (Iterator it1 = root.elementIterator("system"); it1.hasNext();) {
            Element system = (Element) it1.next();
            SystemDefinition definition = parseSystemElement(loader, system, getBasePackage(system));
            if (definition != null) {
                systemDefs.add(definition);
            }
        }
        return systemDefs;
    }

    /**
     * Parses the <tt>system</tt> elements.
     *
     * @param loader        the current class loader
     * @param systemElement the system element
     * @param basePackage   the base package
     * @return the definition for the system
     */
    private static SystemDefinition parseSystemElement(final ClassLoader loader,
                                                       final Element systemElement,
                                                       final String basePackage) {
        String uuid = systemElement.attributeValue("id");
        if ((uuid == null) || uuid.equals("")) {
            throw new DefinitionException("system UUID must be specified");
        }
        final SystemDefinition definition = new SystemDefinition(uuid);

        // add the virtual aspect
        addVirtualAspect(definition);

        // parse the global pointcuts
        List globalPointcuts = parseGlobalPointcutDefs(systemElement);
        //FIXME: systemDef should link a namespace, + remove static hashmap in Namespace (uuid clash in parallel CL)
        ExpressionNamespace systemNamespace = ExpressionNamespace.getNamespace(definition.getUuid());
        for (Iterator iterator = globalPointcuts.iterator(); iterator.hasNext();) {
            PointcutInfo pointcutInfo = (PointcutInfo) iterator.next();
            systemNamespace.addExpressionInfo(
                    pointcutInfo.name, new ExpressionInfo(pointcutInfo.expression, systemNamespace.getName())
            );
        }

        // parse the global deployment scopes definitions
        parseDeploymentScopeDefs(systemElement, definition);

        // parse the global advisable definitions
        parseAdvisableDefs(systemElement, definition);

        // parse the include, exclude and prepare elements
        parseIncludePackageElements(systemElement, definition, basePackage);
        parseExcludePackageElements(systemElement, definition, basePackage);
        parsePrepareElements(systemElement, definition, basePackage);

        // parse without package elements
        parseAspectElements(loader, systemElement, definition, basePackage, globalPointcuts);

        // parse without package elements
        parseMixinElements(loader, systemElement, definition, basePackage);

        // parse with package elements
        parsePackageElements(loader, systemElement, definition, basePackage, globalPointcuts);

        // add all deployment scopes to the virtual advice
        DefinitionParserHelper.attachDeploymentScopeDefsToVirtualAdvice(definition);

        return definition;
    }

    /**
     * Parses the global pointcuts.
     *
     * @param systemElement the system element
     * @return a list with the pointcuts
     */
    private static List parseGlobalPointcutDefs(final Element systemElement) {
        final List globalPointcuts = new ArrayList();
        for (Iterator it11 = systemElement.elementIterator("pointcut"); it11.hasNext();) {
            PointcutInfo pointcutInfo = new PointcutInfo();
            Element globalPointcut = (Element) it11.next();
            for (Iterator it2 = globalPointcut.attributeIterator(); it2.hasNext();) {
                Attribute attribute = (Attribute) it2.next();
                final String name = attribute.getName().trim();
                final String value = attribute.getValue().trim();
                if (name.equalsIgnoreCase("name")) {
                    pointcutInfo.name = value;
                } else if (name.equalsIgnoreCase("expression")) {
                    pointcutInfo.expression = value;
                }
            }
            // pointcut CDATA is expression unless already specified as an attribute
            if (pointcutInfo.expression == null) {
                pointcutInfo.expression = globalPointcut.getTextTrim();
            }
            globalPointcuts.add(pointcutInfo);
        }
        return globalPointcuts;
    }

    /**
     * Parses the global deployment-scope elements.
     *
     * @param systemElement the system element
     * @param definition
     */
    private static void parseDeploymentScopeDefs(final Element systemElement,
                                                 final SystemDefinition definition) {
        for (Iterator it11 = systemElement.elementIterator("deployment-scope"); it11.hasNext();) {
            String expression = null;
            String name = null;
            Element globalPointcut = (Element) it11.next();
            for (Iterator it2 = globalPointcut.attributeIterator(); it2.hasNext();) {
                Attribute attribute = (Attribute) it2.next();
                final String attrName = attribute.getName().trim();
                final String attrValue = attribute.getValue().trim();
                if (attrName.equalsIgnoreCase("name")) {
                    name = attrValue;
                } else if (attrName.equalsIgnoreCase("expression")) {
                    expression = attrValue;
                }
            }
            // pointcut CDATA is expression unless already specified as an attribute
            if (expression == null) {
                expression = globalPointcut.getTextTrim();
            }
            DefinitionParserHelper.createAndAddDeploymentScopeDef(name, expression, definition);
        }
    }

    /**
     * Parses the global advisable elements.
     *
     * @param systemElement the system element
     * @param definition
     */
    private static void parseAdvisableDefs(final Element systemElement,
                                           final SystemDefinition definition) {
        for (Iterator it11 = systemElement.elementIterator("advisable"); it11.hasNext();) {
            Element advisableElement = (Element) it11.next();
            String expression = "";
            String pointcutTypes = "all";
            for (Iterator it2 = advisableElement.attributeIterator(); it2.hasNext();) {
                Attribute attribute = (Attribute) it2.next();
                final String name = attribute.getName().trim();
                final String value = attribute.getValue().trim();
                if (name.equalsIgnoreCase("expression")) {
                    expression = value;
                } else if (name.equalsIgnoreCase("pointcut-type")) {
                    pointcutTypes = value;
                }
            }
            // pointcut CDATA is expression unless already specified as an attribute
            if (expression == null) {
                expression = advisableElement.getTextTrim();
            }
            handleAdvisableDefinition(definition, expression, pointcutTypes);
        }
    }

    /**
     * Parses the definition DOM document.
     *
     * @param loader          the current class loader
     * @param systemElement   the system element
     * @param definition      the definition
     * @param basePackage     the base package
     * @param globalPointcuts the global pointcuts
     */
    private static void parsePackageElements(final ClassLoader loader,
                                             final Element systemElement,
                                             final SystemDefinition definition,
                                             final String basePackage,
                                             final List globalPointcuts) {
        for (Iterator it1 = systemElement.elementIterator("package"); it1.hasNext();) {
            final Element packageElement = ((Element) it1.next());
            final String packageName = basePackage + getPackage(packageElement);
            parseAspectElements(loader, packageElement, definition, packageName, globalPointcuts);
            parseMixinElements(loader, packageElement, definition, packageName);
            parseAdvisableDefs(packageElement, definition);
        }
    }

    /**
     * Parses the <tt>aspect</tt> elements.
     *
     * @param loader          the current class loader
     * @param systemElement   the system element
     * @param definition      the definition object
     * @param packageName     the package name
     * @param globalPointcuts the global pointcuts
     */
    private static void parseAspectElements(final ClassLoader loader,
                                            final Element systemElement,
                                            final SystemDefinition definition,
                                            final String packageName,
                                            final List globalPointcuts) {

        for (Iterator it1 = systemElement.elementIterator("aspect"); it1.hasNext();) {
            String aspectName = null;
            String className = null;
            String deploymentModel = null;
            String containerClassName = null;
            Element aspect = (Element) it1.next();
            for (Iterator it2 = aspect.attributeIterator(); it2.hasNext();) {
                Attribute attribute = (Attribute) it2.next();
                final String name = attribute.getName().trim();
                final String value = attribute.getValue().trim();
                if (name.equalsIgnoreCase("class")) {
                    className = value;
                } else if (name.equalsIgnoreCase("deployment-model")) {
                    deploymentModel = value;
                } else if (name.equalsIgnoreCase("name")) {
                    aspectName = value;
                } else if (name.equalsIgnoreCase("container")) {
                    containerClassName = value;
                }
            }

            // class is mandatory
            if (Strings.isNullOrEmpty(className)) {
                System.err.println("Warning: could not load aspect without 'class=..' attribute");
                new Exception().printStackTrace();
                continue;
            }

            String aspectClassName = packageName + className;
            if (aspectName == null) {
                aspectName = aspectClassName;
            }

            // create the aspect definition
            ClassInfo aspectClassInfo;
            try {
                aspectClassInfo = AsmClassInfo.getClassInfo(aspectClassName, loader);
            } catch (Exception e) {
                System.err.println(
                        "Warning: could not load aspect "
                        + aspectClassName
                        + " from "
                        + loader
                        + "due to: "
                        + e.toString()
                );
                e.printStackTrace();
                continue;
            }

            final AspectDefinition aspectDef = new AspectDefinition(aspectName, aspectClassInfo, definition);

            // add the global pointcuts to the aspect
            for (Iterator it = globalPointcuts.iterator(); it.hasNext();) {
                PointcutInfo pointcutInfo = (PointcutInfo) it.next();
                DefinitionParserHelper.createAndAddPointcutDefToAspectDef(
                        pointcutInfo.name,
                        pointcutInfo.expression,
                        aspectDef
                );
            }
            parsePointcutElements(aspect, aspectDef); //needed to support undefined named pointcut in Attributes AW-152

            // load the different aspect model and let them define their aspects
            AspectModelManager.defineAspect(aspectClassInfo, aspectDef, loader);

            // parse the class bytecode annotations
            AspectAnnotationParser.parse(aspectClassInfo, aspectDef, loader);

            // XML definition settings always overrides attribute definition settings
            // AW-357
            if (!Strings.isNullOrEmpty(deploymentModel)) {
                aspectDef.setDeploymentModel(DeploymentModel.getDeploymentModelFor(deploymentModel));
            }
            if (!Strings.isNullOrEmpty(aspectName)) {
                aspectDef.setName(aspectName);
            }
            if (!Strings.isNullOrEmpty(containerClassName)) {
                aspectDef.setContainerClassName(containerClassName);
            }

            // parse the aspect info
            parseParameterElements(aspect, aspectDef);
            parsePointcutElements(aspect, aspectDef); //reparse pc for XML override (AW-152)
            parseAdviceElements(aspect, aspectDef, aspectClassInfo);
            parseIntroduceElements(aspect, aspectDef, packageName, loader);

            definition.addAspect(aspectDef);
        }
    }

    /**
     * Parses the <tt>mixin</tt> elements.
     *
     * @param loader           the current class loader
     * @param systemElement    the system element
     * @param systemDefinition the system definition
     * @param packageName      the package name
     */
    private static void parseMixinElements(final ClassLoader loader,
                                           final Element systemElement,
                                           final SystemDefinition systemDefinition,
                                           final String packageName) {

        for (Iterator it1 = systemElement.elementIterator("mixin"); it1.hasNext();) {
            String className = null;
            String deploymentModelAsString = null;
            boolean isTransient = false;
            boolean isTransientSetInXML = false;
            String factoryClassName = null;
            String expression = null;
            Element mixin = (Element) it1.next();
            for (Iterator it2 = mixin.attributeIterator(); it2.hasNext();) {
                Attribute attribute = (Attribute) it2.next();
                final String name = attribute.getName().trim();
                final String value = attribute.getValue().trim();
                if (name.equalsIgnoreCase("class")) {
                    className = value;
                } else if (name.equalsIgnoreCase("deployment-model") && value != null) {
                    deploymentModelAsString = value;
                } else if (name.equalsIgnoreCase("transient")) {
                    if (value != null && value.equalsIgnoreCase("true")) {
                        isTransient = true;
                        isTransientSetInXML = true;
                    }
                } else if (name.equalsIgnoreCase("factory")) {
                    factoryClassName = value;
                } else if (name.equalsIgnoreCase("bind-to")) {
                    expression = value;
                }
            }
            String mixinClassName = packageName + className;

            // create the mixin definition
            ClassInfo mixinClassInfo;
            try {
                mixinClassInfo = AsmClassInfo.getClassInfo(mixinClassName, loader);
            } catch (Exception e) {
                System.err.println(
                        "Warning: could not load mixin "
                        + mixinClassName
                        + " from "
                        + loader
                        + "due to: "
                        + e.toString()
                );
                e.printStackTrace();
                continue;
            }

            final DeploymentModel deploymentModel =
                    (deploymentModelAsString != null) ? DeploymentModel.getDeploymentModelFor(deploymentModelAsString)
                    : DeploymentModel.PER_INSTANCE;

            final MixinDefinition mixinDefinition =
                    DefinitionParserHelper.createAndAddMixinDefToSystemDef(
                            mixinClassInfo,
                            expression,
                            deploymentModel,
                            isTransient,
                            systemDefinition
                    );

            // parse the class bytecode annotations
            MixinAnnotationParser.parse(mixinClassInfo, mixinDefinition);

            // XML definition settings always overrides attribute definition settings if present
            if (!Strings.isNullOrEmpty(deploymentModelAsString)) {
                mixinDefinition.setDeploymentModel(DeploymentModel.getDeploymentModelFor(deploymentModelAsString));
            }
            if (!Strings.isNullOrEmpty(factoryClassName)) {
                mixinDefinition.setFactoryClassName(factoryClassName);
            }
            if (isTransientSetInXML) {
                mixinDefinition.setTransient(isTransient);
            }

            parseParameterElements(mixin, mixinDefinition);
        }
    }

    /**
     * Adds a virtual system aspect to the definition. Needed to do various tricks.
     *
     * @param definition
     */
    public static void addVirtualAspect(final SystemDefinition definition) {
        final Class clazz = Virtual.class;
        final String aspectName = clazz.getName();
        ClassInfo aspectClassInfo = JavaClassInfo.getClassInfo(clazz);
        final AspectDefinition aspectDef = new AspectDefinition(aspectName, aspectClassInfo, definition);
        try {
            MethodInfo methodInfo = JavaMethodInfo.getMethodInfo(clazz.getDeclaredMethod("virtual", new Class[]{}));
            aspectDef.addBeforeAdviceDefinition(
                    new AdviceDefinition(
                            methodInfo.getName(),
                            AdviceType.BEFORE,
                            null,
                            aspectName,
                            aspectName,
                            null,
                            methodInfo,
                            aspectDef
                    )
            );
        } catch (NoSuchMethodException e) {
            throw new Error("virtual aspect [" + aspectName + "] does not have expected method: " + e.toString());
        }
        definition.addAspect(aspectDef);
    }

    /**
     * Parses the aspectElement parameters.
     *
     * @param aspectElement the aspect element
     * @param aspectDef     the aspect def
     */
    private static void parseParameterElements(final Element aspectElement,
                                               final AspectDefinition aspectDef) {
        for (Iterator it2 = aspectElement.elementIterator(); it2.hasNext();) {
            Element parameterElement = (Element) it2.next();
            if (parameterElement.getName().trim().equals("param")) {
                aspectDef.addParameter(
                        parameterElement.attributeValue("name"),
                        parameterElement.attributeValue("value")
                );
            }
        }
    }

    /**
     * Parses the mixinElement parameters.
     *
     * @param mixinElement the mixin element
     * @param mixinDef     the mixin def
     */
    private static void parseParameterElements(final Element mixinElement,
                                               final MixinDefinition mixinDef) {
        for (Iterator it2 = mixinElement.elementIterator(); it2.hasNext();) {
            Element parameterElement = (Element) it2.next();
            if (parameterElement.getName().trim().equals("param")) {
                mixinDef.addParameter(
                        parameterElement.attributeValue("name"),
                        parameterElement.attributeValue("value")
                );
            }
        }
    }

    /**
     * Parses the pointcuts.
     *
     * @param aspectElement the aspect element
     * @param aspectDef     the system definition
     */
    private static void parsePointcutElements(final Element aspectElement, final AspectDefinition aspectDef) {
        for (Iterator it2 = aspectElement.elementIterator(); it2.hasNext();) {
            Element pointcutElement = (Element) it2.next();
            if (pointcutElement.getName().trim().equals("pointcut")) {
                String name = pointcutElement.attributeValue("name");
                String expression = pointcutElement.attributeValue("expression");
                // pointcut CDATA is expression unless already specified as an attribute
                if (expression == null) {
                    expression = pointcutElement.getTextTrim();
                }
                DefinitionParserHelper.createAndAddPointcutDefToAspectDef(name, expression, aspectDef);
            } else if (pointcutElement.getName().trim().equals("deployment-scope")) {
                String name = pointcutElement.attributeValue("name");
                String expression = pointcutElement.attributeValue("expression");
                // pointcut CDATA is expression unless already specified as an attribute
                if (expression == null) {
                    expression = pointcutElement.getTextTrim();
                }
                DefinitionParserHelper.createAndAddDeploymentScopeDef(
                        name, expression, aspectDef.getSystemDefinition()
                );
            } else if (pointcutElement.getName().trim().equals("advisable")) {
                String expression = pointcutElement.attributeValue("expression");
                String pointcutTypes = pointcutElement.attributeValue("pointcut-type");
                if (expression == null) {
                    expression = pointcutElement.getTextTrim();
                }
                handleAdvisableDefinition(aspectDef.getSystemDefinition(), expression, pointcutTypes);
            }
        }
    }

    /**
     * Parses the advices.
     *
     * @param aspectElement   the aspect element
     * @param aspectDef       the system definition
     * @param aspectClassInfo the aspect class
     */
    private static void parseAdviceElements(final Element aspectElement,
                                            final AspectDefinition aspectDef,
                                            final ClassInfo aspectClassInfo) {
        List methodList = ClassInfoHelper.createMethodList(aspectClassInfo);
        for (Iterator it2 = aspectElement.elementIterator(); it2.hasNext();) {
            Element adviceElement = (Element) it2.next();
            if (adviceElement.getName().trim().equals("advice")) {
                String name = adviceElement.attributeValue("name");
                String type = adviceElement.attributeValue("type");
                String bindTo = adviceElement.attributeValue("bind-to");

                String adviceName = name;
                MethodInfo method = null;
                for (Iterator it3 = methodList.iterator(); it3.hasNext();) {
                    MethodInfo methodCurrent = (MethodInfo) it3.next();
                    if (aspectDef.isAspectWerkzAspect()) {
                        if (matchMethodAsAdvice(methodCurrent, name)) {
                            method = methodCurrent;
                            break;
                        }
                    } else {
                        // TODO support matchMethodAsAdvice(..) for all aspect models? if so use stuff below
//                        AspectModel aspectModel = AspectModelManager.getModelFor(aspectDef.getAspectModel());
//                        if (aspectModel.matchMethodAsAdvice(methodCurrent, name)) {
//                            method = methodCurrent;
//                            break;
//                        }
                        if (methodCurrent.getName().equals(name)) {
                            method = methodCurrent;
                            break;
                        }
                    }
                }
                if (method == null) {
                    throw new DefinitionException(
                            "could not find advice method [" + name + "] in [" + aspectClassInfo.getName() +
                            "] (are you using a compiler extension that you have not registered?)"+
                            " (are you using XML defined advice, with StaticJoinPoint bindings without specifying the full" +
                            "source like signature?)"
                    );
                }
                createAndAddAdviceDefsToAspectDef(type, bindTo, adviceName, method, aspectDef);
                for (Iterator it1 = adviceElement.elementIterator("bind-to"); it1.hasNext();) {
                    Element bindToElement = (Element) it1.next();
                    String pointcut = bindToElement.attributeValue("pointcut");
                    createAndAddAdviceDefsToAspectDef(type, pointcut, adviceName, method, aspectDef);
                }
            }
        }
    }

    /**
     * Parses the interface introductions.
     *
     * @param aspectElement the aspect element
     * @param aspectDef     the system definition
     * @param packageName
     * @param loader
     */
    private static void parseIntroduceElements(final Element aspectElement,
                                               final AspectDefinition aspectDef,
                                               final String packageName,
                                               final ClassLoader loader) {
        for (Iterator it2 = aspectElement.elementIterator(); it2.hasNext();) {
            Element introduceElement = (Element) it2.next();
            if (introduceElement.getName().trim().equals("introduce")) {
                String klass = introduceElement.attributeValue("class");
                String name = introduceElement.attributeValue("name");
                String bindTo = introduceElement.attributeValue("bind-to");

                // default name = FQN
                final String fullClassName = packageName + klass;
                if ((name == null) || (name.length() <= 0)) {
                    name = fullClassName;
                }

                // load the class info to determine if it is a pure interface introduction
                ClassInfo introductionClassInfo;
                try {
                    introductionClassInfo = AsmClassInfo.getClassInfo(fullClassName, loader);
                } catch (Exception e) {
                    throw new DefinitionException(
                            "could not find interface introduction: "
                            + packageName
                            + klass
                            + " "
                            + e.getMessage()
                    );
                }

                // pure interface introduction
                if (introductionClassInfo.isInterface()) {
                    DefinitionParserHelper.createAndAddInterfaceIntroductionDefToAspectDef(
                            bindTo,
                            name,
                            fullClassName,
                            aspectDef
                    );

                    // handles nested "bind-to" elements
                    for (Iterator it1 = introduceElement.elementIterator("bind-to"); it1.hasNext();) {
                        Element bindToElement = (Element) it1.next();
                        String pointcut = bindToElement.attributeValue("pointcut");
                        DefinitionParserHelper.createAndAddInterfaceIntroductionDefToAspectDef(
                                pointcut,
                                name,
                                fullClassName,
                                aspectDef
                        );
                    }
                }
            }
        }
    }

    /**
     * Creates the advice definitions and adds them to the aspect definition.
     *
     * @param type      the type of advice
     * @param bindTo    the pointcut expresion
     * @param name      the name of the advice
     * @param method    the method implementing the advice
     * @param aspectDef the aspect definition
     */
    private static void createAndAddAdviceDefsToAspectDef(final String type,
                                                          final String bindTo,
                                                          final String name,
                                                          final MethodInfo method,
                                                          final AspectDefinition aspectDef) {
        try {
            if (type.equalsIgnoreCase("around")) {
                final String aspectName = aspectDef.getName();
                AdviceDefinition adviceDef = DefinitionParserHelper.createAdviceDefinition(
                        name,
                        AdviceType.AROUND,
                        bindTo,
                        null,
                        aspectName,
                        aspectDef.getClassName(),
                        method,
                        aspectDef
                );
                aspectDef.addAroundAdviceDefinition(adviceDef);

            } else if (type.equalsIgnoreCase("before")) {
                final String aspectName = aspectDef.getName();
                AdviceDefinition adviceDef = DefinitionParserHelper.createAdviceDefinition(
                        name,
                        AdviceType.BEFORE,
                        bindTo,
                        null,
                        aspectName,
                        aspectDef.getClassName(),
                        method,
                        aspectDef
                );
                aspectDef.addBeforeAdviceDefinition(adviceDef);

            } else if (type.startsWith("after")) {
                String specialArgumentType = null;
                AdviceType adviceType = AdviceType.AFTER;
                if (type.startsWith("after returning(")) {
                    adviceType = AdviceType.AFTER_RETURNING;
                    int start = type.indexOf('(');
                    int end = type.indexOf(')');
                    specialArgumentType = type.substring(start + 1, end).trim();
                } else if (type.startsWith("after throwing(")) {
                    adviceType = AdviceType.AFTER_THROWING;
                    int start = type.indexOf('(');
                    int end = type.indexOf(')');
                    specialArgumentType = type.substring(start + 1, end).trim();
                } else if (type.startsWith("after returning")) {
                    adviceType = AdviceType.AFTER_RETURNING;
                } else if (type.startsWith("after throwing")) {
                    adviceType = AdviceType.AFTER_THROWING;
                } else if (type.startsWith("after")) {
                    adviceType = AdviceType.AFTER_FINALLY;
                } else if (type.startsWith("after finally")) {
                    adviceType = AdviceType.AFTER_FINALLY;
                }
                if (specialArgumentType != null && specialArgumentType.indexOf(' ') > 0) {
                    throw new DefinitionException(
                            "argument to after (returning/throwing) can only be a type (parameter name binding should be done using args(..))"
                    );
                }
                final String aspectName = aspectDef.getName();
                AdviceDefinition adviceDef = DefinitionParserHelper.createAdviceDefinition(
                        name,
                        adviceType,
                        bindTo,
                        specialArgumentType,
                        aspectName,
                        aspectDef.getClassName(),
                        method,
                        aspectDef
                );

                aspectDef.addAfterAdviceDefinition(adviceDef);
            } else {
                throw new DefinitionException("Unkonw type for advice : " + type);
            }
        } catch (DefinitionException e) {
            System.err.println(
                    "WARNING: unable to register advice " + aspectDef.getName() + "." + name +
                    " at pointcut [" + bindTo + "] due to: " + e.getMessage()
            );
            // TODO ALEX - better handling of reg issue (f.e. skip the whole aspect, in DocumentParser, based on DefinitionE
        }
    }

    /**
     * Retrieves and returns the package.
     *
     * @param packageElement the package element
     * @return the package as a string ending with DOT, or empty string
     */
    private static String getPackage(final Element packageElement) {
        String packageName = "";
        for (Iterator it2 = packageElement.attributeIterator(); it2.hasNext();) {
            Attribute attribute = (Attribute) it2.next();
            if (attribute.getName().trim().equalsIgnoreCase("name")) {
                packageName = attribute.getValue().trim();
                if (packageName.endsWith(".*")) {
                    packageName = packageName.substring(0, packageName.length() - 1);
                } else if (packageName.endsWith(".")) {
                    ; // skip
                } else {
                    packageName += ".";
                }
                break;
            } else {
                continue;
            }
        }
        return packageName;
    }

    /**
     * Parses the <tt>include</tt> elements.
     *
     * @param root        the root element
     * @param definition  the definition object
     * @param packageName the package name
     */
    private static void parseIncludePackageElements(final Element root,
                                                    final SystemDefinition definition,
                                                    final String packageName) {
        for (Iterator it1 = root.elementIterator("include"); it1.hasNext();) {
            String includePackage = "";
            Element includeElement = (Element) it1.next();
            for (Iterator it2 = includeElement.attributeIterator(); it2.hasNext();) {
                Attribute attribute = (Attribute) it2.next();
                if (attribute.getName().trim().equalsIgnoreCase("package")) {
                    // handle base package
                    if (packageName.endsWith(".*")) {
                        includePackage = packageName.substring(0, packageName.length() - 2);
                    } else if (packageName.endsWith(".")) {
                        includePackage = packageName.substring(0, packageName.length() - 1);
                    }

                    // handle exclude package
                    includePackage = packageName + attribute.getValue().trim();
                    if (includePackage.endsWith(".*")) {
                        includePackage = includePackage.substring(0, includePackage.length() - 2);
                    } else if (includePackage.endsWith(".")) {
                        includePackage = includePackage.substring(0, includePackage.length() - 1);
                    }
                    break;
                } else {
                    continue;
                }
            }
            if (includePackage.length() != 0) {
                definition.addIncludePackage(includePackage);
            }
        }
    }

    /**
     * Parses the <tt>exclude</tt> elements.
     *
     * @param root        the root element
     * @param definition  the definition object
     * @param packageName the package name
     */
    private static void parseExcludePackageElements(final Element root,
                                                    final SystemDefinition definition,
                                                    final String packageName) {
        for (Iterator it1 = root.elementIterator("exclude"); it1.hasNext();) {
            String excludePackage = "";
            Element excludeElement = (Element) it1.next();
            for (Iterator it2 = excludeElement.attributeIterator(); it2.hasNext();) {
                Attribute attribute = (Attribute) it2.next();
                if (attribute.getName().trim().equalsIgnoreCase("package")) {
                    // handle base package
                    if (packageName.endsWith(".*")) {
                        excludePackage = packageName.substring(0, packageName.length() - 2);
                    } else if (packageName.endsWith(".")) {
                        excludePackage = packageName.substring(0, packageName.length() - 1);
                    }

                    // handle exclude package
                    excludePackage = packageName + attribute.getValue().trim();
                    if (excludePackage.endsWith(".*")) {
                        excludePackage = excludePackage.substring(0, excludePackage.length() - 2);
                    } else if (excludePackage.endsWith(".")) {
                        excludePackage = excludePackage.substring(0, excludePackage.length() - 1);
                    }
                    break;
                } else {
                    continue;
                }
            }
            if (excludePackage.length() != 0) {
                definition.addExcludePackage(excludePackage);
            }
        }
    }

    /**
     * Parses the <tt>prepare</tt> elements.
     *
     * @param root        the root element
     * @param definition  the definition object
     * @param packageName the base package name
     */
    public static void parsePrepareElements(final Element root,
                                            final SystemDefinition definition,
                                            final String packageName) {
        for (Iterator it1 = root.elementIterator("prepare"); it1.hasNext();) {
            String preparePackage = "";
            Element prepareElement = (Element) it1.next();
            for (Iterator it2 = prepareElement.attributeIterator(); it2.hasNext();) {
                Attribute attribute = (Attribute) it2.next();
                if (attribute.getName().trim().equals("package")) {
                    // handle base package
                    if (packageName.endsWith(".*")) {
                        preparePackage = packageName.substring(0, packageName.length() - 2);
                    } else if (packageName.endsWith(".")) {
                        preparePackage = packageName.substring(0, packageName.length() - 1);
                    }

                    // handle prepare package
                    preparePackage = packageName + attribute.getValue().trim();
                    if (preparePackage.endsWith(".*")) {
                        preparePackage = preparePackage.substring(0, preparePackage.length() - 2);
                    } else if (preparePackage.endsWith(".")) {
                        preparePackage = preparePackage.substring(0, preparePackage.length() - 1);
                    }
                    break;
                } else {
                    continue;
                }
            }
            if (preparePackage.length() != 0) {
                definition.addPreparePackage(preparePackage);
            }
        }
    }

    /**
     * Retrieves and returns the base package for a system element
     *
     * @param system a system element
     * @return the base package
     */
    private static String getBasePackage(final Element system) {
        String basePackage = "";
        for (Iterator it2 = system.attributeIterator(); it2.hasNext();) {
            Attribute attribute = (Attribute) it2.next();
            if (attribute.getName().trim().equalsIgnoreCase("base-package")) {
                basePackage = attribute.getValue().trim();
                if (basePackage.endsWith(".*")) {
                    basePackage = basePackage.substring(0, basePackage.length() - 1);
                } else if (basePackage.endsWith(".")) {
                    ; // skip
                } else {
                    basePackage += ".";
                }
                break;
            } else {
                continue;
            }
        }
        return basePackage;
    }

    /**
     * Struct with pointcut info.
     */
    private static class PointcutInfo {
        public String name;
        public String expression;
    }

    /**
     * Check if a method from an aspect class match a given advice signature.
     * <br/>
     * If the signature is just a method name, then we have a match even if JoinPoint is sole method parameter.
     * Else we match both method name and parameters type, with abbreviation support (java.lang.* and JoinPoint)
     *
     * @param method
     * @param adviceSignature
     * @return
     */
    private static boolean matchMethodAsAdvice(MethodInfo method, String adviceSignature) {
        // grab components from adviceSignature
        //TODO catch AOOBE for better syntax error reporting
        String[] signatureElements = Strings.extractMethodSignature(adviceSignature);

        // check method name
        if (!method.getName().equals(signatureElements[0])) {
            return false;
        }
        // check number of args
        if (method.getParameterTypes().length * 2 != signatureElements.length - 1) {
            // we still match if method has "JoinPoint" has sole parameter
            // and adviceSignature has none
            if (signatureElements.length == 1 &&
                method.getParameterTypes().length == 1 &&
                (method.getParameterTypes()[0].getName().equals(TransformationConstants.JOIN_POINT_JAVA_CLASS_NAME)
                 || method.getParameterTypes()[0].getName().equals(TransformationConstants.STATIC_JOIN_POINT_JAVA_CLASS_NAME))) {
                return true;
            } else {
                return false;
            }
        }
        int argIndex = 0;
        for (int i = 1; i < signatureElements.length; i++) {
            String paramType = signatureElements[i++];
            String methodParamType = method.getParameterTypes()[argIndex++].getName();
            // handle shortcuts for java.lang.* and JoinPoint, StaticJoinPoint and Rtti
            String paramTypeResolved = (String) Pattern.ABBREVIATIONS.get(paramType);
            if (methodParamType.equals(paramType) || methodParamType.equals(paramTypeResolved)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles the advisable definition.
     *
     * @param definition
     * @param withinPointcut
     * @param pointcutTypes
     */
    private static void handleAdvisableDefinition(final SystemDefinition definition,
                                                  final String withinPointcut,
                                                  final String pointcutTypes) {
        // add the Advisable Mixin with the expression defined to the system definitions
        definition.addMixinDefinition(
                DefinitionParserHelper.createAndAddMixinDefToSystemDef(
                        AdvisableImpl.CLASS_INFO,
                        withinPointcut,
                        DeploymentModel.PER_INSTANCE,
                        false, // advisble mixin is NOT transient
                        definition
                )
        );

        boolean hasAllPointcuts = false;
        boolean hasExecutionPointcut = false;
        boolean hasCallPointcut = false;
        boolean hasSetPointcut = false;
        boolean hasGetPointcut = false;
        boolean hasHandlerPointcut = false;
        if (pointcutTypes == null ||
            pointcutTypes.equals("") ||
            pointcutTypes.equalsIgnoreCase("all")) {
            hasAllPointcuts = true;
        } else {
            StringTokenizer tokenizer = new StringTokenizer(pointcutTypes, "|");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token.trim().equalsIgnoreCase("all")) {
                    hasAllPointcuts = true;
                    break;
                } else if (token.trim().equalsIgnoreCase("execution")) {
                    hasExecutionPointcut = true;
                } else if (token.trim().equalsIgnoreCase("call")) {
                    hasCallPointcut = true;
                } else if (token.trim().equalsIgnoreCase("set")) {
                    hasSetPointcut = true;
                } else if (token.trim().equalsIgnoreCase("get")) {
                    hasGetPointcut = true;
                } else if (token.trim().equalsIgnoreCase("handler")) {
                    hasHandlerPointcut = true;
                }
            }
        }
        if (hasAllPointcuts || hasExecutionPointcut) {
            DefinitionParserHelper.createAndAddAdvisableDef(
                    // TODO add ctor to expression - BUT: problem with mixin and ctor, ordering issue, Jp.invoke() calls field instance that has not been init yet in ctor (since body not invoked)
                    //"(( execution(!static * *.*(..)) || execution(*.new(..)) ) && " + withinPointcut + ')',
                    // we exclude static method execution since we need the advisable instance
                    "(execution(!static * *.*(..)) && " + withinPointcut + ')',
                    definition
            );
        }
        if (hasAllPointcuts || hasCallPointcut) {
            DefinitionParserHelper.createAndAddAdvisableDef(
                    // TODO add ctor to expression - BUT: problem with mixin and ctor, ordering issue, Jp.invoke() calls field instance that has not been init yet in ctor (since body not invoked)                    //"(call(!static * " + typePattern + ".*(..)) || call(" + typePattern + ".new(..)))",
                    // we exclude static method withincode since we need the advisable instance
                    // as a consequence, withincode(staticinitialization(..)) is also excluded
                    "(call(* *.*(..)) && withincode(!static * *.*(..)) && " + withinPointcut + ')',
                    definition
            );
        }
        if (hasAllPointcuts || hasSetPointcut) {
            DefinitionParserHelper.createAndAddAdvisableDef(
                    // we exclude static method withincode since we need the advisable instance
                    // as a consequence, withincode(staticinitialization(..)) is also excluded
                    "(set(* *.*) && withincode(!static * *.*(..)) && " + withinPointcut + ')',
                    definition
            );
        }
        if (hasAllPointcuts || hasGetPointcut) {
            DefinitionParserHelper.createAndAddAdvisableDef(
                    // we exclude static method withincode since we need the advisable instance
                    // as a consequence, withincode(staticinitialization(..)) is also excluded
                    "(get(* *.*) && withincode(!static * *.*(..))  && " + withinPointcut + ')',
                    definition
            );
        }
        if (hasAllPointcuts || hasHandlerPointcut) {
            DefinitionParserHelper.createAndAddAdvisableDef(
                    // we exclude static method withincode since we need the advisable instance
                    // as a consequence, withincode(staticinitialization(..)) is also excluded
                    "(handler(*..*) && withincode(!static * *.*(..))  && " + withinPointcut + ')',
                    definition
            );
        }
    }
}