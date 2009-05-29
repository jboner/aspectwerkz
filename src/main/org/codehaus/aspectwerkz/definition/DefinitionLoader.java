/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.definition;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Set;
import java.util.HashSet;

/**
 * TODO remove class and move single method to SystemDefinitionContainer?s
 * <p/>
 * Handles the loading of the definition in various ways and formats.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 */
public class DefinitionLoader {
    /**
     * The UUID of the single AspectWerkz system if only one definition is used.
     */
    public static final String DEFAULT_SYSTEM = "default";

    /**
     * The path to the definition file.
     */
    public static final String DEFINITION_FILE = System.getProperty("aspectwerkz.definition.file", null);

    /**
     * The default name for the definition file.
     */
    public static final String DEFAULT_DEFINITION_FILE_NAME = "aspectwerkz.xml";

    /**
     * Returns the default defintion.
     *
     * @param loader
     * @return the default defintion
     */
    public static Set getDefaultDefinition(final ClassLoader loader) {
        if (DEFINITION_FILE != null) {
            File file = new File(DEFINITION_FILE);
            if (file.canRead()) {
                try {
                    return XmlParser.parseNoCache(loader, file.toURL());
                } catch (MalformedURLException e) {
                    System.err.println("<WARN> Cannot read " + DEFINITION_FILE);
                    e.printStackTrace();
                }
            } else {
                System.err.println("<WARN> Cannot read " + DEFINITION_FILE);
            }
        }
        return new HashSet();
    }
}