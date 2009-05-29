/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Starts a target process adding JDWP option to have a listening connector and be in suspend mode <p/>Target process is
 * launched using <i>$JAVA_HOME/bin/java [opt] [main] </i> <br/>and [opt] is patched to use <i>-Xdebug
 * -Xrunjdwp:transport=..,address=..,server=y,suspend=y </i> <br/>
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class JDWPStarter extends AbstractStarter {
    private String transport;

    private String address;

    public JDWPStarter(String opt, String main, String transport, String address) {
        super(opt, main);
        Map jdwpOpt = parseJdwp();
        if (jdwpOpt.containsKey("transport")) {
            this.transport = (String) jdwpOpt.get("transport");
        } else {
            this.transport = transport;
            jdwpOpt.put("transport", this.transport);
        }
        if (jdwpOpt.containsKey("address")) {
            this.address = (String) jdwpOpt.get("address");
        } else {
            this.address = address;
            jdwpOpt.put("address", this.address);
        }
        patchOptions(jdwpOpt);
    }

    public String getTransport() {
        return transport;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Patch JDWP options if any to include necessary information Preserve JDWP options excepted server and suspend.
     * <br/>If transport and address are already specified it uses them.
     */
    private void patchOptions(Map jdwpOpt) {
        if (opt.indexOf("-Xdebug") < 0) {
            opt = "-Xdebug " + opt;
        }
        jdwpOpt.put("server", "y");
        jdwpOpt.put("suspend", "y");
        StringBuffer jdwp = new StringBuffer("-Xrunjdwp:");
        List keys = new ArrayList(jdwpOpt.keySet());

        // JDWP options should start with transport=..,address=..
        // or it fails strangely
        //Collections.reverse(keys);
        keys = jdwpOptionSort(keys);
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = (String) i.next();
            jdwp.append(key).append("=").append((String) jdwpOpt.get(key));
            if (i.hasNext()) {
                jdwp.append(",");
            }
        }
        if (opt.indexOf("-Xrunjdwp:") < 0) {
            opt = jdwp + " " + opt;
        } else {
            int from = opt.indexOf("-Xrunjdwp:");
            int to = Math.min(opt.length(), opt.indexOf(' ', from));
            StringBuffer newOpt = new StringBuffer("");
            if (from > 0) {
                newOpt.append(opt.substring(0, from));
            }
            newOpt.append(" ").append(jdwp);
            if (to < opt.length()) {
                newOpt.append(" ").append(opt.substring(to, opt.length()));
            }
            opt = newOpt.toString();
        }
    }

    /**
     * return a Map(String=>String) of JDWP options
     */
    private Map parseJdwp() {
        if (opt.indexOf("-Xrunjdwp:") < 0) {
            return new HashMap();
        }
        String jdwp = opt.substring(
                opt.indexOf("-Xrunjdwp:") + "-Xrunjdwp:".length(), Math.min(
                        opt.length(), opt
                                      .indexOf(' ', opt.indexOf("-Xrunjdwp:"))
                )
        );
        HashMap jdwpOpt = new HashMap();
        StringTokenizer stz = new StringTokenizer(jdwp, ",");
        while (stz.hasMoreTokens()) {
            String jdwpo = stz.nextToken();
            if (jdwpo.indexOf('=') < 0) {
                System.err.println("WARN - unrecognized JDWP option: " + jdwpo);
                continue;
            }
            jdwpOpt.put(jdwpo.substring(0, jdwpo.indexOf('=')), jdwpo.substring(jdwpo.indexOf('=') + 1));
        }
        return jdwpOpt;
    }

    /**
     * Sort list of String for "transport" to be in first position
     */
    private List jdwpOptionSort(List opt) {
        Comparator c = new Comparator() {
            public int compare(Object o1, Object o2) {
                if (o1 instanceof String && o2 instanceof String) {
                    if ("transport".equals((String) o1)) {
                        return -1000;
                    }
                    if ("transport".equals((String) o2)) {
                        return 1000;
                    }
                    return 0;
                }
                return 0;
            }
        };
        Collections.sort(opt, c);
        return opt;
    }
}