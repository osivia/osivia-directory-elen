package org.osivia.services.group.card.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupCard {

    /** Portal group*/
    private Group group;
    
    public GroupCard() {
        super();
    }

    
    /**
     * Getter for group.
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    
    /**
     * Setter for group.
     * @param group the group to set
     */
    public void setGroup(Group group) {
        this.group = group;
    }

}
