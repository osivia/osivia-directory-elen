package org.osivia.services.group.card.portlet.model;

import javax.naming.Name;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Member {

    private String displayName;
    
    private Name dn;
    
    private String avatarUrl;
    
    private String extra;
    
    /** Deleted indicator. */
    private boolean deleted;
    
    public Member() {
        super();
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
     * Getter for avatarUrl.
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }


    
    /**
     * Setter for avatarUrl.
     * @param avatarUrl the avatarUrl to set
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    /**
     * Getter for extra.
     * @return the extra
     */
    public String getExtra() {
        return extra;
    }


    
    /**
     * Setter for extra.
     * @param extra the extra to set
     */
    public void setExtra(String extra) {
        this.extra = extra;
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


    
    /**
     * Getter for deleted.
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }


    
    /**
     * Setter for deleted.
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
