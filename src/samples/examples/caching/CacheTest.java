/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package examples.caching;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class CacheTest {
    public static void main(String[] args) {
        Pi pi = new Pi();
        pi.getPiDecimal(3);
        pi.getPiDecimal(4);
        pi.getPiDecimal(3);
        int methodInvocations = CacheStatistics.getNrOfMethodInvocationsFor(
                "getPiDecimal",
                new Class[]{
                    int.class
                }
        );
        int cacheInvocations = CacheStatistics.getNrOfCacheInvocationsFor(
                "getPiDecimal",
                new Class[]{
                    int.class
                }
        );
        if (cacheInvocations > 0) {
            double hitRate = methodInvocations / cacheInvocations;
            System.out.println("Hit rate: " + hitRate);
        } else {
            System.out.println("Hit rate: unavailable");
        }
    }
}