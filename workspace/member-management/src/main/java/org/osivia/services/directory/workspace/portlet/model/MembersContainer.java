package org.osivia.services.directory.workspace.portlet.model;

import java.util.List;

/**
 * Workspace members container java-bean.
 * 
 * @author Cédric Krommenhoek
 */
public class MembersContainer {

    /** Members. */
    private List<Member> members;


    /**
     * Constructor.
     */
    public MembersContainer() {
        super();
    }


    /**
     * Getter for members.
     * 
     * @return the members
     */
    public List<Member> getMembers() {
        return members;
    }

    /**
     * Setter for members.
     * 
     * @param members the members to set
     */
    public void setMembers(List<Member> members) {
        this.members = members;
    }

}
