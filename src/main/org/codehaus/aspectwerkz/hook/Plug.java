/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.lang.reflect.Method;

/**
 * Main application that allow two steps preparation of the hook <p/>This can be used instead of ProcessStarter to dual
 * JVM and stream piping <br/><p/>
 * <h2>Usage</h2>
 * <pre>
 *     java [options..] org.codehaus.aspectwerkz.hook.Plug -target &lt;targetJar.jar&gt;
 *     java [options..] org.codehaus.aspectwerkz.hook.Plug -hotswap &lt;jdwp options&gt;
 *     java [options..] org.codehaus.aspectwerkz.hook.Plug -resume &lt;jdwp options&gt;
 *     java [options..] org.codehaus.aspectwerkz.hook.Plug -info &lt;jdwp options&gt;
 * </pre>
 * <p/>
 * <ul>
 * <li>-target targetJar.jar to generate a targetJar.jar containing the patched java.lang.ClassLoader suitable for your
 * current java installation. <br/>Add this jar in -Xbootclasspath/p: options as other AspectWerkz options [see
 * documentation]</li>
 * <li>-hotswap will hotswap the java.lang.ClassLoader in a running or suspended jvm, and will resume the jvm</li>
 * <li>-resume will resume the (running or) suspended jvm</li>
 * <li>-info will print out JPDA information and resume the (running or) suspended jvm</li>*
 * </ul>
 * For the last two invocations, [jdwp options] must be the subpart of the -Xrunjdwp option indicating how to connect to
 * the remote JVM (see sample below or documentation). <i>For now, only localhost connection is supported. </i>
 * <p/>
 * If the JVM was started with -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y
 * Use java [options..] ..Plug -prepare transport=dt_socket,address=8000
 * <p/>
 * <b>Be sure to set AspectWerkz option prior to starting the JVM with -Xrunjdwp options. </b>
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class Plug {

    /**
     * Dumps the modified java.lang.ClassLoader in destJar
     * The aspectcwerkz.classloader.clclasspreprocessor is used
     * if specified, else defaults to AspectWerkz layer 1
     *
     * @param destJar
     * @throws Exception
     */
    public void target(String destJar) throws Exception {
        File dest = new File(destJar);
        if (dest.exists() && !dest.canWrite()) {
            throw new Exception(destJar + " exists and is not writable");
        }

        // patch the java.lang.ClassLoader
        byte[] patched = ClassLoaderPatcher.getPatchedClassLoader(
                System.getProperty(
                        ProcessStarter.CL_PRE_PROCESSOR_CLASSNAME_PROPERTY,
                        org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl.class.getName()
                )
        );

        // pack the jar file
        Manifest mf = new Manifest();
        Attributes at = mf.getMainAttributes();
        at.putValue(Attributes.Name.MANIFEST_VERSION.toString(), "1.0");
        at.putValue("Created-By", "AspectWerkz (c) Plug [java " + System.getProperty("java.version") + ']');
        ZipEntry entry = new ZipEntry("java/lang/ClassLoader.class");
        entry.setSize(patched.length);
        CRC32 crc = new CRC32();
        crc.update(patched);
        entry.setCrc(crc.getValue());
        JarOutputStream jar = new JarOutputStream(new FileOutputStream(dest), mf);
        jar.putNextEntry(entry);
        jar.write(patched);
        jar.closeEntry();
        jar.close();
    }


    /**
     * Print usage information on stdout
     */
    public static void usage() {
        System.out.println("AspectWerkz (c) Plug");
        System.out.println("Usage: " + "-target <targetJar.jar>");
        System.out.println("       " + "-hotswap <jdwp options>");
        System.out.println("       " + "-resume <jdwp options>");
        System.out.println("       " + "-info <jdwp options>");
    }

    /**
     * Parse a jdwp like string in a Map <p/>transport=dt_socket,address=8000 will produce a Map of 2 entries whose keys
     * are transport and address
     *
     * @param args
     * @return Map jdwp options
     */
    public static Map parseArgs(String args) throws Exception {
        Map map = new HashMap();
        StringTokenizer st = new StringTokenizer(args, ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int index = token.indexOf("=");
            if (index < 0) {
                throw new Exception("invalid jdwp string: " + args);
            }
            map.put(token.substring(0, index), token.substring(index + 1));
        }
        return map;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            usage();
            System.exit(1);
        }
        if ("-target".equals(args[0])) {
            try {
                new Plug().target(args[1]);
                System.out.println("done: " + args[1]);
            } catch (Exception e) {
                System.err.println("-target failed: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                Map jdwp = parseArgs(args[1]);
                // do a reflect invocation to avoid relying on a tools.jar dependancy
                if ("-hotswap".equals(args[0])) {
                    Class jdwpClass = Class.forName(
                            "org.codehaus.aspectwerkz.hook.JDWPPlug", false, Plug.class.getClassLoader()
                    );
                    Object instance = jdwpClass.newInstance();
                    Method m = jdwpClass.getDeclaredMethod("hotswap", new Class[]{Map.class});
                    m.invoke(instance, new Object[]{jdwp});
                    //new JDWPPlug().hotswap(jdwp);
                } else if ("-resume".equals(args[0])) {
                    Class jdwpClass = Class.forName(
                            "org.codehaus.aspectwerkz.hook.JDWPPlug", false, Plug.class.getClassLoader()
                    );
                    Object instance = jdwpClass.newInstance();
                    Method m = jdwpClass.getDeclaredMethod("resume", new Class[]{Map.class});
                    m.invoke(instance, new Object[]{jdwp});
                    //new JDWPPlug().resume(jdwp);
                } else if ("-info".equals(args[0])) {
                    Class jdwpClass = Class.forName(
                            "org.codehaus.aspectwerkz.hook.JDWPPlug", false, Plug.class.getClassLoader()
                    );
                    Object instance = jdwpClass.newInstance();
                    Method m = jdwpClass.getDeclaredMethod("info", new Class[]{Map.class});
                    m.invoke(instance, new Object[]{jdwp});
                    //new JDWPPlug().info(jdwp);
                } else {
                    usage();
                    System.exit(1);
                }
            } catch (Exception e) {
                System.err.println(args[0] + " failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}