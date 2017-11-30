package org.osivia.services.group.card.portlet.model;


import javax.naming.Name;

import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class GroupCardOptions {

    /** Group UID. */
    private String uid;
    /** CN */
    private String cn;
    
    private Name dn;
    /** Display Name */
    private String displayName;
    /** Description */
    private String description;
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
     * Getter for uid.
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    
    /**
     * Setter for uid.
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    
    /**
     * Getter for displayName.
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }


    
    /**
     * Setter for displayName.
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    
    /**
     * Getter for description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }


    
    /**
     * Setter for description.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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

    /**
     * Getter for dn.
     * @return the dn
     */
    public Name getDn() {
        return dn;
    }

    /**
     * Setter for dn.
     * @param dn the dn to set
     */
    public void setDn(Name dn) {
        this.dn = dn;
    }

}
