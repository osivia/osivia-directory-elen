package org.osivia.services.group.card.portlet.model;

import javax.naming.Name;

import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Member {

    /** Identifier. */
    private final String id;
    
    private String displayName;
    
    private Name dn;
    
    private String avatarUrl;
    
    private String extra;
    
    /** Deleted indicator. */
    private boolean deleted;

    /** Person. */
    private final Person person;
    
    public Member(Person person) {
        super();
        this.person = person;
        this.id = person.getUid();
        this.displayName = person.getDisplayName();
        if (person.getAvatar() != null) this.avatarUrl = person.getAvatar().getUrl();
        this.dn = person.getDn();
    }
    
    /**
     * Constructor used with select2 when Member its member is selected
     * 
     * @param uid person UID
     */
    public Member(String uid) {
        super();
        this.id = uid;
        this.person = null;
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


    
    /**
     * Getter for id.
     * @return the id
     */
    public String getId() {
        return id;
    }


    
    /**
     * Getter for person.
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

}
