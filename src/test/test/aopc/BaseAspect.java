/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aopc;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.AspectContext;
import org.codehaus.aspectwerkz.AspectContext;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class BaseAspect {
    private AspectContext m_cci;

    public BaseAspect(AspectContext cci) {
        m_cci = cci;
    }

    public Object logAround(JoinPoint jp) throws Throwable {
        String vfqn = m_cci.getUuid() + "/" + m_cci.getName();
        ((Callable) jp.getTarget()).log(vfqn + ".beforeAround ");
        Object result = jp.proceed();
        ((Callable) jp.getTarget()).log(vfqn + ".afterAround ");
        return result;
    }
}