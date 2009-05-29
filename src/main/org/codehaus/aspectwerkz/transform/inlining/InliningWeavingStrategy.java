/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms 8of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining;

import gnu.trove.TLongObjectHashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;
import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.impl.asm.AsmClassInfo;
import org.codehaus.aspectwerkz.transform.Context;
import org.codehaus.aspectwerkz.transform.WeavingStrategy;
import org.codehaus.aspectwerkz.transform.inlining.weaver.AddInterfaceVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.AddMixinMethodsVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.AlreadyAddedMethodAdapter;
import org.codehaus.aspectwerkz.transform.inlining.weaver.ConstructorBodyVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.ConstructorCallVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.FieldSetFieldGetVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.HandlerVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.InstanceLevelAspectVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.JoinPointInitVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.LabelToLineNumberVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.MethodCallVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.MethodExecutionVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.StaticInitializationVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.SerialVersionUidVisitor;
import org.codehaus.aspectwerkz.transform.inlining.weaver.AddWrapperVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * A weaving strategy implementing a weaving scheme based on statical compilation, and no reflection.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class InliningWeavingStrategy implements WeavingStrategy {

    /**
     * Performs the weaving of the target class.
     *
     * @param className
     * @param context
     */
    public void transform(String className, final Context context) {
        try {
            final byte[] bytecode = context.getInitialBytecode();
            final ClassLoader loader = context.getLoader();

            ClassInfo classInfo = AsmClassInfo.getClassInfo(className, bytecode, loader);

            // skip Java reflect proxies for which we cannot get the resource as a stream
            // which leads to warnings when using annotation matching
            // Note: we use an heuristic assuming JDK proxy are classes named "$..."
            // to avoid to call getSuperClass everytime
            if (classInfo.getName().startsWith("$") && classInfo.getSuperclass().getName().equals("java.lang.reflect.Proxy")) {
                context.setCurrentBytecode(context.getInitialBytecode());
                return;
            }

            //TODO:FIXME match on (within, null, classInfo) should be equivalent to those ones. 
            final Set definitions = context.getDefinitions();
            final ExpressionContext[] ctxs = new ExpressionContext[]{
                new ExpressionContext(PointcutType.EXECUTION, classInfo, classInfo),
                new ExpressionContext(PointcutType.CALL, null, classInfo),
                new ExpressionContext(PointcutType.GET, null, classInfo),
                new ExpressionContext(PointcutType.SET, null, classInfo),
                new ExpressionContext(PointcutType.HANDLER, null, classInfo),
                new ExpressionContext(PointcutType.STATIC_INITIALIZATION, classInfo, classInfo),
                new ExpressionContext(PointcutType.WITHIN, classInfo, classInfo)
            };

            if (classFilter(definitions, ctxs, classInfo)) {
                return;
            }

            // build the ClassInfo from the bytecode to avoid loading it from the loader resource stream later
            // to support stub weaving
            //AsmClassInfo.getClassInfo(bytecode, loader);

            // compute CALL + GET/SET early matching results to avoid registering useless visitors
            final boolean filterForCall = classFilterFor(
                    definitions, new ExpressionContext[]{
                        new ExpressionContext(PointcutType.CALL, null, classInfo),
                        new ExpressionContext(PointcutType.WITHIN, classInfo, classInfo)
                    }
            );//FIXME - within make match all
            final boolean filterForGetSet = classFilterFor(
                    definitions, new ExpressionContext[]{
                        new ExpressionContext(PointcutType.GET, null, classInfo),
                        new ExpressionContext(PointcutType.SET, null, classInfo),
                        new ExpressionContext(PointcutType.WITHIN, classInfo, classInfo)
                    }
            );//FIXME - within make match all
            final boolean filterForHandler = classFilterFor(
                    definitions, new ExpressionContext[]{
                        new ExpressionContext(PointcutType.HANDLER, null, classInfo),
                        new ExpressionContext(PointcutType.WITHIN, classInfo, classInfo)
                    }
            );//FIXME - within make match all

            // note: for staticinitialization we do an exact match right there
            boolean filterForStaticinitialization = !classInfo.hasStaticInitializer()
            	||  classFilterFor(definitions, new ExpressionContext[] {
    					new ExpressionContext(PointcutType.STATIC_INITIALIZATION,
    					                      classInfo.staticInitializer(),
    					                      classInfo)
    			  		}
            		);
            if (!filterForStaticinitialization) {
                filterForStaticinitialization = !hasPointcut(definitions, new ExpressionContext(
                        PointcutType.STATIC_INITIALIZATION,
                        classInfo.staticInitializer(),
                        classInfo
                ));
            }

            // prepare ctor call jp
            final ClassReader crLookahead = new ClassReader(bytecode);
            TLongObjectHashMap newInvocationsByCallerMemberHash = null;
            if (!filterForCall) {
                newInvocationsByCallerMemberHash = new TLongObjectHashMap();
                crLookahead.accept(
                        new ConstructorCallVisitor.LookaheadNewDupInvokeSpecialInstructionClassAdapter(
                                newInvocationsByCallerMemberHash
                        ),
                        true
                );
            }

            // prepare handler jp, by gathering ALL catch blocks and their exception type
            List catchLabels = new ArrayList();
            if (!filterForHandler) {
                final ClassReader crLookahead2 = new ClassReader(bytecode);
                final ClassWriter cw2 = AsmHelper.newClassWriter(true);

                HandlerVisitor.LookaheadCatchLabelsClassAdapter lookForCatches =
                        new HandlerVisitor.LookaheadCatchLabelsClassAdapter(
                                cw2, loader, classInfo, context, catchLabels
                        );
                // we must visit exactly as we will do further on with debug info (that produces extra labels)
                crLookahead2.accept(lookForCatches, null, false);
            }

            // gather wrapper methods to support multi-weaving
            // skip annotations visit and debug info by using the lookahead read-only classreader
            Set addedMethods = new HashSet();
            crLookahead.accept(new AlreadyAddedMethodAdapter(addedMethods), true);

            // -- Phase 1 -- type change
            final ClassWriter writerPhase1 = AsmHelper.newClassWriter(true);
            final ClassReader readerPhase1 = new ClassReader(bytecode);
            ClassVisitor reversedChainPhase1 = writerPhase1;
            reversedChainPhase1 = new AddMixinMethodsVisitor(reversedChainPhase1, classInfo, context, addedMethods);
            reversedChainPhase1 = new AddInterfaceVisitor(reversedChainPhase1, classInfo, context);
            readerPhase1.accept(reversedChainPhase1, null, false);
            final byte[] bytesPhase1 = writerPhase1.toByteArray();

            // update the class info
            classInfo = AsmClassInfo.newClassInfo(bytesPhase1, loader);

            // -- Phase 2 -- advices
            final ClassWriter writerPhase2 = AsmHelper.newClassWriter(true);
            final ClassReader readerPhase2 = new ClassReader(bytesPhase1);
            ClassVisitor reversedChainPhase2 = writerPhase2;
            reversedChainPhase2 = new InstanceLevelAspectVisitor(reversedChainPhase2, classInfo, context);
            reversedChainPhase2 = new MethodExecutionVisitor(reversedChainPhase2, classInfo, context, addedMethods);
            reversedChainPhase2 = new ConstructorBodyVisitor(reversedChainPhase2, classInfo, context, addedMethods);
            if(!filterForStaticinitialization) {
            	reversedChainPhase2 = new StaticInitializationVisitor(reversedChainPhase2, context, addedMethods);
            }
            reversedChainPhase2 = new HandlerVisitor(reversedChainPhase2, context, catchLabels);
            if (!filterForCall) {
                reversedChainPhase2 = new MethodCallVisitor(reversedChainPhase2, loader, classInfo, context);
                reversedChainPhase2 = new ConstructorCallVisitor(
                        reversedChainPhase2, loader, classInfo, context, newInvocationsByCallerMemberHash
                );
            }
            if (!filterForGetSet) {
                reversedChainPhase2 = new FieldSetFieldGetVisitor(reversedChainPhase2, loader, classInfo, context);
            }
            reversedChainPhase2 = new LabelToLineNumberVisitor(reversedChainPhase2, context);
            readerPhase2.accept(reversedChainPhase2, null, false);
            final byte[] bytesPhase2 = writerPhase2.toByteArray();

            context.setCurrentBytecode(bytesPhase2);

            // -- Phase 3 -- serialUID and JoinPoint initialization
            if (context.isAdvised()) {
                final ClassWriter writerPhase3 = AsmHelper.newClassWriter(true);
                ClassReader readerPhase3 = new ClassReader(bytesPhase2);
                ClassVisitor reversedChainPhase3 = writerPhase3;
                reversedChainPhase3 = new SerialVersionUidVisitor.Add(reversedChainPhase3, context, classInfo);
                reversedChainPhase3 = new AddWrapperVisitor(reversedChainPhase3, context, addedMethods);
                reversedChainPhase3 = new JoinPointInitVisitor(reversedChainPhase3, context);
                readerPhase3.accept(reversedChainPhase3, null, false);
                final byte[] bytesPhase3 = writerPhase3.toByteArray();

                context.setCurrentBytecode(bytesPhase3);
            }

            // TODO: INNER CLASS OR NOT?
            // loop over emitted jp and flag them as inner classes
//            for (Iterator iterator = ((ContextImpl) context).getEmittedInlinedJoinPoint().iterator(); iterator.hasNext();) {
//                String joinPointClassName = ((ContextImpl.EmittedJoinPoint) iterator.next()).joinPointClassName;
//                int innerIndex = joinPointClassName.lastIndexOf('$');
//                cw.visitInnerClass(joinPointClassName,
//                        joinPointClassName.substring(0, innerIndex),
//                        joinPointClassName.substring(innerIndex + 1, joinPointClassName.length()),
//                        Constants.ACC_PUBLIC + Constants.ACC_STATIC);
//            }

//            // resolve line numbers - debug only
//            List ejp = ((ContextImpl)context).getEmittedJoinPoints();
//            for (Iterator iterator = ejp.iterator(); iterator.hasNext();) {
//                EmittedJoinPoint emittedJoinPoint = (EmittedJoinPoint) iterator.next();
//                emittedJoinPoint.resolveLineNumber(context);
//                System.out.println(emittedJoinPoint.toString());
//            }

            // NOTE: remove when in release time or in debugging trouble (;-) - Alex)
            // FAKE multiweaving - which is a requirement
            //            Object multi = context.getMetaData("FAKE");
            //            if (multi == null) {
            //                context.addMetaData("FAKE", "FAKE");
            //                transform(className, context);
            //            }

        } catch (Throwable t) {
            t.printStackTrace();
            throw new WrappedRuntimeException(t);
        }
    }

    /**
     * Creates a new transformation context.
     *
     * @param name
     * @param bytecode
     * @param loader
     * @return
     */
    public Context newContext(final String name, final byte[] bytecode, final ClassLoader loader) {
        return new ContextImpl(name, bytecode, loader);
    }

    /**
     * Filters out the classes that are not eligible for transformation.
     *
     * @param definitions the definitions
     * @param ctxs        an array with the contexts
     * @param classInfo   the class to filter
     * @return boolean true if the class should be filtered out
     */
    private static boolean classFilter(final Set definitions,
                                       final ExpressionContext[] ctxs,
                                       final ClassInfo classInfo) {
        if (classInfo.isInterface()) {
            return true;
        }
        for (Iterator defs = definitions.iterator(); defs.hasNext();) {
            if (classFilter((SystemDefinition) defs.next(), ctxs, classInfo)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Filters out the classes that are not eligible for transformation.
     *
     * @param definition the definition
     * @param ctxs       an array with the contexts
     * @param classInfo  the class to filter
     * @return boolean true if the class should be filtered out
     * @TODO: when a class had execution pointcut that were removed it must be unweaved, thus not filtered out How to
     * handle that? cache lookup? or custom class level attribute ?
     */
    private static boolean classFilter(final SystemDefinition definition,
                                       final ExpressionContext[] ctxs,
                                       final ClassInfo classInfo) {
        if (classInfo.isInterface()) {
            return true;
        }
        String className = classInfo.getName();
        if (definition.inExcludePackage(className)) {
            return true;
        }
        if (!definition.inIncludePackage(className)) {
            return true;
        }
        if (definition.isAdvised(ctxs)) {
            return false;
        }
        if (definition.hasMixin(ctxs)) {
            return false;
        }
        if (definition.hasIntroducedInterface(ctxs)) {
            return false;
        }
        if (definition.inPreparePackage(className)) {
            return false;
        }
        return true;
    }

    private static boolean classFilterFor(final Set definitions,
                                          final ExpressionContext[] ctxs) {
        for (Iterator defs = definitions.iterator(); defs.hasNext();) {
            if (classFilterFor((SystemDefinition) defs.next(), ctxs)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean classFilterFor(final SystemDefinition definition,
                                          final ExpressionContext[] ctxs) {
        if (definition.isAdvised(ctxs)) {
            return false;
        }
        return true;
    }

    private static boolean hasPointcut(final Set definitions,
                                       final ExpressionContext ctx) {
        for (Iterator defs = definitions.iterator(); defs.hasNext();) {
            if (hasPointcut((SystemDefinition) defs.next(), ctx)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    private static boolean hasPointcut(final SystemDefinition definition,
                                       final ExpressionContext ctx) {
        if (definition.hasPointcut(ctx)) {
            return true;
        }
        return false;
    }
}