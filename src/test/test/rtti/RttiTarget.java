/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.rtti;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.codehaus.aspectwerkz.joinpoint.Rtti;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class RttiTarget {

    public static StringBuffer LOG = new StringBuffer();

    private static int COUNT = 0;

    private static boolean NESTED = false;

    private final int m_id = ++COUNT;

    public void doSomething(int i) {
        LOG.append(toString()).append(".").append(i).append(" ");
        if (!NESTED) {
            NESTED = true;
            RttiTarget nested = new RttiTarget();
            nested.doSomething(i + 1);
        }
    }

    public String toString() {
        return "Target-" + m_id;
    }

    /**
     * This aspect within the target class allows testing of non side effect at system init time
     */
    public static class TestAspect {

        /**
         * This field of type the target class allows testing of non side effect at system init time
         */
        public static RttiTarget ASPECT_Rtti_TARGET_EXECUTING_INSTANCE;

        /**
         * This method using the type of the target class allows testing of non side effect at system init time
         *
         * NOT SUPPORTED IN 1.0
         */
        //public Target fake(Target target) {return null;}

        /**
         * @param jp
         * @return
         * @throws Throwable
         * @Around execution(* test.rtti.RttiTarget.doSomething(int))
         */
        public Object around(JoinPoint jp) throws Throwable {
            Object target = jp.getTarget();
            int arg0 = ((Integer) (((MethodRtti) jp.getRtti()).getParameterValues()[0])).intValue();
            LOG.append("+").append(target.toString()).append(".").append(arg0).append(" ");

            Object ret = jp.proceed();

            Object targetAfter = jp.getTarget();
            int arg0After = ((Integer) (((MethodRtti) jp.getRtti()).getParameterValues()[0])).intValue();
            LOG.append("-").append(targetAfter.toString()).append(".").append(arg0After).append(" ");

            return ret;
        }
    }

}

