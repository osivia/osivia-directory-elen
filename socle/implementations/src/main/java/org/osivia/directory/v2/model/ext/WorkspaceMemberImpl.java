/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.osivia.directory.v2.model.ext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.urls.Link;

/**
 * Impl of a workspace member
 * @author Loïc Billon
 * @since 4.4
 */
public class WorkspaceMemberImpl implements WorkspaceMember, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6982278397728977829L;
	

	private final Person member;
	
	private Link cardUrl;
	
	private WorkspaceRole role;
	
	private List<CollabProfile> localGroups = new ArrayList<CollabProfile>();

    /** Deleted indicator. */
    private boolean deleted;
        
	/**
	 * 
	 */
	public WorkspaceMemberImpl(Person member) {
		this.member = member;
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.ext.WorkspaceMember#getMember()
	 */
	@Override
	public Person getMember() {
		return member;
	}
	
	

	public Link getCard() {
		return cardUrl;
	}

	public void setCard(Link cardUrl) {
		this.cardUrl = cardUrl;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.ext.WorkspaceMember#getRole()
	 */
	@Override
	public WorkspaceRole getRole() {
		return role;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.ext.WorkspaceMember#setRole(org.osivia.directory.v2.model.Workspace.WorkspaceRole)
	 */
	@Override
	public void setRole(WorkspaceRole role) {
		this.role = role;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.ext.WorkspaceMember#getLocalGroups()
	 */
	@Override
	public List<CollabProfile> getLocalGroups() {
		return localGroups;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.ext.WorkspaceMember#setLocalGroups(java.util.List)
	 */
	@Override
	public void setLocalGroups(List<CollabProfile> localGroups) {
		this.localGroups = localGroups;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
}
