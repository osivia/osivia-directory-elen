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

    /** CN */
    private String cn;
    /** DN */
    private Name dn;
    /** Group */
    private PortalGroup group;
    /** Editable group indicator. */
    private boolean editable;
    
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
