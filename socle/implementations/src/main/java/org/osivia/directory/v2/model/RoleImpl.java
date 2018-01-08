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

import javax.naming.InvalidNameException;
import javax.naming.Name;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

/**
 * ODM of a role
 * @author Loïc Billon
 * @since 4.4
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"groupOfUniqueNames"})
public final class RoleImpl implements Role, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 263339811067817048L;

	@Id
	private Name dn;
	
	@Attribute
	private String cn;
	
	@Attribute
	private String description;
	
	@Attribute
	private List<Name> uniqueMember = new ArrayList<Name>();

	/**
	 * @return the dn
	 */
	public Name getDn() {
		return dn;
	}

	/**
	 * @param dn the dn to set
	 */
	public void setDn(Name dn) {
		this.dn = dn;
	}

	/**
	 * @return the cn
	 */
	public String getCn() {
		return cn;
	}

	/**
	 * @param cn the cn to set
	 */
	public void setCn(String cn) {
		this.cn = cn;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the uniqueMembers
	 */
	public List<Name> getUniqueMember() {
		return uniqueMember;
	}

	/**
	 * @param uniqueMembers the uniqueMembers to set
	 */
	public void setUniqueMember(List<Name> uniqueMember) {
		this.uniqueMember = uniqueMember;
	}
	
	public Name buildBaseDn() {
		return LdapNameBuilder.newInstance(System.getProperty("ldap.base")).add("ou=groups").add("ou=roles").build();
	}
	
	public Name buildDn(String cn) {
		try {
			return buildBaseDn().add("cn="+cn);
		} catch (InvalidNameException e) {
			return null;
		}

	}


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.dn.toString();
    }

}
