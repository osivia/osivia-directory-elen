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

import javax.naming.Name;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.IDirService;
import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Service class used to manager workspace groups.
 *
 * @author Loïc Billon
 * @since 4.4
 * @see IDirService
 */
public interface WorkspaceService extends IDirService {

    /**
     * Get empty profile for searching.
     *
     * @return empty profile
     */
    CollabProfile getEmptyProfile();


    /**
     * Get a collab profile.
     *
     * @param cn collab profile CN
     * @return collab profile
     */
    CollabProfile getProfile(String cn);


    /**
     * Get a collab profile.
     *
     * @param dn collab profile DN
     * @return collab profile
     */
    CollabProfile getProfile(Name dn);


    /**
     * Get all members, rights and local groups.
     *
     * @param workspaceId workspace identifier
     * @return members
     */
    List<WorkspaceMember> getAllMembers(String workspaceId);

    /**
     * Get all members, rights and local groups, and person card urls.
     *
     * @param workspaceId workspace identifier
     * @return members
     * @throws PortalException 
     */
	List<WorkspaceMember> getAllMembers(PortalControllerContext pcc,
			String workspaceId) throws PortalException;
	
	
    /**
     * Get workspace member, from his UID.
     * 
     * @param workspaceId workspace identifier
     * @param uid person UID
     * @return workspace member
     */
    WorkspaceMember getMember(String workspaceId, String uid);


    /**
     * Create a workspace with all default roles.
     *
     * @param workspaceId workspace identifier
     * @param owner workspace owner
     */
    void create(String workspaceId, Person owner);


    /**
     * Create a workspace with specified roles.
     *
     * @param workspaceId workspace identifier
     * @param roles workspace roles
     * @param owner workspace owner
     */
    void create(String workspaceId, List<WorkspaceRole> roles, Person owner);


    /**
     * Delete a workspace and his subgroups, unkink users associated.
     *
     * @param workspaceId workspace identifier
     */
    void delete(String workspaceId);


    /**
     * Add a person to a workspace, change his level rights.
     *
     * @param workspaceId workspace identifier
     * @param memberDn member DN
     * @param role workspace role
     */
    WorkspaceMember addOrModifyMember(String workspaceId, Name memberDn, WorkspaceRole role);


    /**
     * Remove a person from a workspace.
     *
     * @param workspaceId workspace identifier
     * @param memberDn member DN
     */
    public void removeMember(String workspaceId, Name memberDn);


    /**
     * Find groups by workspace identifier.
     *
     * @param workspaceId workspace identifier
     * @return searched groups
     */
    List<CollabProfile> findByWorkspaceId(String workspaceId);


    /**
     * Find groups by criteria object.
     *
     * @param profile criteria
     * @return searched groups
     */
    List<CollabProfile> findByCriteria(CollabProfile profile);


    /**
     * Create a local group.
     *
     * @param workspaceId workspace identifier
     * @param displayName workspace display name
     * @param description workspace description
     */
    CollabProfile createLocalGroup(String workspaceId, String displayName, String description);


    /**
     * Remove a local group.
     *
     * @param workspaceId workspace identifier
     * @param dn local group DN
     */
    void removeLocalGroup(String workspaceId, Name dn);


    /**
     * Remove local group.
     *
     * @param workspaceId workspace identifier
     * @param cn local group CN
     */
    void removeLocalGroup(String workspaceId, String cn);


    /**
     * Add member to a local group (should be already in the member group).
     *
     * @param workspaceId workspace identifier
     * @param localGroupDn local group DN
     * @param memberDn member DN
     */
    void addMemberToLocalGroup(String workspaceId, Name localGroupDn, Name memberDn);


    /**
     * Add member to local group.
     * 
     * @param workspaceId workspace identifier
     * @param localGroupCn local group CN
     * @param memberUid member UID
     */
    void addMemberToLocalGroup(String workspaceId, String localGroupCn, String memberUid);


    /**
     * Modify local group properties (name, description).
     *
     * @param localGroup the local group with updated fields
     */
    void modifyLocalGroup(CollabProfile localGroup);


    /**
     * Remove member from a local group.
     *
     * @param workspaceId workspace identifier
     * @param localGroupDn local group DN
     * @param memberDn member DN
     */
    void removeMemberFromLocalGroup(String workspaceId, Name localGroupDn, Name memberDn);


    /**
     * Remove member from a local group.
     * 
     * @param workspaceId workspace identifier
     * @param localGroupCn local group CN
     * @param memberUid member UID
     */
    void removeMemberFromLocalGroup(String workspaceId, String localGroupCn, String memberUid);




}
