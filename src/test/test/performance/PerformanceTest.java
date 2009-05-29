/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.performance;

import junit.framework.TestCase;

/**
 * A so far VERY limited bench. <p/>Only tests the overhead of one around advice and one introduced
 * method.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @TODO: extends this test case to be more interesting or replace with a real bench
 * @TODO: should add some more around advice, since JIT really shines when we have advice chains
 */
public class PerformanceTest extends TestCase {
    private boolean m_printInfo = true;

    private int m_numberOfInvocations = 100000000;

    public PerformanceTest(String name) {
        super(name);
    }

    public void testNonAdvisedMethodPerformance() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            nonAdvisedMethod();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocationNormalMethod = time / (double) m_numberOfInvocations;
        if (m_printInfo) {
            System.out.println("\nNon advised method: " + timePerInvocationNormalMethod);
        }
    }

    public void testAroundAdvicePerJVMPerformance() {
        methodAdvisedMethodPerJVM();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            nonAdvisedMethod();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocationNormalMethod = time / (double) m_numberOfInvocations;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            methodAdvisedMethodPerJVM();
        }
        time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_numberOfInvocations;
        double overhead = timePerInvocation - timePerInvocationNormalMethod;
        if (m_printInfo) {
            System.out.println("\nPER_JVM advice: " + overhead);
        }
    }

    public void testAroundAdvicePerClassPerformance() {
        methodAdvisedMethodPerClass();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            nonAdvisedMethod();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocationNormalMethod = time / (double) m_numberOfInvocations;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            methodAdvisedMethodPerClass();
        }
        time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_numberOfInvocations;
        double overhead = timePerInvocation - timePerInvocationNormalMethod;
        if (m_printInfo) {
            System.out.println("\nPER_CLASS advice: " + overhead);
        }
    }

    public void testAroundAdvicePerInstancePerformance() {
        methodAdvisedMethodPerInstance();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            nonAdvisedMethod();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocationNormalMethod = time / (double) m_numberOfInvocations;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            methodAdvisedMethodPerInstance();
        }
        time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_numberOfInvocations;
        double overhead = timePerInvocation - timePerInvocationNormalMethod;
        if (m_printInfo) {
            System.out.println("\nPER_INSTANCE advice: " + overhead);
        }
    }

    public void testAroundAdvicePerThreadPerformance() {
        methodAdvisedMethodPerThread();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            nonAdvisedMethod();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocationNormalMethod = time / (double) m_numberOfInvocations;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < m_numberOfInvocations; i++) {
            methodAdvisedMethodPerThread();
        }
        time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_numberOfInvocations;
        double overhead = timePerInvocation - timePerInvocationNormalMethod;
        if (m_printInfo) {
            System.out.println("\nPER_THREAD advice: " + overhead);
        }
    }

    public void testIntroductionPerJVMPerformance() {
        long startTime = System.currentTimeMillis();
        PerJVM perJVM = (PerJVM) this;
        for (int i = 0; i < m_numberOfInvocations; i++) {
            perJVM.runPerJVM();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_numberOfInvocations;
        if (m_printInfo) {
            System.out.println("\nPER_JVM introduction: " + timePerInvocation);
        }
    }

    public void testIntroductionPerClassPerformance() {
        long startTime = System.currentTimeMillis();
        PerClass perClass = (PerClass) this;
        for (int i = 0; i < m_numberOfInvocations; i++) {
            perClass.runPerClass();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_numberOfInvocations;
        if (m_printInfo) {
            System.out.println("\nPER_CLASS introduction: " + timePerInvocation);
        }
    }

    public void testIntroductionPerInstancePerformance() {
        long startTime = System.currentTimeMillis();
        PerInstance perInstance = (PerInstance) this;
        for (int i = 0; i < m_numberOfInvocations; i++) {
            perInstance.runPerInstance();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_numberOfInvocations;
        if (m_printInfo) {
            System.out.println("\nPER_INSTANCE introduction: " + timePerInvocation);
        }
    }

    public void testIntroductionPerThreadPerformance() {
        long startTime = System.currentTimeMillis();
        PerThread perThread = (PerThread) this;
        for (int i = 0; i < m_numberOfInvocations; i++) {
            perThread.runPerThread();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_numberOfInvocations;
        if (m_printInfo) {
            System.out.println("\nPER_THREAD introduction: " + timePerInvocation);
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(PerformanceTest.class);
    }

    // ==== methods to test ====
    public void nonAdvisedMethod() {
    }

    public void preAdvisedMethodPerJVM() {
    }

    public void preAdvisedMethodPerClass() {
    }

    public void preAdvisedMethodPerInstance() {
    }

    public void preAdvisedMethodPerThread() {
    }

    public void methodAdvisedMethodPerJVM() {
    }

    public void methodAdvisedMethodPerClass() {
    }

    public void methodAdvisedMethodPerInstance() {
    }

    public void methodAdvisedMethodPerThread() {
    }

    public void methodAdvisedMethodNoAdvice() {
    }
}