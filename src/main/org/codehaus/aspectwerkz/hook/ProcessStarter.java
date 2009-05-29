/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

import com.sun.jdi.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * ProcessStarter uses JPDA JDI api to start a VM with a runtime modified java.lang.ClassLoader, or transparently use a
 * Xbootclasspath style (java 1.3 detected or forced) <p/>
 * <p/>
 * <h2>Important note</h2>
 * Due to a JPDA issue in LauchingConnector, this implementation is based on Process forking. If Xbootclasspath is not
 * used the target VM is started with JDWP options <i>transport=dt_socket,address=9300 </i> unless other specified.
 * <br/>It is possible after the short startup sequence to attach a debugger or any other JPDA attaching connector. It
 * has been validated against a WebLogic 7 startup and is the <i>must use </i> implementation.
 * </p>
 * <p/>
 * <p/>
 * <h2>Implementation Note</h2>
 * See http://java.sun.com/products/jpda/ <br/>See http://java.sun.com/j2se/1.4.1/docs/guide/jpda/jdi/index.html <br/>
 * </p>
 * <p/><p/>For java 1.3, it launch the target VM using a modified java.lang.ClassLoader by generating it and putting it
 * in the bootstrap classpath of the target VM. The java 1.3 version should only be run for experimentation since it
 * breaks the Java 2 Runtime Environment binary code license by overriding a class of rt.jar
 * </p>
 * <p/><p/>For java 1.4, it hotswaps java.lang.ClassLoader with a runtime patched version, wich is compatible with the
 * Java 2 Runtime Environment binary code license. For JVM not supporting the class hotswapping, the same mechanism as
 * for java 1.3 is used.
 * </p>
 * <p/>
 * <p/>
 * <h2>Usage</h2>
 * Use it as a replacement of "java" :<br/><code>java [target jvm option] [target classpath]
 * targetMainClass [targetMainClass args]</code>
 * <br/>should be called like: <br/><code>java [jvm option] [classpath]
 * org.codehaus.aspectwerkz.hook.ProcessStarter [target jvm option] [target classpath] targetMainClass [targetMainClass
 * args]</code>
 * <br/><b>[classpath] must contain %JAVA_HOME%/tools.jar for HotSwap support </b> <br/>[target jvm option] can contain
 * JDWP options, transport and address are preserved if specified.
 * </p>
 * <p/>
 * <p/>
 * <h2>Options</h2>
 * [classpath] must contain %JAVA_HOME%/tools.jar and the jar you want for bytecode modification (asm, bcel, ...)
 * <br/>The java.lang.ClassLoader is patched using the <code>-Daspectwerkz.classloader.clpreprocessor=...</code> in
 * [jvm option]. Specify the FQN of your implementation of hook.ClassLoaderPreProcessor. See {@link
 * org.codehaus.aspectwerkz.hook.ClassLoaderPreProcessor} If not given, the default AspectWerkz layer 1 ASM
 * implementation hook.impl.* is used, which is equivalent to
 * <code>-Daspectwerkz.classloader.clpreprocessor=org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl</code>
 * <br/>Use -Daspectwerkz.classloader.wait=2 in [jvm option] to force a pause of 2 seconds between process fork and JPDA
 * connection for HotSwap. Defaults to no wait.
 * </p>
 * <p/>
 * <p/>
 * <h2>Disabling HotSwap</h2>
 * You disable HotSwap and thus force the use of -Xbootclasspath (like in java 1.3 mode) and specify the directory where
 * the modified class loader bytecode will be stored using in [jvm option]
 * <code>-Daspectwerkz.classloader.clbootclasspath=...</code>. Specify the directory where you want the patched
 * java.lang.ClassLoader to be stored. Default is "./_boot". The directory is created if needed (with the subdirectories
 * corresponding to package names). <br/>The directory is <b>automatically </b> incorporated in the -Xbootclasspath
 * option of [target jvm option]. <br/>You shoud use this option mainly for debuging purpose, or if you need to start
 * different jvm with different classloader preprocessor implementations.
 * </p>
 * <p/>
 * <p/>
 * <h2>Option for AspectWerkz layer 1 ASM implementation</h2>
 * When using the default AspectWerkz layer 1 ASM implementation
 * <code>org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl</code>, java.lang.ClassLoader is modified to
 * call a class preprocessor at each class load (except for class loaded by the bootstrap classloader). <br/>The
 * effective class preprocessor is defined with <code>-Daspectwerkz.classloader.preprocessor=...</code> in [target jvm
 * option]. Specify the FQN of your implementation of org.codehaus.aspectwerkz.hook.ClassPreProcessor interface. <br/>If
 * this parameter is not given, the default AspectWerkz layer 2
 * org.codehaus.aspectwerkz.transform.AspectWerkzPreProcessor is used. <br/>
 * </p>
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class ProcessStarter {
    /**
     * option for classloader preprocessor target
     */
    final static String CL_PRE_PROCESSOR_CLASSNAME_PROPERTY = "aspectwerkz.classloader.clpreprocessor";

    /**
     * default dir when -Xbootclasspath is forced or used (java 1.3)
     */
    private final static String CL_BOOTCLASSPATH_FORCE_DEFAULT = "." + File.separatorChar + "_boot";

    /**
     * option for target dir when -Xbootclasspath is forced or used (java 1.3)
     */
    private final static String CL_BOOTCLASSPATH_FORCE_PROPERTY = "aspectwerkz.classloader.clbootclasspath";

    /**
     * option for seconds to wait before connecting
     */
    private final static String CONNECTION_WAIT_PROPERTY = "aspectwerkz.classloader.wait";

    /**
     * target process
     */
    private Process process = null;

    /**
     * used if target VM exits before launching VM
     */
    private boolean executeShutdownHook = true;

    /**
     * thread to redirect streams of target VM in launching VM
     */
    private Thread inThread;

    /**
     * thread to redirect streams of target VM in launching VM
     */
    private Thread outThread;

    /**
     * thread to redirect streams of target VM in launching VM
     */
    private Thread errThread;

    /**
     * Test if current java installation supports HotSwap
     */
    private static boolean hasCanRedefineClass() {
        try {
            VirtualMachine.class.getMethod("canRedefineClasses", new Class[]{});
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    private int run(String[] args) {
        // retrieve options and main
        String[] javaArgs = parseJavaCommandLine(args);
        String optionArgs = javaArgs[0];
        String cpArgs = javaArgs[1];
        String mainArgs = javaArgs[2];
        String options = optionArgs + " -cp " + cpArgs;
        String clp = System.getProperty(
                CL_PRE_PROCESSOR_CLASSNAME_PROPERTY,
                "org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl"
        );

        // if java version does not support method "VirtualMachine.canRedefineClass"
        // or if bootclasspath is forced, transform optionsArg
        if (!hasCanRedefineClass() || (System.getProperty(CL_BOOTCLASSPATH_FORCE_PROPERTY) != null)) {
            String bootDir = System.getProperty(CL_BOOTCLASSPATH_FORCE_PROPERTY, CL_BOOTCLASSPATH_FORCE_DEFAULT);
            if (System.getProperty(CL_BOOTCLASSPATH_FORCE_PROPERTY) != null) {
                System.out.println("HotSwap deactivated, using bootclasspath: " + bootDir);
            } else {
                System.out.println("HotSwap not supported by this java version, using bootclasspath: " + bootDir);
            }
            ClassLoaderPatcher.patchClassLoader(clp, bootDir);
            BootClasspathStarter starter = new BootClasspathStarter(options, mainArgs, bootDir);
            try {
                process = starter.launchVM();
            } catch (IOException e) {
                System.err.println("failed to launch process :" + starter.getCommandLine());
                e.printStackTrace();
                return -1;
            }

            // attach stdout VM streams to this streams
            // this is needed early to support -verbose:class like options
            redirectStdoutStreams();
        } else {
            // lauch VM in suspend mode
            JDWPStarter starter = new JDWPStarter(options, mainArgs, "dt_socket", "9300");
            try {
                process = starter.launchVM();
            } catch (IOException e) {
                System.err.println("failed to launch process :" + starter.getCommandLine());
                e.printStackTrace();
                return -1;
            }

            // attach stdout VM streams to this streams
            // this is needed early to support -verbose:class like options
            redirectStdoutStreams();

            // override class loader in VM thru an attaching connector
            int secondsToWait = 0;
            try {
                secondsToWait = Integer.parseInt(System.getProperty(CONNECTION_WAIT_PROPERTY, "0"));
            } catch (NumberFormatException nfe) {
                ;
            }
            VirtualMachine vm = JDWPClassLoaderPatcher.hotswapClassLoader(
                    clp,
                    starter.getTransport(),
                    starter.getAddress(),
                    secondsToWait
            );
            if (vm == null) {
                process.destroy();
            } else {
                vm.resume();
                vm.dispose();
            }
        }

        // attach VM other streams to this streams
        redirectOtherStreams();

        // add a shutdown hook to "this" to shutdown VM
        Thread shutdownHook = new Thread() {
            public void run() {
                shutdown();
            }
        };
        try {
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            int exitCode = process.waitFor();
            executeShutdownHook = false;
            return exitCode;
        } catch (Exception e) {
            executeShutdownHook = false;
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * shutdown target VM (used by shutdown hook of lauching VM)
     */
    private void shutdown() {
        if (executeShutdownHook) {
            process.destroy();
        }
        try {
            outThread.join();
            errThread.join();
        } catch (InterruptedException e) {
            ;
        }
    }

    /**
     * Set up stream redirection in target VM for stdout
     */
    private void redirectStdoutStreams() {
        outThread = new StreamRedirectThread("out.redirect", process.getInputStream(), System.out);
        outThread.start();
    }

    /**
     * Set up stream redirection in target VM for stderr and stdin
     */
    private void redirectOtherStreams() {
        inThread = new StreamRedirectThread("in.redirect", System.in, process.getOutputStream());
        inThread.setDaemon(true);
        errThread = new StreamRedirectThread("err.redirect", process.getErrorStream(), System.err);
        inThread.start();
        errThread.start();
    }

    public static void main(String[] args) {
        System.exit((new ProcessStarter()).run(args));
    }

    private static String escapeWhiteSpace(String s) {
        if (s.indexOf(' ') > 0) {
            StringBuffer sb = new StringBuffer();
            StringTokenizer st = new StringTokenizer(s, " ", true);
            String current = null;
            while (st.hasMoreTokens()) {
                current = st.nextToken();
                if (" ".equals(current)) {
                    sb.append("\\ ");
                } else {
                    sb.append(current);
                }
            }
            return sb.toString();
        } else {
            return s;
        }
    }

    /**
     * Remove first and last " or ' if any
     *
     * @param s string to handle
     * @return s whitout first and last " or ' if any
     */
    public static String removeEmbracingQuotes(String s) {
        if ((s.length() >= 2) && (s.charAt(0) == '"') && (s.charAt(s.length() - 1) == '"')) {
            return s.substring(1, s.length() - 1);
        } else if ((s.length() >= 2) && (s.charAt(0) == '\'') && (s.charAt(s.length() - 1) == '\'')) {
            return s.substring(1, s.length() - 1);
        } else {
            return s;
        }
    }

    /**
     * Analyse the args[] as a java command line
     *
     * @param args
     * @return String[] [0]:jvm options except -cp|-classpath, [1]:classpath without -cp, [2]: mainClass + mainOptions
     */
    public String[] parseJavaCommandLine(String[] args) {
        StringBuffer optionsArgB = new StringBuffer();
        StringBuffer cpOptionsArgB = new StringBuffer();
        StringBuffer mainArgB = new StringBuffer();
        String previous = null;
        boolean foundMain = false;
        for (int i = 0; i < args.length; i++) {
            //System.out.println("" + i + " " + args[i]);
            if (args[i].startsWith("-") && !foundMain) {
                if (!("-cp".equals(args[i])) && !("-classpath").equals(args[i])) {
                    optionsArgB.append(args[i]).append(" ");
                }
            } else if (!foundMain && ("-cp".equals(previous) || "-classpath".equals(previous))) {
                if (cpOptionsArgB.length() > 0) {
                    cpOptionsArgB.append(
                            (System.getProperty("os.name", "").toLowerCase().indexOf("windows") >= 0)
                            ? ";"
                            : ":"
                    );
                }
                cpOptionsArgB.append(removeEmbracingQuotes(args[i]));
            } else {
                foundMain = true;
                mainArgB.append(args[i]).append(" ");
            }
            previous = args[i];
        }

        // restore quote around classpath or escape whitespace depending on win*/*nix
        StringBuffer classPath = new StringBuffer();
        if (System.getProperty("os.name", "").toLowerCase().indexOf("windows") >= 0) {
            classPath = classPath.append("\"").append(cpOptionsArgB.toString()).append("\"");
        } else {
            classPath = classPath.append(escapeWhiteSpace(cpOptionsArgB.toString()));
        }
        String[] res = new String[]{
            optionsArgB.toString(), classPath.toString(), mainArgB.toString()
        };
        return res;
    }
}