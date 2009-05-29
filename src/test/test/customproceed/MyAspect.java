/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.customproceed;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class MyAspect {

    public static interface ProceedWithIntArg extends JoinPoint {
        Object proceed(int i);
    }

    public static interface ProceedWithLongArg extends StaticJoinPoint {
        Object proceed(long l);
    }

    public static interface ProceedWithStringArg extends JoinPoint {
        Object proceed(String s);
    }

    public static interface ProceedWithMiscArgs1 extends StaticJoinPoint {
        Object proceed(long i, String s);
    }

    public static interface ProceedWithMiscArgs2 extends StaticJoinPoint {
        Object proceed(long i, String s, int[][] matrix);
    }

    /**
     * @Around execution(* test.customproceed.CustomProceedTest.setInt(int)) && args(i)
     */
    public Object around1(ProceedWithIntArg jp, int i) {
        CustomProceedTest.log("around1 ");
        CustomProceedTest.log(new Integer(i).toString());
        CustomProceedTest.log(" ");
        return jp.proceed(1);
    }


    /**
     * @Around execution(* test.customproceed.CustomProceedTest.setLong(long)) && args(l)
     */
    public Object around2(ProceedWithLongArg jp, long l) {
        CustomProceedTest.log("around2 ");
        CustomProceedTest.log(new Long(l).toString());
        CustomProceedTest.log(" ");
        return jp.proceed(2);
    }

    /**
     * @Around execution(* test.customproceed.CustomProceedTest.setString(String)) && args(s)
     */
    public Object around3(ProceedWithStringArg jp, String s) {
        CustomProceedTest.log("around3 ");
        CustomProceedTest.log(s);
        CustomProceedTest.log(" ");
        return jp.proceed("gnitset");
    }

    /**
     * Around execution(* test.customproceed.CustomProceedTest.setMisc1(..)) && args(l, s)
     *
     * @Around execution(* test.customproceed.CustomProceedTest.setMisc1(long, String)) && args(l, s)
     */
    public Object around4(ProceedWithMiscArgs1 jp, long l, String s) {
        CustomProceedTest.log("around4 ");
        CustomProceedTest.log(new Long(l).toString());
        CustomProceedTest.log(" ");
        CustomProceedTest.log(s);
        CustomProceedTest.log(" ");
        return jp.proceed(12345, "gnitset");
    }

    /**
     * @Around execution(* test.customproceed.CustomProceedTest.setMisc2(long, String, int[][])) && args(l, s, matrix)
     */
    public Object around5(ProceedWithMiscArgs2 jp, long l, String s, int[][] matrix) {
        CustomProceedTest.log("around5 ");
        CustomProceedTest.log(new Long(l).toString());
        CustomProceedTest.log(" ");
        CustomProceedTest.log(s);
        CustomProceedTest.log(" ");
        CustomProceedTest.log(new Integer(matrix[0][0]).toString());
        CustomProceedTest.log(" ");
        matrix[0][0] = 123;
        return jp.proceed(12345, "gnitset", matrix);
    }
}
