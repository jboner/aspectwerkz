package examples.introduction;

import org.codehaus.aspectwerkz.AspectContext;

import java.io.Serializable;

/**
 * @Aspect perClass
 */
public class IntroductionAspect extends AbstractIntroductionAspect {

    /**
     * @Introduce within(examples.introduction.Target)
     */
    public Serializable serializable;

    /**
     * @Mixin(pointcut="within(@examples.annotation.Annotation *..*)", deploymentModel="perInstance")
     */
    public static class MyConcreteImpl extends MyImpl {

        /**
         * The instance we are introduced to since we are perInstance
         */
        private final Object m_target;

        /**
         * @param target
         */
        public MyConcreteImpl(final Object target) {
            m_target = target;
            System.out.println("--Accessing mixin target instance from the mixin <init>...");
            System.out.println("-- I am introduced to " + target);
            sayHello2();
            System.out.println("--..<init> done");
        }

        public String sayHello2() {
            return "Hello World! Hello World!";
        }
    }
}