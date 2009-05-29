/**************************************************************************************
 * Copyright (c) Jonas Bon?r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

import java.util.Map;
import java.util.Iterator;
import java.io.IOException;

/**
 * Isolation of JDWP dependancies to Plug online mode in a running / remote VM.
 * See Plug.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class JDWPPlug {

    /**
     * transport jdwp option
     */
    private final static String TRANSPORT_JDWP = "transport";

    /**
     * address jdwp option
     */
    private final static String ADDRESS_JDWP = "address";

    /**
     * Connects to a remote JVM using the jdwp options specified in jdwp
     *
     * @param jdwp
     * @return VirtualMachine or null if failure
     * @throws Exception
     */
    private VirtualMachine connect(Map jdwp) throws Exception {
        String transport = (String) jdwp.get(TRANSPORT_JDWP);
        String address = (String) jdwp.get(ADDRESS_JDWP);
        String name = null;
        if ("dt_socket".equals(transport)) {
            name = "com.sun.jdi.SocketAttach";
        } else if ("dt_shmem".equals(transport)) {
            name = "com.sun.jdi.SharedMemoryAttach";
        }
        AttachingConnector connector = null;
        for (Iterator i = Bootstrap.virtualMachineManager().attachingConnectors().iterator(); i.hasNext();) {
            AttachingConnector aConnector = (AttachingConnector) i.next();
            if (aConnector.name().equals(name)) {
                connector = aConnector;
                break;
            }
        }
        if (connector == null) {
            throw new Exception("no AttachingConnector for transport: " + transport);
        }
        Map args = connector.defaultArguments();
        if ("dt_socket".equals(transport)) {
            ((Connector.Argument) args.get("port")).setValue(address);
        } else if ("dt_shmem".equals(transport)) {
            ((Connector.Argument) args.get("name")).setValue(address);
        }
        try {
            VirtualMachine vm = connector.attach(args);
            return vm;
        } catch (IllegalConnectorArgumentsException e) {
            System.err.println("failed to attach to VM (" + transport + ", " + address + "):");
            e.printStackTrace();
            for (Iterator i = e.argumentNames().iterator(); i.hasNext();) {
                System.err.println("wrong or missing argument - " + i.next());
            }
            return null;
        } catch (IOException e) {
            System.err.println("failed to attach to VM (" + transport + ", " + address + "):");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Resume the remote JVM, without hotswapping classes
     *
     * @param jdwp
     * @throws Exception
     */
    public void resume(Map jdwp) throws Exception {
        VirtualMachine vm = connect(jdwp);
        if (vm != null) {
            vm.resume();
            vm.dispose();
        }
    }

    /**
     * Prints information about the remote JVM and resume
     *
     * @param jdwp
     * @throws Exception
     */
    public void info(Map jdwp) throws Exception {
        VirtualMachine vm = connect(jdwp);
        if (vm != null) {
            System.out.println("java.vm.name = " + vm.name());
            System.out.println("java.version = " + vm.version());
            System.out.println(vm.description());
            vm.resume();
            vm.dispose();
        }
    }

    /**
     * Hotswaps the java.lang.ClassLoader of the remote JVM and resume
     *
     * @param jdwp
     * @throws Exception
     */
    public void hotswap(Map jdwp) throws Exception {
        // @todo check it works at runtime not suspended
        VirtualMachine vm = JDWPClassLoaderPatcher.hotswapClassLoader(
                System.getProperty(
                        ProcessStarter.CL_PRE_PROCESSOR_CLASSNAME_PROPERTY,
                        org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl.class.getName()
                ), (String) jdwp
                            .get(TRANSPORT_JDWP), (String) jdwp.get(ADDRESS_JDWP)
        );
        if (vm != null) {
            vm.resume();
            vm.dispose();
        }
    }


}
