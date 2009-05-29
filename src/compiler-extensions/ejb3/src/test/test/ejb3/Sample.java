/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.ejb3;

import org.codehaus.aspectwerkz.ejb3.EJBInterceptorDeployer;

/**
 * Sample app that runs outside of the container hence deploy its own EJBs
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class Sample {

    static {
        EJBInterceptorDeployer.deploy("test.ejb3.MyEJBIsTheAspect", Sample.class.getClassLoader());
    }

    public static void main(String args[]) throws Throwable {
        // some one would do a lookup or inject for that when in the container
        MyEJBIsTheAspect myEjb = new MyEJBIsTheAspect();

        System.out.println("Calling the EJB");
        int i = myEjb.businessSum(1, 2);
        System.out.println("got : " + i);

        System.out.println("Calling the EJB");
        int j = myEjb.businessSubstract(1, 2);
        System.out.println("got : " + j);
    }

}
