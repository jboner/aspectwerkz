package test.polymorphic;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.codehaus.aspectwerkz.joinpoint.ConstructorRtti;
import org.codehaus.aspectwerkz.joinpoint.Rtti;
import junit.framework.TestCase;

public class PolymorphicTest extends TestCase {

    public static StringBuffer LOG = new StringBuffer("");

    public void testPolymorphicCallJoinPoint() {
        // see AW-228
        LOG = new StringBuffer("");
        SubClass child = new SubClass();
        child.methodTest();
        assertEquals("call child parent parent 1 ", LOG.toString());

        LOG = new StringBuffer("");
        SuperClass parent = new SuperClass();
        parent.methodTest();
        assertEquals("call parent ", LOG.toString());
    }

    public void testCtorCall() {
        LOG = new StringBuffer("");
        SubClass child = new SubClass("s");
        assertEquals("callctor parent s child s ", LOG.toString());

        LOG = new StringBuffer("");
        SuperClass parent = new SuperClass("ss");
        assertEquals("callctor parent ss ", LOG.toString());
    }

    public void testCtorExecution() {
        LOG = new StringBuffer("");
        SubClass child = new SubClass(0);
        assertEquals("exector parent 0 exector child 0 ", LOG.toString());

        LOG = new StringBuffer("");
        SuperClass parent = new SuperClass(1);
        assertEquals("exector parent 1 ", LOG.toString());
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(PolymorphicTest.class);
    }


    //---- Aspect

    public static class TestAspect {

        public void method1Advise(JoinPoint joinPoint) {
            MethodRtti mrtti = (MethodRtti) joinPoint.getRtti();
            LOG.append("call ");
        }

        public void ctor1Advise(JoinPoint joinPoint) {
            ConstructorRtti crtti = (ConstructorRtti) joinPoint.getRtti();
            LOG.append("exector ");
        }

        public void ctor2Advise(JoinPoint joinPoint) {
            ConstructorRtti crtti = (ConstructorRtti) joinPoint.getRtti();
            LOG.append("callctor ");
        }
    }
}