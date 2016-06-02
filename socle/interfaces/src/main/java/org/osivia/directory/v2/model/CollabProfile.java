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
package org.osivia.directory.v2.model;

import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceRole;


/**
 * LDAP group used to manage workspace rights and access
 * @author Loïc Billon
 * @since 4.4
 */
public interface CollabProfile {

	/**
	 * 
	 * @param type
	 */
	public void setType(WorkspaceGroupType type);

	/**
	 * 
	 * @return
	 */
	public WorkspaceGroupType getType();

	/**
	 * 
	 * @param explicitManager
	 */
	public void setExplicitManager(List<Name> explicitManager);

	/**
	 * 
	 * @return
	 */
	public List<Name> getExplicitManager();

	/**
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName);

	/**
	 * 
	 * @return
	 */
	public String getDisplayName();

	/**
	 * 
	 * @param workspaceId
	 */
	public void setWorkspaceId(String workspaceId);

	/**
	 * 
	 * @return
	 */
	public String getWorkspaceId();

	/**
	 * 
	 * @param explicitMember
	 */
	public void setExplicitMember(List<Name> explicitMember);

	/**
	 * 
	 * @return
	 */
	public List<Name> getExplicitMember();

	/**
	 * 
	 * @param uniqueMember
	 */
	public void setUniqueMember(List<Name> uniqueMember);

	/**
	 * 
	 * @return
	 */
	public List<Name> getUniqueMember();

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description);

	/**
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * 
	 * @param cn
	 */
	public void setCn(String cn);

	/**
	 * 
	 * @return
	 */
	public String getCn();

	/**
	 * 
	 * @param dn
	 */
	public void setDn(Name dn);

	/**
	 * 
	 * @return
	 */
	public Name getDn();

	/**
	 * @return
	 */
	WorkspaceRole getRole();

	/**
	 * @param role
	 */
	void setRole(WorkspaceRole role);
	

	/**
	 * 
	 * @param cn
	 * @return
	 */
	public Name buildDn(String cn);

}
