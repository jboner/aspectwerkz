/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.joinpoint.impl;

import org.codehaus.aspectwerkz.joinpoint.Rtti;


/**
 * Implementation of static initialization RTTI.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 */
public class StaticInitializationRttiImpl implements Rtti {
	private final StaticInitializerSignatureImpl m_signature;
	
	/**
	 * Creates a new staticinitialization RTTI
	 * 
	 * @param signature the underlying <CODE>StaticInitializerSignatureImpl</CODE>
	 */
	public StaticInitializationRttiImpl(final StaticInitializerSignatureImpl signature) {
		m_signature = signature;
	}
	
	/**
	 * @see org.codehaus.aspectwerkz.joinpoint.Rtti#getName()
	 */
	public String getName() {
		return m_signature.getName();
	}

	/**
	 * @see org.codehaus.aspectwerkz.joinpoint.Rtti#getTarget()
	 */
	public Object getTarget() {
		return null;
	}

	/**
	 * @see org.codehaus.aspectwerkz.joinpoint.Rtti#getThis()
	 */
	public Object getThis() {
		return null;
	}

	/**
	 * @see org.codehaus.aspectwerkz.joinpoint.Rtti#getDeclaringType()
	 */
	public Class getDeclaringType() {
		return m_signature.getDeclaringType();
	}

	/**
	 * @see org.codehaus.aspectwerkz.joinpoint.Rtti#getModifiers()
	 */
	public int getModifiers() {
		return m_signature.getModifiers();
	}

	/**
	 * @see org.codehaus.aspectwerkz.joinpoint.Rtti#cloneFor(java.lang.Object, java.lang.Object)
	 */
	public Rtti cloneFor(Object targetInstance, Object thisInstance) {
		return new StaticInitializationRttiImpl(m_signature);
	}

}
