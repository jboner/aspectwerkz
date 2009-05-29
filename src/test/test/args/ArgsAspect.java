/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.args;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import test.Loggable;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class ArgsAspect {

    //-- Method execution pointcuts with args

    /**
     * @Expression within(test.args.ArgsAdviceTest)
     */
    Pointcut in_scope;

    /**
     * @Expression in_scope && execution(* matchAll(..)) && args(String, String, long)
     */
    Pointcut pc_matchAll;

    /**
     * @Expression in_scope && execution(* matchAllWithWildcard(..)) && args(..)
     */
    Pointcut pc_matchAllWithWildcard;

    /**
     * @Expression in_scope && execution(* getFirst(..)) && args(s, ..)
     */
    void pc_getFirst(String s) {
        ;
    }// here we use "return void" style

    /**
     * @Expression in_scope && execution(* changeArg(..)) && args(String, s, long)
     */
    Pointcut pc_changeArg(StringBuffer s) {
        return null;
    }// here we use "return null" style

    /**
     * @Expression in_scope && execution(* orderChangedInPointcutSignature(..)) && args(s0, s1, long)
     */
    Pointcut pc_orderChangedInPointcutSignature(String s1, String s0) {
        return null;
    }

    /**
     * @Expression in_scope && execution(* orderChangedInAdviceSignature(..)) && args(s0, s1, long)
     */
    Pointcut pc_orderChangedInAdviceSignature(String s0, String s1) {
        return null;
    }

    /**
     * @Expression in_scope && execution(* orderChangedInPointcutAndAdviceSignature(..)) && args(s0, s1, long)
     */
    Pointcut pc_orderChangedInPointcutAndAdviceSignature(String s1, String s0) {
        return null;
    }

    /**
     * @Before in_scope && execution(* singleAndDotDot(..)) && args(i)
     */
    public void singleAndDotDot(JoinPoint joinPoint, int i) {
        ((Loggable) joinPoint.getTarget()).log("before " + i + " ");
    }

    /**
     * @Before in_scope && execution(* withArray(..)) && args(l, s, matrix)
     */
    public void withArray(JoinPoint joinPoint, long l, String s, int[][] matrix) {
        String iis = "";
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                iis += ""+matrix[i][j]+"-";
            }
        }
        ((Loggable) joinPoint.getTarget()).log("before " + l + " " + s + " " + iis + " ");
    }

    /**
     * @Before pc_matchAll || pc_matchAllWithWildcard
     */
    public void matchAllBefore(JoinPoint joinPoint) {
        ((Loggable) joinPoint.getTarget()).log("before ");
    }

    /**
     * @After pc_matchAll || pc_matchAllWithWildcard
     */
    public void matchAllAfter(JoinPoint joinPoint) {
        ((Loggable) joinPoint.getTarget()).log("after ");
    }

    /**
     * @Around pc_matchAll || pc_matchAllWithWildcard
     */
    public Object matchAllAround(JoinPoint joinPoint) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 ");
        Object res = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 ");
        return res;
    }


    /**
     * @Before pc_getFirst(as)
     */
    public void getFirstBefore(JoinPoint joinPoint, String as) {
        ((Loggable) joinPoint.getTarget()).log("before " + as + " ");
    }

    /**
     * @After pc_getFirst(as)
     */
    public void getFirstAfter(String as, JoinPoint joinPoint) {//here we use some fancy order in the signature
        ((Loggable) joinPoint.getTarget()).log("after " + as + " ");
    }

    /**
     * @Around pc_getFirst(as)
     */
    public Object getFirstAround(JoinPoint joinPoint, String as) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 " + as + " ");
        Object res = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 " + as + " ");
        return res;
    }

    /**
     * @Before in_scope && execution(* getFirstAnonymous(..)) && args(as,String,..)
     */
    public void getFirstAnonymousBefore(JoinPoint joinPoint, String as) {
        ((Loggable) joinPoint.getTarget()).log("before " + as + " ");
    }

    /**
     * @After in_scope && execution(* getFirstAnonymous(..)) && args(as, String, long)
     */
    public void getFirstAnonymousAfter(String as, JoinPoint joinPoint) {
        ((Loggable) joinPoint.getTarget()).log("after " + as + " ");
    }

    /**
     * @Around in_scope && execution(* getFirstAnonymous(..)) && args(as,..)
     */
    public Object getFirstAnonymousAround(JoinPoint joinPoint, String as) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 " + as + " ");
        Object res = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 " + as + " ");
        return res;
    }

    /**
     * @Before pc_changeArg(as)
     */
    public void changeArgBefore(JoinPoint joinPoint, StringBuffer as) {
        as.append("x");
        ((Loggable) joinPoint.getTarget()).log("before " + as.toString() + " ");
    }

    /**
     * @After pc_changeArg(as)
     */
    public void changeArgAfter(JoinPoint joinPoint, StringBuffer as) {
        as.append("x");
        ((Loggable) joinPoint.getTarget()).log("after " + as.toString() + " ");
    }

    /**
     * @Around pc_changeArg(as)
     */
    public Object changeArgAround(StringBuffer as, JoinPoint joinPoint) throws Throwable {//here we use some fancy order in the signature
        as.append("x");
        ((Loggable) joinPoint.getTarget()).log("before1 " + as.toString() + " ");
        Object res = joinPoint.proceed();
        as.append("x");
        ((Loggable) joinPoint.getTarget()).log("after1 " + as.toString() + " ");
        return res;
    }


    /**
     * @Before pc_orderChangedInPointcutSignature(as0, as1)
     */
    public void orderChangedInPointcutSignatureBefore(JoinPoint joinPoint, String as0, String as1) {
        ((Loggable) joinPoint.getTarget()).log("before " + as0 + " " + as1 + " ");
    }

    /**
     * @After pc_orderChangedInPointcutSignature(as0, as1)
     */
    public void orderChangedInPointcutSignatureAfter(JoinPoint joinPoint, String as0, String as1) {
        ((Loggable) joinPoint.getTarget()).log("after " + as0 + " " + as1 + " ");
    }

    /**
     * @Around pc_orderChangedInPointcutSignature(as0, as1)
     */
    public Object orderChangedInPointcutSignatureAround(JoinPoint joinPoint, String as0, String as1) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 " + as0 + " " + as1 + " ");
        Object res = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 " + as0 + " " + as1 + " ");
        return res;
    }


    /**
     * @Before pc_orderChangedInAdviceSignature(as1, as0)
     */
    public void orderChangedInAdviceSignatureBefore(JoinPoint joinPoint, String as0, String as1) {
        ((Loggable) joinPoint.getTarget()).log("before " + as0 + " " + as1 + " ");
    }

    /**
     * @After pc_orderChangedInAdviceSignature(as1, as0)
     */
    public void orderChangedInAdviceSignatureAfter(JoinPoint joinPoint, String as0, String as1) {
        ((Loggable) joinPoint.getTarget()).log("after " + as0 + " " + as1 + " ");
    }

    /**
     * @Around pc_orderChangedInAdviceSignature(as1, as0)
     */
    public Object orderChangedInAdviceSignatureAround(JoinPoint joinPoint, String as0, String as1) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 " + as0 + " " + as1 + " ");
        Object res = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 " + as0 + " " + as1 + " ");
        return res;
    }


    /**
     * @Before pc_orderChangedInPointcutAndAdviceSignature(as1, as0)
     */
    public void orderChangedInPointcutAndAdviceSignatureBefore(JoinPoint joinPoint, String as0, String as1) {
        ((Loggable) joinPoint.getTarget()).log("before " + as0 + " " + as1 + " ");
    }

    /**
     * @After pc_orderChangedInPointcutAndAdviceSignature(as1, as0)
     */
    public void orderChangedInPointcutAndAdviceSignatureAfter(JoinPoint joinPoint, String as0, String as1) {
        ((Loggable) joinPoint.getTarget()).log("after " + as0 + " " + as1 + " ");
    }

    /**
     * @Around pc_orderChangedInPointcutAndAdviceSignature(as1, as0)
     */
    public Object orderChangedInPointcutAndAdviceSignatureAround(JoinPoint joinPoint, String as0, String as1)
            throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 " + as0 + " " + as1 + " ");
        Object res = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 " + as0 + " " + as1 + " ");
        return res;
    }

    //-- Method call pointcuts with args

    /**
     * @Expression in_scope && call(* callGetFirstAndSecond(..)) && args(l, s)
     */
    void pc_callGetFirstAndSecond(long l, String[] s) {
    };

    /**
     * @Before pc_callGetFirstAndSecond(l, s)
     */
    public void callGetFirstAndSecondBefore(JoinPoint joinPoint, long l, String[] s) {
        ((Loggable) joinPoint.getTarget()).log("before " + l + " " + s[0] + "," + s[1] + " ");
    }

    /**
     * @After pc_callGetFirstAndSecond(l, s)
     */
    public void callGetFirstAndSecondAfter(JoinPoint joinPoint, long l, String[] s) {
        ((Loggable) joinPoint.getTarget()).log("after " + l + " " + s[0] + "," + s[1] + " ");
    }

    /**
     * @Around pc_callGetFirstAndSecond(l, s)
     */
    public Object callGetFirstAndSecondAround(JoinPoint joinPoint, long l, String[] s) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 " + l + " " + s[0] + "," + s[1] + " ");
        Object res = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 " + l + " " + s[0] + "," + s[1] + " ");
        return res;
    }

    //-- Ctor execution pointcuts with args
    // we are using inner class, so args() is a bit tricky

    /**
     * @Expression execution(test.args.ArgsAdviceTest$CtorExecution.new(..)) && args(test.args.ArgsAdviceTest, s)
     */
    void pc_ctorExecutionGetFirst(String s) {
    };

    /**
     * @Before pc_ctorExecutionGetFirst(s)
     */
    public void ctorExecutionGetFirstBefore(JoinPoint joinPoint, String s) {
        ((Loggable) joinPoint.getTarget()).log("before " + s + " ");
    }

    /**
     * @After pc_ctorExecutionGetFirst(s)
     */
    public void ctorExecutionGetFirstAfter(JoinPoint joinPoint, String s) {
        ((Loggable) joinPoint.getTarget()).log("after " + s + " ");
    }

    /**
     * @Around pc_ctorExecutionGetFirst(s)
     */
    public Object ctorExecutionGetFirstAround(JoinPoint joinPoint, String s) throws Throwable {
        ((Loggable) joinPoint.getTarget()).log("before1 " + s + " ");
        Object res = joinPoint.proceed();
        ((Loggable) joinPoint.getTarget()).log("after1 " + s + " ");
        return res;
    }

    //-- Ctor call pointcuts with args
    // we are using inner class, so args() is a bit tricky

    /**
     * @Expression in_scope && call(test.args.ArgsAdviceTest$CtorCall.new(..)) && args(test.args.ArgsAdviceTest, s)
     */
    void pc_ctorCallGetFirst(String s) {
    };

    /**
     * @Before pc_ctorCallGetFirst(s)
     */
    public void ctorCallGetFirstBefore(JoinPoint joinPoint, String s) {
        ArgsAdviceTest.logStatic("before " + s + " ");
    }

    /**
     * @After pc_ctorCallGetFirst(s)
     */
    public void ctorCallGetFirstAfter(JoinPoint joinPoint, String s) {
        ArgsAdviceTest.logStatic("after " + s + " ");
    }

    /**
     * @Around pc_ctorCallGetFirst(s)
     */
    public Object ctorCallGetFirstAround(JoinPoint joinPoint, String s) throws Throwable {
        ArgsAdviceTest.logStatic("before1 " + s + " ");
        Object res = joinPoint.proceed();
        ArgsAdviceTest.logStatic("after1 " + s + " ");
        return res;
    }

    //-- field set with args()
    /**
     * @Expression in_scope && set(* m_field) && args(s)
     */
    void pc_mfield(String s) {
    };

    /**
     * @Before pc_mfield(s)
     */
    public void mfieldBefore(JoinPoint joinPoint, String s) {
        String fieldValue = ((ArgsAdviceTest) joinPoint.getTarget()).getField();
        ((Loggable) joinPoint.getTarget()).log("before " + fieldValue + "," + s + " ");
    }

    /**
     * @After pc_mfield(s)
     */
    public void mfieldAfter(JoinPoint joinPoint, String s) {
        String fieldValue = ((ArgsAdviceTest) joinPoint.getTarget()).getField();
        ((Loggable) joinPoint.getTarget()).log("after " + fieldValue + "," + s + " ");
    }

    /**
     * @Around pc_mfield(s)
     */
    public Object mfieldAround(JoinPoint joinPoint, String s) throws Throwable {
        String fieldValue = ((ArgsAdviceTest) joinPoint.getTarget()).getField();
        ((Loggable) joinPoint.getTarget()).log("before1 " + fieldValue + "," + s + " ");
        s = "changed"; // will be ignored due to delegation ! [AJ]
        Object res = joinPoint.proceed();
        fieldValue = ((ArgsAdviceTest) joinPoint.getTarget()).getField();
        ((Loggable) joinPoint.getTarget()).log("after1 " + fieldValue + "," + s + " ");
        return "ignored";
    }

    //-- static field set with args()
    /**
     * @Expression in_scope && set(* s_field) && args(s)
     */
    void pc_sfield(String s) {
    };

    /**
     * @Before pc_sfield(s)
     */
    public void sfieldBefore(JoinPoint joinPoint, String s) {
        String fieldValue = ArgsAdviceTest.getStaticField();
        ArgsAdviceTest.logStatic("before " + fieldValue + "," + s + " ");
    }

    /**
     * @After pc_sfield(s)
     */
    public void sfieldAfter(JoinPoint joinPoint, String s) {
        String fieldValue = ArgsAdviceTest.getStaticField();
        ArgsAdviceTest.logStatic("after " + fieldValue + "," + s + " ");
    }

    /**
     * @Around pc_sfield(s)
     */
    public Object sfieldAround(JoinPoint joinPoint, String s) throws Throwable {
        String fieldValue = ArgsAdviceTest.getStaticField();
        ArgsAdviceTest.logStatic("before1 " + fieldValue + "," + s + " ");
        s = "changed"; // will be ignored due to delegation ! [AJ]
        Object res = joinPoint.proceed();
        fieldValue = ArgsAdviceTest.getStaticField();
        ArgsAdviceTest.logStatic("after1 " + fieldValue + "," + s + " ");
        return "ignored";
    }

}
