/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.proxy;

import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;
import org.codehaus.aspectwerkz.hook.impl.ClassPreProcessorHelper;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.backport175.reader.bytecode.AnnotationReader;
import org.codehaus.backport175.reader.bytecode.spi.BytecodeProvider;

import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Get proxy classes from target classes that implement target interfaces
 * and weaves in all matching aspects deployed in the class loader
 * and defined by the <code>META-INF/aop.xml</code> file.
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class ProxyDelegationStrategy {

    /**
     * Suffix for proxy class name. A UUID is further suffixed.
     */
    private static final String PROXY_SUFFIX_START = "$$ProxiedByAWDelegation$$";

    /**
     * Cache for the compiled proxy classes. Implemented interfaces classes are composite key.
     */
    private final static Map PROXY_CLASS_CACHE = new WeakHashMap();

    /**
     * Compile or retrieve from cache a delegation proxy for the given interfaces.
     *
     * @param interfaces
     * @param useCache
     * @param makeAdvisable
     * @return
     */
    static Class getProxyClassFor(Class[] interfaces, boolean useCache, boolean makeAdvisable) {
        if (!useCache) {
            return getNewProxyClassFor(interfaces, makeAdvisable);
        } else {
            CompositeClassKey key = new CompositeClassKey(interfaces);
            synchronized (PROXY_CLASS_CACHE) {
                Object cachedProxyClass = PROXY_CLASS_CACHE.get(key);
                if (cachedProxyClass != null) {
                    return (Class) cachedProxyClass;
                }
                Class proxyClass = getNewProxyClassFor(interfaces, makeAdvisable);
                PROXY_CLASS_CACHE.put(key, proxyClass);
                return proxyClass;
            }
        }
    }

    /**
     * Create a delegation proxy or retrieve it from cache and instantiate it, using the given implementations.
     * <p/>
     * Each implementation must implement the respective given interface.
     *
     * @param interfaces
     * @param implementations
     * @param useCache
     * @param makeAdvisable
     * @return
     */
    static Object newInstance(final Class[] interfaces, final Object[] implementations, final boolean useCache, final boolean makeAdvisable) {
        if (!implementsRespectively(interfaces, implementations)) {
            throw new RuntimeException("Given implementations not consistents with given interfaces");
        }
        Class proxy = getProxyClassFor(interfaces, useCache, makeAdvisable);
        try {
            return proxy.getConstructor(interfaces).newInstance(implementations);
        } catch (Throwable t) {
            throw new WrappedRuntimeException(t);
        }
    }

    /**
     * Return true if each implementation implement the respective given interface.
     *
     * @param interfaces
     * @param implementations
     * @return
     */
    private static boolean implementsRespectively(final Class[] interfaces, final Object[] implementations) {
        if (interfaces.length != implementations.length) {
            return false;
        }
        for (int i = 0; i < interfaces.length; i++) {
            if (!interfaces[i].isAssignableFrom(implementations[i].getClass())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compile a new proxy class and attach it to the lowest shared classloader of the given interfaces (as for JDK proxy).
     *
     * @param interfaces
     * @param makeAdvisable
     * @return
     */
    private static Class getNewProxyClassFor(Class[] interfaces, boolean makeAdvisable) {
        ClassLoader loader = getLowestClassLoader(interfaces);
        String proxyClassName = getUniqueClassNameForProxy(interfaces);

        if (makeAdvisable) {
            Proxy.makeProxyAdvisable(proxyClassName, loader);
        }

        final byte[] bytes = ProxyDelegationCompiler.compileProxyFor(loader, interfaces, proxyClassName);
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
     * Returns the lowest (childest) shared classloader or fail it detects parallel hierarchies.
     *
     * @param classes
     * @return
     */
    private static ClassLoader getLowestClassLoader(Class[] classes) {
        ClassLoader loader = classes[0].getClassLoader();
        for (int i = 1; i < classes.length; i++) {
            Class other = classes[i];
            if (SystemDefinitionContainer.isChildOf(other.getClassLoader(), loader)) {
                loader = other.getClassLoader();
            } else if (SystemDefinitionContainer.isChildOf(loader, other.getClassLoader())) {
                ;//loader is fine
            } else {
                throw new RuntimeException("parallel classloader hierarchy not supported");
            }
        }
        return loader;
    }

    private static String getUniqueClassNameForProxy(Class[] interfaces) {
        //TODO based on first interface. Do we need a naming strategy / prefix ?
        //TODO see in pointcut matching how the classname is tweaked to get the class it refers to
        return interfaces[0].getName().replace('.', '/') + PROXY_SUFFIX_START + new Long(Uuid.newUuid()).toString();
    }

    /**
     * A composite key for the proxy cache.
     */
    private static class CompositeClassKey {
        private final Class[] m_interfaces;

        CompositeClassKey(Class[] interfaces) {
            m_interfaces = interfaces;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositeClassKey)) return false;

            final CompositeClassKey compositeClassKey = (CompositeClassKey) o;

            if (!Arrays.equals(m_interfaces, compositeClassKey.m_interfaces)) return false;

            return true;
        }

        public int hashCode() {
            int result = 1;
            for (int i = 0; i < m_interfaces.length; i++) {
                result = 31 * result + m_interfaces[i].hashCode();
            }
            return result;
        }
    }

}
