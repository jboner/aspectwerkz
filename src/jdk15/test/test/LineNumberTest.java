/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.annotation.Before;
import org.codehaus.aspectwerkz.transform.AspectWerkzPreProcessor;
import org.codehaus.aspectwerkz.transform.inlining.EmittedJoinPoint;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class LineNumberTest extends TestCase {

    int m_field;

    int s_field;

    public LineNumberTest() {//ctor exe
        m_field = 1;//field set
        int mget = m_field;//field get
        s_field = 2;//field set
        int sget = s_field;//field get
    }

    public void exec() {//method exe
        called();//method call
    }

    public void called() {//method exe
        LineNumberTest t = new LineNumberTest();//ctor call
        try {
            throw new RuntimeException("fake");//ctor call
        } catch (RuntimeException e) {//handler
            ;
        }
    }


    public void testLineNumbers() throws Throwable {
        AspectWerkzPreProcessor aw = new AspectWerkzPreProcessor();
        aw.initialize();
        String name = LineNumberTest.class.getName().replace('/', '.');
        InputStream in = LineNumberTest.class.getClassLoader().getResourceAsStream(name.replace('.','/')+".class");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (in.available() > 0) {
            int length = in.read(buffer);
            if (length == -1) {
                break;
            }
            bos.write(buffer, 0, length);
        }
        in.close();

        byte[] inBytes = bos.toByteArray();
        AspectWerkzPreProcessor.Output output = aw.preProcessWithOutput(name, inBytes, LineNumberTest.class.getClassLoader());

        // we have 11 joinpoints
        assertEquals(11, output.emittedJoinPoints.length);

        //Note: this test depends upon visitor order
        // adapt the output when needed
        StringBuffer emitted = new StringBuffer();
        for (int i = 0; i < output.emittedJoinPoints.length; i++) {
            EmittedJoinPoint emittedJoinPoint = output.emittedJoinPoints[i];
            //System.out.println(emittedJoinPoint);
            emitted.append(emittedJoinPoint.toString());
            emitted.append("\n");
        }
        String allJps = "ConstructorExecution , caller test/LineNumberTest.<init>()V , callee test/LineNumberTest.<init> ()V , line 0\n" +
                "FieldSet , caller test/LineNumberTest.<init>()V , callee test/LineNumberTest.m_field I , line 28\n" +
                "FieldGet , caller test/LineNumberTest.<init>()V , callee test/LineNumberTest.m_field I , line 29\n" +
                "FieldSet , caller test/LineNumberTest.<init>()V , callee test/LineNumberTest.s_field I , line 30\n" +
                "FieldGet , caller test/LineNumberTest.<init>()V , callee test/LineNumberTest.s_field I , line 31\n" +
                "MethodExecution , caller test/LineNumberTest.exec()V , callee test/LineNumberTest.exec ()V , line 0\n" +
                "MethodCall , caller test/LineNumberTest.exec()V , callee test/LineNumberTest.called ()V , line 35\n" +
                "MethodExecution , caller test/LineNumberTest.called()V , callee test/LineNumberTest.called ()V , line 0\n" +
                "ConstructorCall , caller test/LineNumberTest.called()V , callee test/LineNumberTest.<init> ()V , line 39\n" +
                "ConstructorCall , caller test/LineNumberTest.called()V , callee java/lang/RuntimeException.<init> (Ljava/lang/String;)V , line 41\n" +
                "Handler , caller test/LineNumberTest.called()V , callee java/lang/RuntimeException. Ljava/lang/RuntimeException; , line 42\n" +
                "";
        assertEquals(allJps, emitted.toString());
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(LineNumberTest.class);
    }

    public static class Aspect {

        @Before("within(test.LineNumberTest) && !withincode(* test*(..)) && !withincode(* main(..)) && !withincode(* suite())")
        void before() {
        }
    }
}
