package org.osivia.services.directory.workspace.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Local group edition form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroup
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocalGroupEditionForm extends LocalGroup {

    /** Workspace identifier. */
    private String workspaceId;
    /** Members. */
    private List<Member> members;
    /** Added member. */
    private Member addedMember;


    /**
     * Constructor.
     */
    public LocalGroupEditionForm() {
        super();
    }


    /**
     * Getter for workspaceId.
     * 
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Setter for workspaceId.
     * 
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
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

    /**
     * Getter for addedMember.
     * 
     * @return the addedMember
     */
    public Member getAddedMember() {
        return addedMember;
    }

    /**
     * Setter for addedMember.
     * 
     * @param addedMember the addedMember to set
     */
    public void setAddedMember(Member addedMember) {
        this.addedMember = addedMember;
    }

}
