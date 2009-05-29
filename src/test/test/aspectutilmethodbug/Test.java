package test.aspectutilmethodbug;

import junit.framework.TestCase;

public class Test extends TestCase {
    private static String s_logString;

    public void test1() {
        s_logString = "";
        invoke();
        assertEquals("before invoke after ", s_logString);
    }

    public void invoke() {
        Test.log("invoke ");
    }

    public static void log(String msg) {
        s_logString += msg;
    }

    public static void main(String[] args) {
        new Test().test1();
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(Test.class);
    }
}
