package org.osivia.services.person.management.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User java-bean.
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class User {
    
    /** Identifier. */
    private String id;
    /** Display name. */
    private String displayName;
    /** Extra informations. */
    private String extra;
    /** Avatar URL. */
    private String avatarUrl;


    /**
     * Constructor.
     */
    public User() {
        super();
    }


    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id.
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for displayName.
     * 
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter for displayName.
     * 
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter for extra.
     * 
     * @return the extra
     */
    public String getExtra() {
        return extra;
    }

    /**
     * Setter for extra.
     * 
     * @param extra the extra to set
     */
    public void setExtra(String extra) {
        this.extra = extra;
    }

    /**
     * Getter for avatarUrl.
     * 
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Setter for avatarUrl.
     * 
     * @param avatarUrl the avatarUrl to set
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
