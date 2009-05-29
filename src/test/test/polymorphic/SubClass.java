package test.polymorphic;

public class SubClass extends SuperClass {

    public SubClass() {
    }

    public SubClass(int i) {
        super(i);
        PolymorphicTest.LOG.append("child " + i).append(" ");
    }

    public SubClass(String s) {
        super(s);
        PolymorphicTest.LOG.append("child " + s).append(" ");
    }

    public synchronized void methodTest() {
        PolymorphicTest.LOG.append("child ");
        super.methodTest();//this call is NOT a joinpoint
        super.methodTest(1);//neither this one
        //super.some();//this one neither
    }


}