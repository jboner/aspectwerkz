/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.mixin.perclass;

import java.io.Serializable;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class IntroductionTestAspect {
    /**
     * @Introduce within(test.mixin.perclass.ToBeIntroduced)
     */
    Serializable serializable;
    /**
     * Introduce within(test.mixin.perclass.ToBeIntroduced)
     */
    IntroductionsClone clone;
}