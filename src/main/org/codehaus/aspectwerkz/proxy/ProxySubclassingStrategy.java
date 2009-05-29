/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.proxy;

import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;
import org.codehaus.aspectwerkz.hook.impl.ClassPreProcessorHelper;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.backport175.reader.bytecode.spi.BytecodeProvider;
import org.codehaus.backport175.reader.bytecode.AnnotationReader;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Get proxy classes from target classes and weaves in all matching aspects deployed in the class loader
 * and defined by the <code>META-INF/aop.xml</code> file.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class ProxySubclassingStrategy {

    /**
     * The suffix for the compiled proxy classes.
     */
    public static final String PROXY_SUFFIX_START = "$$ProxiedByAWSubclassing$$";

    /**
     * Cache for the compiled proxy classes. Target class is key.
     */
    private static final Map PROXY_CLASS_CACHE = new WeakHashMap();

    /**
     * Creates a new proxy instance based for the class specified and instantiates it using its default no-argument
     * constructor.
     * <p/>
     * The proxy will be cached and non-advisable.
     *
     * @param clazz the target class to make a proxy for
     * @return the proxy instance
     */
    static Object newInstance(final Class clazz) {
        try {
            Class proxyClass = getProxyClassFor(clazz, true, false);
            return proxyClass.newInstance();
        } catch (Throwable t) {
            throw new WrappedRuntimeException(t);
        }
    }

    /**
     * Creates a new proxy instance for the class specified and instantiates it using the constructor matching
     * the argument type array specified.
     * <p/>
     * The proxy will be cached and non-advisable.
     *
     * @param clazz          the target class to make a proxy for
     * @param argumentTypes  the argument types matching the signature of the constructor to use when instantiating the proxy
     * @param argumentValues the argument values to use when instantiating the proxy
     * @return the proxy instance
     */
    static Object newInstance(final Class clazz, final Class[] argumentTypes, final Object[] argumentValues) {
        try {
            Class proxyClass = getProxyClassFor(clazz, true, false);
            return proxyClass.getDeclaredConstructor(argumentTypes).newInstance(argumentValues);
        } catch (Throwable t) {
            throw new WrappedRuntimeException(t);
        }
    }

    /**
     * Creates a new proxy instance based for the class specified and instantiates it using its default no-argument
     * constructor.
     *
     * @param clazz         the target class to make a proxy for
     * @param useCache      true if a cached instance of the proxy classed should be used
     * @param makeAdvisable true if the proxy class should implement the <code>Advisable</code> interface,
     *                      e.g. be prepared for programmatic, runtime, per instance hot deployement of advice
     * @return the proxy instance
     */
    static Object newInstance(final Class clazz, final boolean useCache, final boolean makeAdvisable) {
        try {
            Class proxyClass = getProxyClassFor(clazz, useCache, makeAdvisable);
            return proxyClass.newInstance();
        } catch (Throwable t) {
            throw new WrappedRuntimeException(t);
        }
    }

    /**
     * Creates a new proxy instance for the class specified and instantiates it using the constructor matching
     * the argument type array specified.
     *
     * @param clazz          the target class to make a proxy for
     * @param argumentTypes  the argument types matching the signature of the constructor to use when instantiating the proxy
     * @param argumentValues the argument values to use when instantiating the proxy
     * @param useCache       true if a cached instance of the proxy classed should be used
     * @param makeAdvisable  true if the proxy class should implement the <code>Advisable</code> interface,
     *                       e.g. be prepared for programmatic, runtime, per instance hot deployement of advice
     * @return the proxy instance
     */
    static Object newInstance(final Class clazz,
                              final Class[] argumentTypes,
                              final Object[] argumentValues,
                              final boolean useCache,
                              final boolean makeAdvisable) {
        try {
            Class proxyClass = getProxyClassFor(clazz, useCache, makeAdvisable);
            return proxyClass.getDeclaredConstructor(argumentTypes).newInstance(argumentValues);
        } catch (Throwable t) {
            throw new WrappedRuntimeException(t);
        }
    }

    /**
     * Compiles and returns a proxy class for the class specified.
     *
     * @param clazz         the target class to make a proxy for
     * @param useCache      true if a cached instance of the proxy classed should be used
     * @param makeAdvisable true if the proxy class should implement the <code>Advisable</code> interface,
     *                      e.g. be prepared for programmatic, runtime, per instance hot deployement of advice
     * @return the proxy class
     */
    static Class getProxyClassFor(final Class clazz, final boolean useCache, final boolean makeAdvisable) {

        // FIXME - add support for proxying java.* classes
        if (clazz.getName().startsWith("java.")) {
            throw new RuntimeException("can not create proxies from system classes (java.*)");
        }
        if (!useCache) {
            return getNewProxyClassFor(clazz, makeAdvisable);
        } else {
            synchronized (PROXY_CLASS_CACHE) {
                Object cachedProxyClass = PROXY_CLASS_CACHE.get(clazz);
                if (cachedProxyClass != null) {
                    return (Class) cachedProxyClass;
                }
                Class proxyClass = getNewProxyClassFor(clazz, makeAdvisable);
                PROXY_CLASS_CACHE.put(clazz, proxyClass);
                return proxyClass;
            }
        }
    }

    /**
     * Compiles and returns a proxy class for the class specified.
     * No cache is used, but compiles a new one each invocation.
     *
     * @param clazz
     * @param makeAdvisable true if the proxy class should implement the <code>Advisable</code> interface,
     *                      e.g. be prepared for programmatic, runtime, per instance hot deployement of advice
     * @return the proxy class
     */
    private static Class getNewProxyClassFor(final Class clazz, final boolean makeAdvisable) {
        ClassLoader loader = clazz.getClassLoader();
        String proxyClassName = getUniqueClassNameForProxy(clazz);

        if (makeAdvisable) {
            Proxy.makeProxyAdvisable(proxyClassName, loader);
        }

        final byte[] bytes = ProxySubclassingCompiler.compileProxyFor(clazz, proxyClassName);
        // register the bytecode provider
        // TODO AV - could be optimized ?(f.e. recompile everytime instead of creating many provider)
        AnnotationReader.setBytecodeProviderFor(
                proxyClassName, loader, new BytecodeProvider() {
                    public byte[] getBytecode(String className, ClassLoader loader) throws Exception {
                        return bytes;
                    }
                }
        );
        byte[] transformedBytes = ClassPreProcessorHelper.defineClass0Pre(
                loader, proxyClassName, bytes, 0, bytes.length, null
        );

        return AsmHelper.defineClass(loader, transformedBytes, proxyClassName);
    }

    /**
     * Returns a unique name for the proxy class.
     *
     * @param clazz target class
     * @return the proxy class name
     */
    private static String getUniqueClassNameForProxy(final Class clazz) {
        return clazz.getName().replace('.', '/') + PROXY_SUFFIX_START + new Long(Uuid.newUuid()).toString();
    }

    /**
     * Returns a unique name for the proxy class.
     *
     * @param proxyClassName
     * @return the class name beeing proxied
     */
    static String getUniqueClassNameFromProxy(final String proxyClassName) {
        int index = proxyClassName.lastIndexOf(PROXY_SUFFIX_START);
        if (index > 0) {
            return proxyClassName.substring(0, index);
        } else {
            return null;
        }
    }

}
