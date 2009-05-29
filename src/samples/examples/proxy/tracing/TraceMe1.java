package examples.proxy.tracing;

import org.codehaus.aspectwerkz.proxy.Proxy;
import org.codehaus.aspectwerkz.intercept.Advisable;
import org.codehaus.aspectwerkz.intercept.AroundAdvice;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

public class TraceMe1 {

    public void step1() {
        System.out.println("    - TraceMe1.step1");
        step2();
    }

    protected void step2() {
        System.out.println("        - TraceMe1.step2");
        step3();
    }

    void step3() {
        System.out.println("            - TraceMe1.step3");
    }

    public static void main(String[] args) {

        // get a proxy instance of the TraceMe1 class
        // this class will have been weaved with all AW aspects on the classpath
        System.out.println("\nINFO:: ------ get a weaved proxy to the TraceMe1 class -----");
        TraceMe1 traceMe1 = (TraceMe1) Proxy.newInstance(TraceMe1.class, false, true);

        // invoke the method - trigger deployed matched aspects (one around, one before and one after)
        System.out.println("\nINFO:: ------ run method chain with only regular AW aspects -----");
        traceMe1.step1();

        // adding around interceptor using Advisble API
        System.out.println("\nINFO:: ------ adding an around per instance interceptor on the fly -----");
        ((Advisable) traceMe1).aw_addAdvice(
                "execution(* *.step3())",
                new AroundAdvice() {
                    public Object invoke(JoinPoint jp) throws Throwable {
                        System.out.println("Interceptor::ENTERING - step3()");
                        Object result = jp.proceed();
                        System.out.println("Interceptor::EXITING - step3()");
                        return result;
                    }
                }
        );

        // invoking the method chain with the added around interceptor
        System.out.println(
                "\nINFO:: ------ run method chain with the added per instance runtime added around interceptor -----"
        );
        traceMe1.step1();
    }
}
