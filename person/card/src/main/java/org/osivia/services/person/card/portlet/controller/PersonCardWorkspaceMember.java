package org.osivia.services.person.card.portlet.controller;

import org.osivia.directory.v2.model.ext.WorkspaceMember;

/**
 * User workspaces form
 * @author Loïc Billon
 *
 */
public class PersonCardWorkspaceMember {

	/** Workspace member (used for role)
	 */
	private WorkspaceMember member;
	
    /** Workspace title. */
    private String title;
    /** Workspace description. */
    private String description;
    /** Workspace vignetteUrl. */
    private String vignetteUrl;
    /** Worskapce link */
	private String link;
	/** Workspace id */
	private String workspaceId;
	
	public PersonCardWorkspaceMember(WorkspaceMember member) {
		this.member = member;
	}

	/**
	 * 
	 * @return
	 */
	public WorkspaceMember getMember() {
		return member;
	}

	/**
	 * 
	 * @param member
	 */
	public void setMember(WorkspaceMember member) {
		this.member = member;
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 */
	public String getVignetteUrl() {
		return vignetteUrl;
	}

	/**
	 * 
	 * @param vignetteUrl
	 */
	public void setVignetteUrl(String vignetteUrl) {
		this.vignetteUrl = vignetteUrl;
	}

	/**
	 * 
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * 
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
		
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

	
	
}
