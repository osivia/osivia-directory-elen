package org.osivia.services.group.card.portlet.model;


import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class GroupCardOptions {

    /** CN */
    private String cn;
    /** Group */
    private PortalGroup group;
    /** Editable group indicator. */
    private boolean editable;
    /** Deletable group indicator. */
    private boolean deletable;
    
    public GroupCardOptions() {
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

    
    /**
     * Getter for editable.
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    
    /**
     * Setter for editable.
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    
    /**
     * Getter for deletable.
     * @return the deletable
     */
    public boolean isDeletable() {
        return deletable;
    }

    
    /**
     * Setter for deletable.
     * @param deletable the deletable to set
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    /**
     * Getter for cn.
     * @return the cn
     */
    public String getCn() {
        return cn;
    }
    
    /**
     * Setter for cn.
     * @param cn the cn to set
     */
    public void setCn(String cn) {
        this.cn = cn;
    }

}
