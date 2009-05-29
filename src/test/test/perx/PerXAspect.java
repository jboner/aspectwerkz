/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.perx;

import org.codehaus.aspectwerkz.AspectContext;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;
import org.codehaus.aspectwerkz.joinpoint.management.JoinPointType;


/**
 * Aspect with perThis/perTarget deployment scope.
 * 
 * @author <a href='mailto:the_mindstorm@evolva.ro'>Alexandru Popescu</a>
 */
public class PerXAspect {
	private String m_logPrefix;
	
	public PerXAspect(AspectContext aspectContext) {
		m_logPrefix = aspectContext.getParameter("logPrefix");
	}
	
	public void beforeAll(StaticJoinPoint sjp) {
		AssociationScopeTest.SCOPE_LOG += m_logPrefix + " ";
        AssociationScopeTest.JOINPOINTTYPES.add(sjp.getType().toString());
	}
    
    private void printStaticJoinPoint(final StaticJoinPoint sjp) {
        StringBuffer buf = new StringBuffer("-----\n");
        
        buf.append("IN:").append(m_logPrefix).append("\n");
        
        JoinPointType jpt = sjp.getType();
        
        buf.append(jpt.toString()).append("(");
        switch (jpt.hashCode()) {
            case JoinPointType.CONSTRUCTOR_CALL_INT:
                buf.append(sjp.getCalleeClass().getName())
                        .append(".<init>)");
                break;
            case JoinPointType.CONSTRUCTOR_EXECUTION_INT:
                buf.append(sjp.getCallerClass().getName())
                        .append(".<init>)");
                break;
            case JoinPointType.METHOD_CALL_INT:
                buf.append(sjp.getCalleeClass().getName())
                        .append(".")
                        .append(sjp.getSignature().getName())
                        .append("())");
                break;
            case JoinPointType.METHOD_EXECUTION_INT:
                buf.append(sjp.getCallerClass().getName())
                        .append(".")
                        .append(sjp.getSignature().getName())
                        .append("())");
                break;
            default:
                buf.append(sjp.getSignature().getName())
                    .append("())");
        }
        
        buf.append(")");
        buf.append("[CALLERCLASS:").append(sjp.getCallerClass().getName()).append(",");
        buf.append("CALLEECLASS:").append(sjp.getCalleeClass().getName()).append("]");
        System.out.println(buf.toString());
    }
}
