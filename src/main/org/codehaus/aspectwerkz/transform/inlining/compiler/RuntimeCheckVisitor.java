/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.compiler;


import org.codehaus.aspectwerkz.expression.ExpressionVisitor;
import org.codehaus.aspectwerkz.expression.Undeterministic;
import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.ExpressionNamespace;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.expression.ast.ASTOr;
import org.codehaus.aspectwerkz.expression.ast.ASTAnd;
import org.codehaus.aspectwerkz.expression.ast.ASTNot;
import org.codehaus.aspectwerkz.expression.ast.ASTTarget;
import org.codehaus.aspectwerkz.expression.ast.ASTPointcutReference;
import org.codehaus.aspectwerkz.expression.ast.ASTExecution;
import org.codehaus.aspectwerkz.expression.ast.ASTCall;
import org.codehaus.aspectwerkz.expression.ast.ASTSet;
import org.codehaus.aspectwerkz.expression.ast.ASTGet;
import org.codehaus.aspectwerkz.expression.ast.ASTHandler;
import org.codehaus.aspectwerkz.expression.ast.ASTStaticInitialization;
import org.codehaus.aspectwerkz.expression.ast.ASTWithin;
import org.codehaus.aspectwerkz.expression.ast.ASTWithinCode;
import org.codehaus.aspectwerkz.expression.ast.ASTHasMethod;
import org.codehaus.aspectwerkz.expression.ast.ASTHasField;
import org.codehaus.aspectwerkz.expression.ast.ASTThis;
import org.codehaus.aspectwerkz.expression.ast.ASTCflow;
import org.codehaus.aspectwerkz.expression.ast.ASTCflowBelow;
import org.codehaus.aspectwerkz.expression.ast.ASTArgs;
import org.codehaus.aspectwerkz.transform.inlining.compiler.AbstractJoinPointCompiler;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.cflow.CflowCompiler;
import org.codehaus.aspectwerkz.aspect.AdviceInfo;
import org.codehaus.aspectwerkz.aspect.container.AspectFactoryManager;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Visit an expression and push on the bytecode stack the boolean expression that corresponds to the residual
 * part for the target(CALLEE) filtering and cflow / cflowbelow runtime checks
 * <p/>
 * TODO: for now OR / AND / NOT are turned in IAND etc, ie "&" and not "&&" that is more efficient but is using labels.
 * <p/>
 * Note: we have to override here (and maintain) every visit Method that visit a node that appears in an expression
 * (f.e. set , get, etc, but not ASTParameter), since we cannot rely on AND/OR/NOT nodes to push the boolean expressions.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class RuntimeCheckVisitor extends ExpressionVisitor implements Opcodes {
    public static final int NULL_PER_OBJECT_TYPE = -1;
    public static final int PER_THIS_TYPE        = 1;
    public static final int PER_TARGET_TYPE      = 2;

    private MethodVisitor cv;

    private ExpressionInfo m_expressionInfo;

    private CompilerInput m_input;

    private int m_perObjectCheckType = NULL_PER_OBJECT_TYPE;

    private String m_aspectQName;

    /**
     * Create a new visitor given a specific AdviceInfo
     *
     * @param cv                   of the method block we are compiling
     * @param info                 expression info
     * @param input
     * @param perObjectType
     * @param aspectQName
     */
    public RuntimeCheckVisitor(final MethodVisitor cv,
                               final ExpressionInfo info,
                               final CompilerInput input,
                               final int perObjectType,
                               final String aspectQName) {
        super(
                info,
                info.toString(),
                info.getNamespace(),
                info.getExpression().getASTRoot()
        );
        m_expressionInfo = info;
        m_input = input;
        m_perObjectCheckType   = perObjectType;
        m_aspectQName          = aspectQName;

        this.cv = cv;
    }

    /**
     * Push the boolean typed expression on the stack.
     *
     * @param adviceInfo
     */
    public void pushCheckOnStack(AdviceInfo adviceInfo) {
        super.match(adviceInfo.getExpressionContext());

        switch(m_perObjectCheckType) {
            case PER_THIS_TYPE: {
                AbstractJoinPointCompiler.loadCaller(cv, m_input);
                cv.visitMethodInsn(
                        INVOKESTATIC,
                        AspectFactoryManager.getAspectFactoryClassName(
                                adviceInfo.getAspectClassName(),
                                adviceInfo.getAspectQualifiedName()
                        ),
                        TransformationConstants.FACTORY_HASASPECT_METHOD_NAME,
                        TransformationConstants.FACTORY_HASASPECT_PEROBJECT_METHOD_SIGNATURE
                );
                cv.visitInsn(IAND);

                break;
            }

            case PER_TARGET_TYPE: {
                AbstractJoinPointCompiler.loadCallee(cv,m_input);
                cv.visitMethodInsn(
                        INVOKESTATIC,
                        AspectFactoryManager.getAspectFactoryClassName(
                                adviceInfo.getAspectClassName(),
                                adviceInfo.getAspectQualifiedName()
                        ),
                        TransformationConstants.FACTORY_HASASPECT_METHOD_NAME,
                        TransformationConstants.FACTORY_HASASPECT_PEROBJECT_METHOD_SIGNATURE
                );
                cv.visitInsn(IAND);

                break;
            }
        }
    }

    /**
     * Handles OR expression
     *
     * @param node
     * @param data
     * @return
     */
    public Object visit(ASTOr node, Object data) {
        Boolean matchL = (Boolean) node.jjtGetChild(0).jjtAccept(this, data);
        Boolean matchR = (Boolean) node.jjtGetChild(1).jjtAccept(this, data);
        Boolean intermediate = Undeterministic.or(matchL, matchR);
        cv.visitInsn(IOR);
        for (int i = 2; i < node.jjtGetNumChildren(); i++) {
            Boolean matchNext = (Boolean) node.jjtGetChild(i).jjtAccept(this, data);
            intermediate = Undeterministic.or(intermediate, matchNext);
            cv.visitInsn(IOR);
        }
        return intermediate;
    }

    public Object visit(ASTAnd node, Object data) {
        Boolean matchL = (Boolean) node.jjtGetChild(0).jjtAccept(this, data);
        Boolean matchR = (Boolean) node.jjtGetChild(1).jjtAccept(this, data);
        Boolean intermediate = Undeterministic.and(matchL, matchR);
        cv.visitInsn(IAND);
        for (int i = 2; i < node.jjtGetNumChildren(); i++) {
            Boolean matchNext = (Boolean) node.jjtGetChild(i).jjtAccept(this, data);
            intermediate = Undeterministic.and(intermediate, matchNext);
            cv.visitInsn(IAND);
        }
        return intermediate;
    }

    public Object visit(ASTNot node, Object data) {
        Boolean match = (Boolean) node.jjtGetChild(0).jjtAccept(this, data);
        cv.visitInsn(INEG);
        return Undeterministic.not(match);
    }

    public Object visit(ASTTarget node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        if (match != null) {
            push(match);
        } else {
            // runtime check
            String boundedTypeDesc = AsmHelper.convertReflectDescToTypeDesc(node.getBoundedType(m_expressionInfo));
            AbstractJoinPointCompiler.loadCallee(cv, m_input);
            cv.visitTypeInsn(INSTANCEOF, boundedTypeDesc.substring(1, boundedTypeDesc.length() - 1));
        }
        return match;
    }

    public Object visit(ASTThis node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTCflow node, Object data) {
        // runtime check
        String cflowClassName = CflowCompiler.getCflowAspectClassName(node.hashCode());
        cv.visitMethodInsn(
                INVOKESTATIC,
                cflowClassName,
                TransformationConstants.IS_IN_CFLOW_METOD_NAME,
                TransformationConstants.IS_IN_CFLOW_METOD_SIGNATURE
        );
        return (Boolean) super.visit(node, data);
    }

    public Object visit(ASTCflowBelow node, Object data) {
        // runtime check
        //TODO: cflowbelow ID will differ from cflow one.. => not optimized
        String cflowClassName = CflowCompiler.getCflowAspectClassName(node.hashCode());
        cv.visitMethodInsn(
                INVOKESTATIC,
                cflowClassName,
                TransformationConstants.IS_IN_CFLOW_METOD_NAME,
                TransformationConstants.IS_IN_CFLOW_METOD_SIGNATURE
        );
        return (Boolean) super.visit(node, data);
    }

    public Object visit(ASTArgs node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTPointcutReference node, Object data) {
        ExpressionContext context = (ExpressionContext) data;
        ExpressionNamespace namespace = ExpressionNamespace.getNamespace(m_namespace);
        ExpressionVisitor expression = namespace.getExpression(node.getName());

        // build a new RuntimeCheckVisitor to visit the sub expression
        RuntimeCheckVisitor referenced = new RuntimeCheckVisitor(
                cv,
                expression.getExpressionInfo(),
                m_input,
                m_perObjectCheckType,
                m_aspectQName
        );
        return referenced.matchUndeterministic(context);
    }

    public Object visit(ASTExecution node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTCall node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTSet node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTGet node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTHandler node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTStaticInitialization node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTWithin node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTWithinCode node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTHasMethod node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }

    public Object visit(ASTHasField node, Object data) {
        Boolean match = (Boolean) super.visit(node, data);
        push(match);
        return match;
    }


    private void push(Boolean b) {
        if (b == null) {
            throw new Error("attempt to push an undetermined match result");
        } else if (b.booleanValue()) {
            cv.visitInsn(ICONST_1);
        } else {
            cv.visitInsn(ICONST_M1);
        }
    }
}
