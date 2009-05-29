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

import org.codehaus.aspectwerkz.transform.TransformationUtil;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;

import java.lang.reflect.Modifier;

/**
 * A compiler that compiles/generates a class that represents a specific join
 * point, a class which invokes the advices and the target join point
 * statically.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu </a>
 * @version $Revision: 1.3 $
 */
public class StaticInitializationJoinPointCompiler extends AbstractJoinPointCompiler {
	private static final Type[] ARG_TYPES = new Type[0];
	
	/**
	 * Creates a new join point compiler instance.
	 * 
	 * @param model
	 */
	StaticInitializationJoinPointCompiler(final CompilationInfo.Model model) {
		super(model);
	}

	/**
	 * Creates join point specific fields.
	 */
	protected void createJoinPointSpecificFields() {
	    m_fieldNames = new String[0];
	    
		m_cw.visitField(ACC_PRIVATE + ACC_STATIC,
						SIGNATURE_FIELD_NAME,
						STATICINITIALIZATION_SIGNATURE_IMPL_CLASS_SIGNATURE,
						null,
						null);
	}

	/**
	 * Creates the signature for the join point.
	 * 
	 * @param cv
	 */
	protected void createSignature(final MethodVisitor cv) {
		cv.visitFieldInsn(GETSTATIC,
		                  m_joinPointClassName,
		                  TARGET_CLASS_FIELD_NAME_IN_JP,
		                  CLASS_CLASS_SIGNATURE);
		cv.visitMethodInsn(INVOKESTATIC,
		                   SIGNATURE_FACTORY_CLASS,
		                   NEW_STATICINITIALIZATION_SIGNATURE_METHOD_NAME,
		                   NEW_STATICINITIALIZATION_SIGNATURE_METHOD_SIGNATURE);
		cv.visitFieldInsn(PUTSTATIC,
		                  m_joinPointClassName,
		                  SIGNATURE_FIELD_NAME,
		                  STATICINITIALIZATION_SIGNATURE_IMPL_CLASS_SIGNATURE);
	}

	/**
	 * Optimized implementation that does not retrieve the parameters from the
	 * join point instance but is passed directly to the method from the input
	 * parameters in the 'invoke' method. Can only be used if no around advice
	 * exists.
	 * 
	 * @param cv
	 * @param input
	 */
	protected void createInlinedJoinPointInvocation(final MethodVisitor cv,
                                                    final CompilerInput input) {
		String joinPointName = TransformationUtil.getPrefixedOriginalClinitName(m_calleeClassName);
		
		cv.visitMethodInsn(INVOKESTATIC, m_calleeClassName, joinPointName, m_calleeMemberDesc);
	}

	/**
	 * Creates a call to the target join point, the parameter(s) to the join
	 * point are retrieved from the invocation local join point instance.
	 * 
	 * @param cv
	 */
	protected void createJoinPointInvocation(final MethodVisitor cv) {

		// load the target instance member field unless calleeMember is static
		String joinPointName = TransformationUtil.getPrefixedOriginalClinitName(m_calleeClassName);
		cv.visitMethodInsn(INVOKESTATIC, m_calleeClassName, joinPointName, m_calleeMemberDesc);
	}

	/**
	 * Returns the join points return type.
	 * 
	 * @return
	 */
	protected Type getJoinPointReturnType() {
		return Type.VOID_TYPE;
	}

	/**
	 * Returns the join points argument type(s).
	 * 
	 * @return
	 */
	protected Type[] getJoinPointArgumentTypes() {
		return ARG_TYPES;
	}

	/**
	 * Creates the getRtti method
	 */
	protected void createGetRttiMethod() {
		MethodVisitor cv = m_cw.visitMethod(ACC_PUBLIC,
		                                  GET_RTTI_METHOD_NAME,
		                                  GET_RTTI_METHOD_SIGNATURE,
		                                  null,
		                                  null
		);

		// new StaticInitializationRttiImpl
		cv.visitTypeInsn(NEW, STATICINITIALIZATION_RTTI_IMPL_CLASS_NAME);
		cv.visitInsn(DUP);
		cv.visitFieldInsn(GETSTATIC,
		                  m_joinPointClassName,
		                  SIGNATURE_FIELD_NAME,
		                  STATICINITIALIZATION_SIGNATURE_IMPL_CLASS_SIGNATURE);
        cv.visitMethodInsn(INVOKESPECIAL, 
                           STATICINITIALIZATION_RTTI_IMPL_CLASS_NAME, 
                           INIT_METHOD_NAME,
                           STATICINITIALIZATION_RTTI_IMPL_INIT_SIGNATURE
        );
        
		cv.visitInsn(ARETURN);
		cv.visitMaxs(0, 0);
	}

	/**
	 * Creates the getSignature method.
	 */
	protected void createGetSignatureMethod() {
		MethodVisitor cv = m_cw.visitMethod(ACC_PUBLIC,
		                                  GET_SIGNATURE_METHOD_NAME,
		                                  GET_SIGNATURE_METHOD_SIGNATURE,
		                                  null,
		                                  null);

		cv.visitFieldInsn(GETSTATIC,
		                  m_joinPointClassName,
		                  SIGNATURE_FIELD_NAME,
		                  STATICINITIALIZATION_SIGNATURE_IMPL_CLASS_SIGNATURE);
		cv.visitInsn(ARETURN);
		cv.visitMaxs(0, 0);
	}
}