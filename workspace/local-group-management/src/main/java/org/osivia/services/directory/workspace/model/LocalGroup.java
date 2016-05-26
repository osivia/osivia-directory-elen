package org.osivia.services.directory.workspace.model;

import java.util.List;

/**
 * Local group java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class LocalGroup {

    /** Identifier. */
    private String id;
    /** Display name. */
    private String displayName;
    /** Members. */
    private List<Member> members;
    /** Added members. */
    private List<String> addedMembers;
    /** Deleted indicator. */
    private boolean deleted;


    /**
     * Constructor.
     */
    public LocalGroup() {
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
        LocalGroup other = (LocalGroup) obj;
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
     * Getter for members.
     *
     * @return the members
     */
    public List<Member> getMembers() {
        return this.members;
    }

    /**
     * Setter for members.
     *
     * @param members the members to set
     */
    public void setMembers(List<Member> members) {
        this.members = members;
    }

    /**
     * Getter for addedMembers.
     *
     * @return the addedMembers
     */
    public List<String> getAddedMembers() {
        return this.addedMembers;
    }

    /**
     * Setter for addedMembers.
     *
     * @param addedMembers the addedMembers to set
     */
    public void setAddedMembers(List<String> addedMembers) {
        this.addedMembers = addedMembers;
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
