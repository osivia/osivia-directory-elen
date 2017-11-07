package org.osivia.services.directory.various.portlet.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Duplicated groups key java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DuplicatedGroupsKey {

    /** Workspace identifier. */
    private final String workspaceId;
    /** Group label. */
    private final String groupLabel;


    /**
     * Constructor.
     * 
     * @param workspaceId workspace identifier
     * @param groupLabel group label
     */
    private DuplicatedGroupsKey(String workspaceId, String groupLabel) {
        super();
        this.workspaceId = StringUtils.lowerCase(workspaceId);
        this.groupLabel = StringUtils.lowerCase(groupLabel);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupLabel == null) ? 0 : groupLabel.hashCode());
        result = prime * result + ((workspaceId == null) ? 0 : workspaceId.hashCode());
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DuplicatedGroupsKey other = (DuplicatedGroupsKey) obj;
        if (groupLabel == null) {
            if (other.groupLabel != null)
                return false;
        } else if (!groupLabel.equals(other.groupLabel))
            return false;
        if (workspaceId == null) {
            if (other.workspaceId != null)
                return false;
        } else if (!workspaceId.equals(other.workspaceId))
            return false;
        return true;
    }


    /**
     * Getter for workspaceId.
     * 
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Getter for groupLabel.
     * 
     * @return the groupLabel
     */
    public String getGroupLabel() {
        return groupLabel;
    }

}
