/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class CallerSideTestHelper {

    public CallerSideTestHelper() {
    }

    public CallerSideTestHelper(int i) {
    }

    public void passingParameterToAdviceMethod() {
    }

    public String invokeMemberMethodAround() {
        return "invokeMemberMethodAround";
    }

    public String invokeMemberMethodAround(String a, String b) {
        return "invokeMemberMethodAround";
    }

    public String invokeMemberMethodAround(String a, String b, String c) {
        return "invokeMemberMethodAround";
    }

    public String invokeStaticMethodAround() {
        return "invokeMemberMethodAround";
    }

    public int invokeMemberMethodAroundPrimitiveType() {
        return 2;
    }

    public int invokeStaticMethodAroundPrimitiveType() {
        return 3;
    }

    public void invokeMemberMethodAroundVoidType() {
        return;
    }

    public String invokeMemberMethodPre() {
        return "invokeMemberMethodPre";
    }

    public String invokeMemberMethodPost() {
        return "invokeMemberMethodPost";
    }

    public String invokeMemberMethodPrePost() {
        return "invokeMemberMethodPrePost";
    }

    public static String invokeStaticMethodPre() {
        return "invokeStaticMethodPre";
    }

    public static String invokeStaticMethodPost() {
        return "invokeStaticMethodPost";
    }

    public static String invokeStaticMethodPrePost() {
        return "invokeStaticMethodPrePost";
    }
}