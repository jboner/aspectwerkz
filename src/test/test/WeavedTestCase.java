/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;
import org.codehaus.aspectwerkz.hook.impl.WeavingClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Transparently runs TestCase with an embedded online mode Write a JUnit test case and extends
 * WeaverTestCase.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class WeavedTestCase extends TestCase {
    /**
     * the test runner that runs the test thru reflection in a weaving ClassLoader
     */
    private static WeaverTestRunner s_runner = new WeaverTestRunner();

    public WeavedTestCase() {
        super();
    }

    public WeavedTestCase(String name) {
        super(name);
    }

    /**
     * Overrides JUnit runBare() to run thru the weaverTestRunner This allow WeaverTestCase to be
     * regular TestCase
     *
     * @throws Throwable
     */
    public void runBare() throws Throwable {
        s_runner.runTest(this.getClass().getName(), getName());
    }

    /**
     * Callback the regulare JUnit runBare()
     *
     * @throws Throwable
     */
    public void runBareAfterWeaving() throws Throwable {
        super.runBare();
    }

    /**
     * Allow to run WeaverTestCase thru a weaving ClassLoader
     */
    public static class WeaverTestRunner {
        /**
         * Weaving classloader
         */
        private WeavingClassLoader cl;

        /**
         * Build weavin classloader with system class path and ext. classloader as parent
         */
        public WeaverTestRunner() {
            try {
                String path = System.getProperty("java.class.path");
                ArrayList paths = new ArrayList();
                StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
                while (st.hasMoreTokens()) {
                    paths.add((new File(st.nextToken())).getCanonicalFile().toURL());
                }
                cl = new WeavingClassLoader(
                        (URL[]) paths.toArray(new URL[]{}), ClassLoader
                                                            .getSystemClassLoader().getParent()
                );
            } catch (IOException e) {
                throw new WrappedRuntimeException(e);
            }
        }

        /**
         * Runs a single test (testXX) Takes care of not using the weaving class loader is online
         * mode or weavingClassLoader.main() is already used (might fail under JRockit MAPI)
         *
         * @param testClassName  test class
         * @param testMethodName test method
         * @throws Throwable
         */
        public void runTest(String testClassName, String testMethodName) throws Throwable {
            // skip test embedded weaving if online mode / weavingClassLoader.main() is already used
            if ((cl.getClass().getClassLoader() == null)
                || (cl.getClass().getClassLoader().getClass().getName()
                    .indexOf("hook.impl.Weaving") > 0)) {
                ;
            } else {
                Thread.currentThread().setContextClassLoader(cl); // needed for Aspect loading
            }
            Class testClass = Class.forName(
                    testClassName, true, Thread.currentThread()
                                         .getContextClassLoader()
            );

            //)cl.loadClass(testClassName);
            Constructor ctor = null;
            Object testInstance = null;
            try {
                // new junit style
                ctor = testClass.getConstructor(new Class[]{});
                testInstance = ctor.newInstance(new Object[]{});
                Method setNameMethod = testClass.getMethod(
                        "setExpression", new Class[]{
                            String.class
                        }
                );
                setNameMethod.invoke(
                        testInstance, new Object[]{
                            testMethodName
                        }
                );
            } catch (NoSuchMethodException e) {
                ctor = testClass.getConstructor(
                        new Class[]{
                            String.class
                        }
                );
                testInstance = ctor.newInstance(
                        new Object[]{
                            testMethodName
                        }
                );
            }
            Method runAfterWeavingMethod = testClass.getMethod(
                    "runBareAfterWeaving",
                    new Class[]{}
            );
            runAfterWeavingMethod.invoke(testInstance, new Object[]{});
        }
    }
}