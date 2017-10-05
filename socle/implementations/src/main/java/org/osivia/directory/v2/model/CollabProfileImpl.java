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

import java.io.Serializable;
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
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

/**
 * ODM of a collaborative group profile
 * 
 * @author Loïc Billon
 * @since 4.4
 */
@Component("collabProfile")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"portalCollabProfile"})
public final class CollabProfileImpl implements CollabProfile, Serializable {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;


    /** DN. */
    @Id
    private Name dn;

    /** CN. */
    @Attribute
    private String cn;

    /** Description. */
    @Attribute
    private String description;

    /** Unique member. */
    @Attribute
    private List<Name> uniqueMember;

    /** Explicit member. */
    @Attribute(name = "portalExplicitMember")
    private List<Name> explicitMember;

    /** Workspace identifier. */
    @Attribute(name = "portalCollabWorkspaceId")
    private String workspaceId;

    /** Display name (useful for local group). */
    @Attribute(name = "portalDisplayName")
    private String displayName;

    /** Explicit manager. */
    @Attribute(name = "portalExplicitManager")
    private List<Name> explicitManager;

    /** Profile type. */
    @Attribute(name = "portalProfileType")
    private WorkspaceGroupType type;

    /** Profile role. */
    @Attribute(name = "portalCollabWorkspaceRole")
    private WorkspaceRole role;


    /**
     * Constructor.
     */
    public CollabProfileImpl() {
        super();
        this.uniqueMember = new ArrayList<Name>();
        this.explicitMember = new ArrayList<Name>();
        this.explicitManager = new ArrayList<Name>();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Name getDn() {
        return dn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setDn(Name dn) {
        this.dn = dn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getCn() {
        return cn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setCn(String cn) {
        this.cn = cn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Name> getUniqueMember() {
        return uniqueMember;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniqueMember(List<Name> uniqueMember) {
        this.uniqueMember = uniqueMember;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Name> getExplicitMember() {
        return explicitMember;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setExplicitMember(List<Name> explicitMember) {
        this.explicitMember = explicitMember;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceId() {
        return workspaceId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Name> getExplicitManager() {
        return explicitManager;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setExplicitManager(List<Name> explicitManager) {
        this.explicitManager = explicitManager;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WorkspaceGroupType getType() {
        return type;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setType(WorkspaceGroupType type) {
        this.type = type;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WorkspaceRole getRole() {
        return role;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setRole(WorkspaceRole role) {
        this.role = role;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Name buildDn(String cn) {
        return LdapNameBuilder.newInstance(System.getProperty("ldap.base")).add("ou=groups").add("ou=collabProfiles").add("cn=" + cn).build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.dn.toString();
    }

}
