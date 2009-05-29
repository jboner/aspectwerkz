package examples.proxy.tracing;

import org.codehaus.aspectwerkz.proxy.Proxy;

public class TraceMe2 {

    public TraceMe2(String name) {
    }

    void step1() {
        step2();
    }

    public void step2() {
        step3();
    }

    protected void step3() {
    }

    public static void main(String[] args) {
        TraceMe2 traceMe2 = (TraceMe2) Proxy.newInstance(
                TraceMe2.class, new Class[]{String.class}, new String[]{"foo"}
        );
        traceMe2.step1();

        traceMe2 = (TraceMe2) Proxy.newInstance(TraceMe2.class, new Class[]{String.class}, new String[]{"foo"});
        traceMe2.step1();
    }
}
