/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.logging;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ArgLoggingTarget {

    private int m_counter1;

    private int m_counter2;

    public int getCounter() {
        System.out.println("getCounter before");
        return m_counter1;
    }

    public void increment() {
        System.out.println("increment before = " + m_counter2);
        m_counter2 = m_counter2 + 1;
        System.out.println("increment after = " + m_counter2);
    }

    /**
     * @examples.annotation.Annotation
     */
    public int toLog_1(int typeMatch, String s, int i) {
        System.out.println("toLog_1");
        toLog_2(0, "b", 2);
        return 0;
    }

    public java.lang.String[] toLog_2(int typeMatch, String s, int i) {
        System.out.println("toLog_2");
        int result = toLog_3(0, new String[]{"c"});
        return null;
    }

    private static int toLog_3(int typeMatch, String[] sarr) {
        System.out.println("toLog_2");
        return -1;
    }

    public static void main(String args[]) throws Throwable {
        new Runner().run();
    }
}

class Runner {

    public void run() {
        ArgLoggingTarget target = new ArgLoggingTarget();
        doRun(target);
    }

    public void doRun(ArgLoggingTarget target) {
        target.toLog_1(0, "a", 1);
        int counter1 = target.getCounter();
        System.out.println("getCounter after = " + counter1);
        target.increment();
    }
}