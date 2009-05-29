/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.expression;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @Serializable
 */
public final class Target {
    public static int modifier2 = 0;

    transient static final protected int modifier3 = 0;

    // ============ field modifier test =============
    int modifier1 = 0;

    // ============ field type test =============
    int type1;

    int[][] type2;

    String type3;

    String[] type4;

    // ============ field name test =============
    int __field$Name1;

    // ============ field attribute test =============

    /**
     * @ReadOnly
     */
    int attribute1;

    // ============ method modifiers test =============
    void modifiers1() {
    }

    public static void modifiers2() {
    }

    protected native static final synchronized void modifiers3();

    private void modifiers4() {
    }

    // ============ method parameters test =============
    void parameters1() {
    }

    void parameters2(int i, float f, byte b) {
    }

    void parameters2bis(int i, short s, byte b, int ibis, float fbis, byte bbis) {
    }

    void parameters3(String s, java.lang.StringBuffer sb, java.lang.String s2) {
    }

    void parameters4(Object[] o) {
    }

    void parameters5(int[][] i) {
    }

    // ============ method return type test =============
    void returnType1() {
    }

    int returnType2() {
        return -1;
    }

    String returnType3() {
        return "test";
    }

    java.lang.Process returnType4() {
        return null;
    }

    float[][] returnType5() {
        return null;
    }

    // ============ method name test =============
    void __method$Name1() {
    }

    // ============ method attribute test =============

    /**
     * @Requires
     */
    public void attributes1() {
    }

    /**
     * @Requires
     */
    public Target() {

    }

    // ============ ctor test =============
    private Target(int i) {

    }


}