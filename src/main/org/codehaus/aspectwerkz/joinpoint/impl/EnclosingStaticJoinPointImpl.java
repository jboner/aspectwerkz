/*
 * $Id: EnclosingStaticJoinPointImpl.java,v 1.2 2005/01/31 14:34:24 avasseur Exp $
 * $Date: 2005/01/31 14:34:24 $
 */
package org.codehaus.aspectwerkz.joinpoint.impl;

import org.codehaus.aspectwerkz.joinpoint.EnclosingStaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.Signature;
import org.codehaus.aspectwerkz.joinpoint.management.JoinPointType;


/**
 * Sole implementation of {@link org.codehaus.aspectwerkz.joinpoint.EnclosingStaticJoinPoint}.
 * It provides access to the enclosing {@link org.codehaus.aspectwerkz.joinpoint.Signature}
 * of the joinpoint.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro">Alex Popescu</a>
 * @version $Revision: 1.2 $
 */
public class EnclosingStaticJoinPointImpl implements EnclosingStaticJoinPoint {
	private Signature m_signature;
	private JoinPointType m_joinPointType;
	
	public EnclosingStaticJoinPointImpl(Signature signature, JoinPointType jpType) {
		m_signature = signature;
		m_joinPointType = jpType;
	}
	
	/**
	 * Retrieve the {@link Signature} of the enclosing join point.
	 * 
	 * @return a {@link Signature}
	 */
	public Signature getSignature() {
		return m_signature;
	}

	/**
	 * Return a join point type corresponding to the enclosing join point.
	 * 
	 * @return one of {@link JoinPointType#CONSTRUCTOR_EXECUTION} or
	 * {@link JoinPointType#METHOD_EXECUTION} or {@link JoinPointType#STATIC_INITIALIZATION}.
	 */
	public JoinPointType getType() {
		return m_joinPointType;
	}
}
