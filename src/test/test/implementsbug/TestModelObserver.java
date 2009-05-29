package test.implementsbug;

/**
 * @Aspect perJVM
 */
public class TestModelObserver {
    /**
     * @Introduce within(test.implementsbug.TestView)
     */
    Observer observer;

    /**
     * @Introduce within(test.implementsbug.TestModel)
     */
    Subject subject;
}