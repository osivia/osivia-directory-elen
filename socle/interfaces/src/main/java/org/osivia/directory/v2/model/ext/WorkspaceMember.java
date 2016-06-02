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

import java.util.List;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Object representing a workspace member with his rights on a workspace and the local groups he belongs to.
 * @author Loïc Billon
 * @since 4.4
 */
public interface WorkspaceMember {

	/**
	 * @return the member
	 */
	public Person getMember();

	/**
	 * @return the role
	 */
	public WorkspaceRole getRole();

	/**
	 * @param role the role to set
	 */
	public void setRole(WorkspaceRole role);

	/**
	 * @return the localGroups
	 */
	public List<CollabProfile> getLocalGroups();

	/**
	 * @param localGroups the localGroups to set
	 */
	public void setLocalGroups(List<CollabProfile> localGroups);

}