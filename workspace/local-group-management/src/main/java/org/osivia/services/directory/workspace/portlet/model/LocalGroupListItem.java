package org.osivia.services.directory.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Local group list item java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroup
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocalGroupListItem extends LocalGroup {

    /** Members count. */
    private int membersCount;
    /** Deleted indicator. */
    private boolean deleted;


    /**
     * Constructor.
     */
    public LocalGroupListItem() {
        super();
    }


    /**
     * Getter for membersCount.
     * 
     * @return the membersCount
     */
    public int getMembersCount() {
        return membersCount;
    }

    /**
     * Setter for membersCount.
     * 
     * @param membersCount the membersCount to set
     */
    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    /**
     * Getter for deleted.
     * 
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Setter for deleted.
     * 
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
