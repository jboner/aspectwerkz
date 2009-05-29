package test.implementsbug;

import junit.framework.TestCase;

public class ImplementsTest extends TestCase {
    public ImplementsTest() {
    }

    public ImplementsTest(String name) {
        super(name);
    }

    public void testInstanceOf() {
        assertTrue(new TestModel() instanceof Subject);
        assertTrue(new TestView() instanceof Observer);
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ImplementsTest.class);
    }
}