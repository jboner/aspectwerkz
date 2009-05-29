/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD-style license *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.extension.hotswap;

/**
 * A simple class to test the in process HotSwap
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class Foo {

    public void sayHello() {
        System.out.println("Hello - I am " + this + " class " + this.getClass().hashCode());
    }

    public static void main(String a[]) throws Throwable {
        //        System.out.println("start");
        //        HotSwapClient client = new HotSwapClient();
        //        System.out.println("created hotswap client");
        //        Foo aFoo = new Foo();
        //        aFoo.sayHello();
        //        ClassPool cp = ClassPool.getDefault();
        //        CtClass newFoo = cp.get("org.codehaus.aspectwerkz.extension.hotswap.Foo");
        //        CtMethod m = newFoo.getDeclaredMethod("sayHello");
        //        m.insertBefore("{System.out.println(\"\thotswapped talks:\");}");
        //        byte[] newFooB = cp.write("org.codehaus.aspectwerkz.extension.hotswap.Foo");
        //        HotSwapClient.hotswap(Foo.class, newFooB);
        //
        //        // same instance is hotswapped
        //        aFoo.sayHello();
        //
        //        // other instance is hotswapped
        //        Foo bFoo = new Foo();
        //        bFoo.sayHello();
        //        ClassPool cp2 = new ClassPool(null);
        //        cp2.appendClassPath(new LoaderClassPath(Foo.class.getClassLoader()));
        //        try {
        //            // swap java.lang.ClassLoader with itself
        //            cp2.writeFile("java.lang.ClassLoader", "_dump");
        //            //byte[] bytecode =
        // ClassLoaderPatcher.getPatchedClassLoader("org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl");
        //            HotSwapClient.hotswap(ClassLoader.class, cp2.get("java.lang.ClassLoader").toBytecode());
        //        } catch (Throwable e) {
        //            e.printStackTrace();
        //        }
        //
        //        // swap java.lang.String with itself
        //        cp2.writeFile("java.lang.String", "_dump");
        //        //byte[] bytecode =
        // ClassLoaderPatcher.getPatchedClassLoader("org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl");
        //        HotSwapClient.hotswap(String.class, cp2.get("java.lang.String").toBytecode());
    }
}