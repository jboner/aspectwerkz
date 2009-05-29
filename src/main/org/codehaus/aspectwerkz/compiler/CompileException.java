/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.compiler;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An exception occured during compilation
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class CompileException extends Exception {
    private Throwable nested;

    public CompileException(String msg, Throwable e) {
        super(msg);
        nested = e;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
        if (nested != null) {
            writer.println("nested:");
            nested.printStackTrace(writer);
        }
    }

    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        if (nested != null) {
            out.println("nested:");
            nested.printStackTrace(out);
        }
    }
}