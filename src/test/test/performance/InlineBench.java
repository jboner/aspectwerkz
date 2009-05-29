package test.performance;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

public class InlineBench {
    private int m_nrInvocations = 1000000000;

    public void run() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < m_nrInvocations; i++) {
            notAdvised();
        }
        long time = System.currentTimeMillis() - startTime;
        double timePerInvocationNormalMethod = time / (double) m_nrInvocations;

        toAdvise();

        startTime = System.currentTimeMillis();
        for (int i = 0; i < m_nrInvocations; i++) {
            toAdvise();
        }
        time = System.currentTimeMillis() - startTime;
        double timePerInvocation = time / (double) m_nrInvocations;
        double overhead = timePerInvocation - timePerInvocationNormalMethod;
        System.out.println("\nOverhead: " + overhead);
    }

    public static void main(String[] args) {
        new InlineBench().run();
    }

    public void toAdvise() {
    }

    public void notAdvised() {
    }

    public static class Aspect {
        public void before(JoinPoint jp) throws Throwable {
        }
    }
}