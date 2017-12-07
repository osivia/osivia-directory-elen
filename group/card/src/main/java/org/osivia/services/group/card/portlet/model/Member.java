package org.osivia.services.group.card.portlet.model;

import javax.naming.Name;

import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Member implements Comparable<Member>{

    /** Identifier. */
    private final String id;
    
    private String displayName;
    
    private String givenName;
    
    private Name dn;
    
    private String avatarUrl;
    
    private String extra;
    
    private String index;
    
    /** Deleted indicator. */
    private boolean deleted;
    
    private boolean added;

    /** Person. */
    private final Person person;
    
    public Member(Person person) {
        super();
        this.person = person;
        this.id = person.getUid();
        this.displayName = person.getDisplayName();
        this.givenName = person.getGivenName();
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

    
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Member arg0) {
        if (this.givenName == null)
        {
            if (arg0 == null || arg0.givenName == null)
            {
                return 0;
            } else
            {
                return -1;
            }
        } else
        {
            if (arg0 == null || arg0.givenName == null)
            {
                return 1;
            } else
            {
                return this.getGivenName().compareTo(arg0.getGivenName());
            }
        }
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

    
    /**
     * Getter for index.
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    
    /**
     * Setter for index.
     * @param index the index to set
     */
    public void setIndex(String index) {
        this.index = index;
    }

    
    /**
     * Getter for givenName.
     * @return the givenName
     */
    public String getGivenName() {
        return givenName;
    }

    
    /**
     * Setter for givenName.
     * @param givenName the givenName to set
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    
    /**
     * Getter for added.
     * @return the added
     */
    public boolean isAdded() {
        return added;
    }

    
    /**
     * Setter for added.
     * @param added the added to set
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

}
