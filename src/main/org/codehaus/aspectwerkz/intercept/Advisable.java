/**************************************************************************************
 * Copyright (c) Jonas BonŽr, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.intercept;

/**
 * The advisable interface is introduced to target class made advisable.
 * </p>
 * Note: user should ensure that the target class has no user defined method named aw_addAdvice
 * and aw_removeAdvice. Other methods are made synthetic and thus will not lead to name clashes.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public interface Advisable {

    /**
     * @param pointcut
     * @param advice
     */
    void aw_addAdvice(String pointcut, Advice advice);

    /**
     * @param pointcut
     * @param adviceClass
     */
    void aw_removeAdvice(String pointcut, Class adviceClass);

    /**
     * @param joinPointIndex
     * @return
     */
    AroundAdvice[] aw$getAroundAdvice(final int joinPointIndex);

    /**
     * @param joinPointIndex
     * @return
     */
    BeforeAdvice[] aw$getBeforeAdvice(final int joinPointIndex);

    /**
     * @param joinPointIndex
     * @return
     */
    AfterAdvice[] aw$getAfterAdvice(final int joinPointIndex);

    /**
     * @param joinPointIndex
     * @return
     */
    AfterReturningAdvice[] aw$getAfterReturningAdvice(final int joinPointIndex);

    /**
     * @param joinPointIndex
     * @return
     */
    AfterThrowingAdvice[] aw$getAfterThrowingAdvice(final int joinPointIndex);
}
