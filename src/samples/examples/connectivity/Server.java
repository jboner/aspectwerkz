/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.connectivity;

/**
 * Starts up the aspectwerkz system (which starts up the remote proxy server).
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class Server {
    public static void main(String[] args) {
        Target target = new Target();
        ((Test1) target).test1(); // to start up the AspectWerkz system, just a trigger
    }
}