/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform;

/**
 * A tuple based on className and defining ClassLoader object
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class ClassCacheTuple {
    private ClassLoader classLoader;

    private String className;

    public ClassCacheTuple(ClassLoader classLoader, String className) {
        setClassLoader(classLoader);
        setClassName(className);
    }

    public ClassCacheTuple(Class klass) {
        setClassLoader(klass.getClassLoader());
        setClassName(klass.getName());
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassCacheTuple)) {
            return false;
        }
        final ClassCacheTuple classCacheTuple = (ClassCacheTuple) o;
        if (!classLoader.equals(classCacheTuple.classLoader)) {
            return false;
        }
        if (!className.equals(classCacheTuple.className)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = classLoader.hashCode();
        result = (29 * result) + className.hashCode();
        return result;
    }
}