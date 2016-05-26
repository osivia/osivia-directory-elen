package org.osivia.services.directory.workspace.model;

/**
 * Workspace member java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class Member {

    /** Identifier. */
    private String id;
    /** Avatar URL. */
    private String avatar;
    /** Display name. */
    private String displayName;
    /** Mail. */
    private String mail;
    /** Deleted indicator. */
    private boolean deleted;


    /**
     * Constructor.
     */
    public Member() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Member other = (Member) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }


    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
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
     * Getter for avatar.
     *
     * @return the avatar
     */
    public String getAvatar() {
        return this.avatar;
    }

    /**
     * Setter for avatar.
     *
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Getter for displayName.
     *
     * @return the displayName
     */
    public String getDisplayName() {
        return this.displayName;
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
     * Getter for mail.
     *
     * @return the mail
     */
    public String getMail() {
        return this.mail;
    }

    /**
     * Setter for mail.
     *
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Getter for deleted.
     *
     * @return the deleted
     */
    public boolean isDeleted() {
        return this.deleted;
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
