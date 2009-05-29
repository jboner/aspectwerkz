/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining;

import org.codehaus.aspectwerkz.definition.AspectDefinition;
import org.codehaus.aspectwerkz.DeploymentModel;
import org.codehaus.aspectwerkz.DeploymentModel;
import org.codehaus.aspectwerkz.aspect.container.AspectFactoryManager;
import org.codehaus.aspectwerkz.transform.inlining.spi.AspectModel;

/**
 * TODO docuemnt
 *
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public class AspectInfo {
    private final AspectDefinition m_aspectDefinition;//FIXME - remove this dependancie
    private final String m_aspectQualifiedName;
    private final String m_aspectFieldName;
    private final String m_aspectClassName;
    private final String m_aspectClassSignature;
    private final String m_aspectFactoryClassName;
    private final DeploymentModel m_deploymentModel;

    private AspectModel m_aspectModel;

    public AspectInfo(final AspectDefinition aspectDefinition,
                      final String aspectFieldName,
                      final String aspectClassName,
                      final String aspectClassSignature) {
        m_aspectDefinition = aspectDefinition;
        m_aspectQualifiedName = aspectDefinition.getQualifiedName();
        m_aspectFieldName = aspectFieldName;
        m_aspectClassName = aspectClassName;
        m_aspectClassSignature = aspectClassSignature;
        m_deploymentModel = aspectDefinition.getDeploymentModel();
        m_aspectFactoryClassName = AspectFactoryManager.getAspectFactoryClassName(m_aspectClassName, m_aspectQualifiedName);
    }

    public AspectDefinition getAspectDefinition() {
        return m_aspectDefinition;
    }

    public String getAspectClassName() {
        return m_aspectClassName;
    }

    public String getAspectQualifiedName() {
        return m_aspectQualifiedName;
    }

    public DeploymentModel getDeploymentModel() {
        return m_deploymentModel;
    }

    public String getAspectFieldName() {
        return m_aspectFieldName;
    }

    public String getAspectClassSignature() {
        return m_aspectClassSignature;
    }

    public String getAspectFactoryClassName() {
        return m_aspectFactoryClassName;
    }

    public AspectModel getAspectModel() {
        return m_aspectModel;
    }

    public void setAspectModel(AspectModel aspectModel) {
        m_aspectModel = aspectModel;
    }

    public boolean equals(Object o) {
        //TODO should we use AspectDef instead ??
        if (this == o) {
            return true;
        }
        if (!(o instanceof AspectInfo)) {
            return false;
        }

        final AspectInfo aspectInfo = (AspectInfo) o;

        if (!m_aspectQualifiedName.equals(aspectInfo.m_aspectQualifiedName)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return m_aspectQualifiedName.hashCode();
    }
}
