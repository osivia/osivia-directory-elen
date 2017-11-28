package org.osivia.services.group.card.portlet.model;

import org.osivia.directory.v2.model.PortalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class GroupCard {

    /** Portal group*/
    @Autowired
    @Qualifier("portalGroup")
    private PortalGroup group;
    
    public GroupCard() {
        super();
    }

    
    /**
     * Getter for group.
     * @return the group
     */
    public PortalGroup getGroup() {
        return group;
    }

    
    /**
     * Setter for group.
     * @param group the group to set
     */
    public void setGroup(PortalGroup group) {
        this.group = group;
    }

}
