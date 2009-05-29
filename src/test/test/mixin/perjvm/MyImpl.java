/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.mixin.perjvm;

import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;
import org.codehaus.aspectwerkz.definition.MixinDefinition;

import java.util.Map;
import java.util.Iterator;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class MyImpl implements Introductions {

    public static int s_count = 0;

    public static Map s_params;

    public MyImpl() {
        s_count++;

        // access the param
        SystemDefinition def = SystemDefinitionContainer.getDefinitionFor(
                this.getClass().getClassLoader(),
                "tests"
        );
        for (Iterator iterator = def.getMixinDefinitions().iterator(); iterator.hasNext();) {
            MixinDefinition mixinDefinition = (MixinDefinition) iterator.next();
            if (mixinDefinition.getMixinImpl().getName().equals(this.getClass().getName().replace('/','.'))) {
                s_params = mixinDefinition.getParameters();
                break;
            }
        }
    }

    public void NOT_IN_MIXIN_INTF() {
    }

    public void noArgs() {
        return;
    }

    public int intArg(int arg) {
        return arg;
    }

}

