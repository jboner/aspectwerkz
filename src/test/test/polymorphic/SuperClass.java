package test.polymorphic;

public class SuperClass {

    public SuperClass() {

    }

    public SuperClass(int i) {
        PolymorphicTest.LOG.append("parent " + i).append(" ");
    }

    public SuperClass(String s) {
        PolymorphicTest.LOG.append("parent " + s).append(" ");
    }

    public synchronized void methodTest() {
        PolymorphicTest.LOG.append("parent ");
    }

    public synchronized void methodTest(int i) {
        PolymorphicTest.LOG.append("parent " + i + " ");
    }
}