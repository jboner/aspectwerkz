/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.compiler;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * VerifierClassLoader does not follow parent delegation model. <p/>It allow to run the -verify option of offline mode
 * on aspectwerkz itself.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class VerifierClassLoader extends URLClassLoader {
    public VerifierClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // First, check if the class has already been loaded
        Class c = findLoadedClass(name);
        if (c == null) {
            try {
                // try to load the class localy
                c = findClass(name);
            } catch (ClassNotFoundException e) {
                // delegate to parent
                c = getParent().loadClass(name);
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}