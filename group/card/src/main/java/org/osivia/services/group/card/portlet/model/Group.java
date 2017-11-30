package org.osivia.services.group.card.portlet.model;

import java.util.List;

public class Group {

    /** Dipslay name */
    private String displayName;
    /** Description */
    private String description;
    /** Members list */
    private List<Member> members;
    
    public Group() {
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
     * Getter for members.
     * @return the members
     */
    public List<Member> getMembers() {
        return members;
    }

    
    /**
     * Setter for members.
     * @param members the members to set
     */
    public void setMembers(List<Member> members) {
        this.members = members;
    }

}
