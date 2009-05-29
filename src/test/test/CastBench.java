/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class CastBench implements Cast {
    private static final int NR_INVOCATIONS = 1000000000;

    public void invoke() {
    }

    public void invokeInterface() {
    }

    public static void main(String[] args) {
        CastBench bench = new CastBench();
        for (int i = 0; i < NR_INVOCATIONS; i++) {
            bench.invoke();
        }

        benchRegularInvoke(bench);
        benchInterfaceInvoke(bench);
        benchCastInvoke(bench);
    }

    private static void benchCastInvoke(CastBench bench) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < NR_INVOCATIONS; i++) {
            ((Cast) bench).invokeInterface();
        }
        long end = System.currentTimeMillis() - start;
        double time = end / (double) NR_INVOCATIONS;
        System.out.println("cast invoke = " + time);
    }

    private static void benchInterfaceInvoke(Cast bench) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < NR_INVOCATIONS; i++) {
            bench.invokeInterface();
        }
        long end = System.currentTimeMillis() - start;
        double time = end / (double) NR_INVOCATIONS;
        System.out.println("interface invoke = " + time);
    }

    private static void benchRegularInvoke(CastBench bench) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < NR_INVOCATIONS; i++) {
            bench.invoke();
        }
        long end = System.currentTimeMillis() - start;
        double time = end / (double) NR_INVOCATIONS;
        System.out.println("regular invoke = " + time);
    }
}
