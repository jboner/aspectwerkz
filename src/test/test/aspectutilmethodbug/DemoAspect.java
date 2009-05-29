package test.aspectutilmethodbug;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

public class DemoAspect {

    /**
     * @Around execution(public void test.aspectutilmethodbug.Test.invoke())
     */
    public Object trace(final JoinPoint joinPoint) throws Throwable {
        Test.log("before ");
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Test.log("after ");
        return result;
    }

    /**
     * Method that screwed up the advice method indexing.
     */
    private void log(String message) {
    }
}