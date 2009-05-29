/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.weaver;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.objectweb.asm.*;
import org.codehaus.aspectwerkz.transform.Context;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.transform.TransformationUtil;
import org.codehaus.aspectwerkz.transform.inlining.ContextImpl;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.InterfaceIntroductionDefinition;
import org.codehaus.aspectwerkz.definition.MixinDefinition;
import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ClassInfoHelper;

/**
 * Adds an interface to the target class.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AddInterfaceVisitor extends ClassAdapter implements TransformationConstants {

    private final static String ADVISABLE_MIXIN_IMPL_NAME = "org.codehaus.aspectwerkz.intercept.AdvisableImpl";

    private final ContextImpl m_ctx;
    private final ClassInfo m_classInfo;

    /**
     * Creates a new add interface class adapter.
     *
     * @param cv
     * @param classInfo
     * @param ctx
     */
    public AddInterfaceVisitor(final ClassVisitor cv,
                               final ClassInfo classInfo,
                               final Context ctx) {
        super(cv);
        m_classInfo = classInfo;
        m_ctx = (ContextImpl) ctx;
    }

    /**
     * Visits the class.
     *
     * @param access
     * @param name
     * @param signature
     * @param superName
     * @param interfaces
     */
    public void visit(final int version,
                      final int access,
                      final String name,
                      final String signature,
                      final String superName,
                      final String[] interfaces) {
        ExpressionContext ctx = new ExpressionContext(PointcutType.WITHIN, m_classInfo, m_classInfo);
        if (classFilter(m_classInfo, ctx, m_ctx.getDefinitions())) {
            super.visit(version, access, name, signature, superName, interfaces);
            return;
        }

        // javaclass names of interface to have
        // use a Set to avoid doublons
        final Set interfacesToAdd = new HashSet();//FIXME AVP must preserve ordering

        // already there interface javaclass names
        for (int i = 0; i < interfaces.length; i++) {
            interfacesToAdd.add(interfaces[i].replace('/', '.'));
        }

        // add new ones
        final Set systemDefinitions = m_ctx.getDefinitions();
        for (Iterator it = systemDefinitions.iterator(); it.hasNext();) {
            SystemDefinition systemDefinition = (SystemDefinition) it.next();
            final List interfaceIntroDefs = systemDefinition.getInterfaceIntroductionDefinitions(ctx);
            for (Iterator it2 = interfaceIntroDefs.iterator(); it2.hasNext();) {
                final InterfaceIntroductionDefinition interfaceIntroDef = (InterfaceIntroductionDefinition) it2.next();
                interfacesToAdd.addAll(interfaceIntroDef.getInterfaceClassNames());
            }
            final List mixinDefinitions = systemDefinition.getMixinDefinitions(ctx);
            for (Iterator it2 = mixinDefinitions.iterator(); it2.hasNext();) {
                final MixinDefinition mixinDef = (MixinDefinition) it2.next();
                if (ADVISABLE_MIXIN_IMPL_NAME.equals(mixinDef.getMixinImpl().getName())) {
                    // mark it as made advisable
                    m_ctx.markMadeAdvisable();
                }
                final List interfaceList = mixinDef.getInterfaceClassNames();
                for (Iterator it3 = interfaceList.iterator(); it3.hasNext();) {
                    interfacesToAdd.add(((String) it3.next()));
                }
            }
        }

        //FIXME AVP refactor to handle precedence injection
//        if (ClassInfoHelper.hasMethodClash(interfacesToAdd, m_ctx.getLoader())) {
//            super.visit(version, access, name, superName, interfaces, sourceFile);
//            return;
//        }

        int i = 0;
        final String[] newInterfaceArray = new String[interfacesToAdd.size()];
        for (Iterator it = interfacesToAdd.iterator(); it.hasNext();) {
            newInterfaceArray[i++] = (String) it.next();
        }

        for (int j = 0; j < newInterfaceArray.length; j++) {
            newInterfaceArray[j] = newInterfaceArray[j].replace('.', '/');

        }
        super.visit(version, access, name, signature, superName, newInterfaceArray);
        m_ctx.markAsAdvised();
    }

    /**
     * Filters the classes to be transformed.
     *
     * @param classInfo   the class to filter
     * @param ctx         the context
     * @param definitions a set with the definitions
     * @return boolean true if the method should be filtered away
     */
    public static boolean classFilter(final ClassInfo classInfo,
                                      final ExpressionContext ctx,
                                      final Set definitions) {
        for (Iterator it = definitions.iterator(); it.hasNext();) {
            SystemDefinition systemDef = (SystemDefinition) it.next();
            if (classInfo.isInterface()) {
                return true;
            }
            String className = classInfo.getName().replace('/', '.');
            if (systemDef.inExcludePackage(className)) {
                return true;
            }
            if (!systemDef.inIncludePackage(className)) {
                return true;
            }
            if (systemDef.hasMixin(ctx) || systemDef.hasIntroducedInterface(ctx)) {
                return false;
            }
        }
        return true;
    }
}