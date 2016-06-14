package org.osivia.services.directory.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.ext.WorkspaceMember;

/**
 * Workspace members container java-bean.
 * 
 * @author Cédric Krommenhoek
 */
public class MembersContainer {

	/** Workspace Id */
	private String workspaceId;
	
    /** Members. */
    private List<WorkspaceMember> members;


    /**
     * Constructor.
     */
    public MembersContainer(String workspaceId) {
    	
    	
        super();
        
        this.workspaceId = workspaceId;
    }

    
    

    /**
	 * @return the workspaceId
	 */
	public String getWorkspaceId() {
		return workspaceId;
	}




	/**
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
    public List<WorkspaceMember> getMembers() {
        return members;
    }

    /**
     * Setter for members.
     * 
     * @param members the members to set
     */
    public void setMembers(List<WorkspaceMember> members) {
        this.members = members;
    }

}
