/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.connectivity;

import org.codehaus.aspectwerkz.connectivity.RemoteProxy;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Client {

    public static void main(String[] args) {
        run();
    }

    /**
     * This example shows two ways of using the remote proxy feature: <p/>1. It creates a client
     * proxy that creates a matching instance on the server. The client now has seamless access this
     * new instance on the server. <p/>2. The instance on the server creates a new proxy to another
     * specific instance on and sends this proxy to the client. The client then have access to this
     * specific instance. (Proxy created on the server-side using:
     * <code>RemoteProxy proxy = RemoteProxy.createServerProxy(myInstance, "localhost", 7777);</code>)
     */
    private static void run() {
        // 1)
        // creates a new remote proxy for the TestImpl1 class which maps to an instance of this
        // class on the server
        RemoteProxy proxy1 = RemoteProxy.createClientProxy(
                new String[]{
                    "examples.connectivity.Test1"
                }, "examples.connectivity.Test1Impl", "localhost", 6663
        );
        // retrieves the proxy the the TestImpl1 instance
        Test1 mixin1 = (Test1) proxy1.getInstance();

        // 2)
        // retrieve the proxy to a specific instance created on the server
        RemoteProxy proxy2 = mixin1.getTest1();
        // retrieves the proxy the the TestImpl2 instance
        Test2 mixin2 = (Test2) proxy2.getInstance();

        // 3)
        // invoke methods on the proxies (executed on the server)
        System.out.println("Mixin1 says: " + mixin1.test1());
        System.out.println("Mixin2 says: " + mixin2.test2());

        // 4)
        // close the proxies (close() must always be called)
        proxy1.close();
        proxy2.close();
    }
}