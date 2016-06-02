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

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

/**
 * ODM of a collaborative group profile
 * @author Loïc Billon
 * @since 4.4
 */
@Component("collabProfile")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"portalCollabProfile"})
public class CollabProfileImpl implements CollabProfile {


	@Id
	private Name dn;
	
	@Attribute
	private String cn;
	
	@Attribute
	private String description;
	
	@Attribute
	private List<Name> uniqueMember = new ArrayList<Name>();;
	
	@Attribute(name="portalExplicitMember")
	private List<Name> explicitMember = new ArrayList<Name>();;	
	
	@Attribute(name="portalCollabWorkspaceId")
	private String workspaceId;
	
	@Attribute(name="portalDisplayName")
	private String displayName;
	
	@Attribute(name="portalExplicitManager")
	private List<Name> explicitManager = new ArrayList<Name>();
	
	@Attribute(name="portalProfileType")
	private WorkspaceGroupType type;
	
	@Attribute(name="portalCollabWorkspaceRole")
	private WorkspaceRole role;
	
	@Attribute
	@Transient
	private Long modifyTimeStamp;

	/**
	 * @return the dn
	 */
	@Override
	public Name getDn() {
		return dn;
	}

	/**
	 * @param dn the dn to set
	 */
	@Override
	public void setDn(Name dn) {
		this.dn = dn;
	}

	/**
	 * @return the cn
	 */
	@Override
	public String getCn() {
		return cn;
	}

	/**
	 * @param cn the cn to set
	 */
	@Override
	public void setCn(String cn) {
		this.cn = cn;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the uniqueMembers
	 */
	@Override
	public List<Name> getUniqueMember() {
		return uniqueMember;
	}

	/**
	 * @param uniqueMembers the uniqueMembers to set
	 */
	@Override
	public void setUniqueMember(List<Name> uniqueMember) {
		this.uniqueMember = uniqueMember;
	}
	
	
	
	/**
	 * @return the explicitMember
	 */
	@Override
	public List<Name> getExplicitMember() {
		return explicitMember;
	}

	/**
	 * @param explicitMember the explicitMember to set
	 */
	@Override
	public void setExplicitMember(List<Name> explicitMember) {
		this.explicitMember = explicitMember;
	}

	/**
	 * @return the workspaceId
	 */
	@Override
	public String getWorkspaceId() {
		return workspaceId;
	}

	/**
	 * @param workspaceId the workspaceId to set
	 */
	@Override
	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	/**
	 * @return the displayName
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the explicitManager
	 */
	@Override
	public List<Name> getExplicitManager() {
		return explicitManager;
	}

	/**
	 * @param explicitManager the explicitManager to set
	 */
	@Override
	public void setExplicitManager(List<Name> explicitManager) {
		this.explicitManager = explicitManager;
	}

	/**
	 * @return the type
	 */
	@Override
	public WorkspaceGroupType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	@Override
	public void setType(WorkspaceGroupType type) {
		this.type = type;
	}
	
	
	/**
	 * @return the role
	 */
	public WorkspaceRole getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(WorkspaceRole role) {
		this.role = role;
	}
	
	

	/**
	 * @return the modifyTimeStamp
	 */
	public Long getModifyTimeStamp() {
		return modifyTimeStamp;
	}

	/**
	 * @param modifyTimeStamp the modifyTimeStamp to set
	 */
	public void setModifyTimeStamp(Long modifyTimeStamp) {
		this.modifyTimeStamp = modifyTimeStamp;
	}

	@Override
	public Name buildDn(String cn) {
		return LdapNameBuilder.newInstance(System.getProperty("ldap.base")).add("ou=groups").add("ou=collabProfiles").add("cn="+cn).build();
	}
}
