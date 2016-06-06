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
package org.osivia.directory.v2.service;

import java.util.List;
import java.util.Map;

import javax.naming.Name;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.directory.v2.IDirService;
import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Service class used to manager workspace groups
 * @author Loïc Billon
 * @since 4.4
 */
public interface WorkspaceService extends IDirService {

	/**
	 * Get all members, rights and local groups
	 * @param cn
	 * @return members
	 */
	public List<WorkspaceMember> getAllMembers(String cn);
	
	/**
	 * Create a workspace with all default roles
	 * @param workspaceId
	 * @param description
	 * @param owner
	 */
	public void create(String workspaceId, String description, Person owner);
	
	/**
	 * Create a workspace with specified roles
	 * @param workspaceId
	 * @param description
	 * @param roles
	 * @param owner
	 */
	public void create(String workspaceId, String description,
			Map<WorkspaceRole, String> roles, Person owner);	
	
	/**
	 * Delete a workspace and his subgroups, unkink users associated.
	 * @param workspaceId
	 */
	void delete(String workspaceId);
	
	/**
	 * Add a person to a workspace, change his level rights.
	 * @param workspaceId
	 * @param memberDn
	 * @param role
	 */
	public void addOrModifyMember(String workspaceId, Name memberDn, WorkspaceRole role);
	
	/**
	 * Remove a person from a workspace
	 * @param workspaceId
	 * @param memberDn
	 */
	public void removeMember(String workspaceId, Name memberDn);


	/**
	 * Find groups by workspace id
	 * @param workspaceId
	 * @return searched groups
	 */
	List<CollabProfile> findByWorkspaceId(String workspaceId);
	
	/**
	 * Find groups by criteria object
	 * @param profile
	 * @return searched groups
	 */
	List<CollabProfile> findByCriteria(CollabProfile profile);

	/**
	 * Create a local group
	 * @param workspaceId
	 * @param description
	 */
	void createLocalGroup(String workspaceId, String description);

	/**
	 * Remove a local group
	 * @param workspaceId
	 * @param dn
	 */
	void removeLocalGroup(String workspaceId, Name dn);

	/**
	 * Add member to a local group (should be already in the member group)
	 * @param workspaceId
	 * @param localGroupDn
	 * @param member
	 */
	void addMemberToLocalGroup(String workspaceId, Name localGroupDn,
			Name member);

	/**
	 * Remove member from a local group.
	 * @param workspaceId
	 * @param localGroupDn
	 * @param member
	 */
	void removeMemberFromLocalGroup(String workspaceId,Name localGroupDn,
			Name member);


	
}
