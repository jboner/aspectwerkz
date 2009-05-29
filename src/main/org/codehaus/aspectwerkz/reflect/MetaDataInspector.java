/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect;

/**
 * Inspects info.
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class MetaDataInspector {
    /**
     * Checks if a class has a certain field.
     *
     * @param classInfo
     * @param fieldName
     * @return
     */
    public static boolean hasField(final ClassInfo classInfo, final String fieldName) {
        for (int i = 0; i < classInfo.getFields().length; i++) {
            FieldInfo fieldMetaData = classInfo.getFields()[i];
            if (fieldMetaData.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a class implements a certain interface.
     *
     * @param classInfo
     * @param interfaceName
     * @return
     */
    public static boolean hasInterface(final ClassInfo classInfo, final String interfaceName) {
        for (int i = 0; i < classInfo.getInterfaces().length; i++) {
            ClassInfo interfaceMetaData = classInfo.getInterfaces()[i];
            if (interfaceMetaData.getName().equals(interfaceName)) {
                return true;
            }
        }
        return false;
    }
}