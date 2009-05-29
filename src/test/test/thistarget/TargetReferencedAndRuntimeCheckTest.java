/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.thistarget;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.definition.Pointcut;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class TargetReferencedAndRuntimeCheckTest extends TestCase {

    private static String s_log = "";

    //--- Target implements 2 interface and a complex rutime check will thus be done
    public static interface I0Target {
        public void call();
    }

    public static interface I1Target {
    }

    public static interface I2Target {
    }

    public static class ImplementsTwoTarget implements I0Target, I1Target, I2Target {
        public void call() {
            log("ImplementsTwoTarget");
        }
    }

    public static class ImplementsOneTarget implements I0Target, I1Target {
        public void call() {
            log("ImplementsOneTarget");
        }
    }

    public static class ImplementsZeroTarget implements I0Target {
        public void call() {
            log("ImplementsZeroTarget");
        }
    }

    //--- Aspect

    public static class Aspect {

        /**
         * @Expression target(myTarget) && call(* test.thistarget.*.call()) && within(test.thistarget.TargetReferencedAndRuntimeCheckTest)
         */
        Pointcut referenceI1Target(I1Target myTarget) {
            return null;
        }

        /**
         * @Expression target(myTarget) && call(* test.thistarget.*.call()) && within(test.thistarget.TargetReferencedAndRuntimeCheckTest)
         */
        Pointcut referenceI2Target(I2Target myTarget) {
            return null;
        }

        /**
         * @Before referenceI1Target(t) && referenceI2Target(t)
         */
        void before(Object t) {// if we don't use Object here but f.e. I1Target, the validation visitor will complain
            log("before_I1TargetAndI2Target");
            ThisTargetAspect.validate(t, I1Target.class);
            ThisTargetAspect.validate(t, I2Target.class);
        }

        /**
         * @Before referenceI1Target(t)
         */
        void before2(I1Target t) {
            log("before_I1Target");
            ThisTargetAspect.validate(t, I1Target.class);
        }
    }

    public void testRuntimeChecks() {
        I0Target i1 = new ImplementsTwoTarget();
        s_log = "";
        i1.call();
        assertEquals("before_I1TargetAndI2Target before_I1Target ImplementsTwoTarget ", s_log);

        I0Target i2 = new ImplementsOneTarget();
        s_log = "";
        i2.call();
        assertEquals("before_I1Target ImplementsOneTarget ", s_log);

        I0Target i3 = new ImplementsZeroTarget();
        s_log = "";
        i3.call();
        assertEquals("ImplementsZeroTarget ", s_log);
    }





    //--- JUnit

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(TargetReferencedAndRuntimeCheckTest.class);
    }

    static void log(String s) {
        s_log += s + " ";
    }


}
