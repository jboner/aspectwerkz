/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.compiler;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;

/**
 * Redefines the existing join point class and turns it into a delegation class delegating to the newly created
 * replacement join point class.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class HandlerJoinPointRedefiner extends HandlerJoinPointCompiler {
    /**
     * The redefined model.
     */
    private final CompilationInfo.Model m_redefinedModel;

    /**
     * Creates a new join point compiler instance.
     *
     * @param model
     */
    HandlerJoinPointRedefiner(final CompilationInfo model) {
        super(model.getInitialModel());
        m_redefinedModel = model.getRedefinedModel();
    }

    /**
     * Creates the 'invoke' method.
     */
    protected void createInvokeMethod() {
        String invokeDesc = buildInvokeMethodSignature();
        MethodVisitor cv = m_cw.visitMethod(
                ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                INVOKE_METHOD_NAME,
                invokeDesc,
                null,
                new String[]{
                    THROWABLE_CLASS_NAME
                }
        );
        AsmHelper.loadArgumentTypes(cv, Type.getArgumentTypes(invokeDesc), true);
        cv.visitMethodInsn(INVOKESTATIC, m_redefinedModel.getJoinPointClassName(), INVOKE_METHOD_NAME, invokeDesc);
        AsmHelper.addReturnStatement(cv, Type.getReturnType(invokeDesc));
        cv.visitMaxs(0, 0);
    }

    /**
     * Creates the 'invoke' method.
     */
    protected void createInlinedInvokeMethod() {
        createInvokeMethod();
    }
}
