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

import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.urls.Link;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

/**
 * ODM of a person
 * @author Loïc Billon
 * @since 4.4
 */
@Component("person")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"portalPerson"})
public class PersonImpl implements Person {

	@Id
	private Name dn;
	
	@Attribute
	private String cn;
	
	@Attribute
	private String sn;
	
	@Attribute
	private String displayName;
	
	@Attribute
	private String givenName;
	
	@Attribute
	private String mail;
	
	@Attribute
	private String title;
	
	@Attribute
	private String uid;
	
	@Attribute
	private List<Name> portalPersonProfile = new ArrayList<Name>();
	
	@Attribute
	@Transient
	private String userPassword;
	
	/** Avatar */
	@Transient
	private Link avatar = new Link("", false);

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getDn()
	 */
	@Override
	public Name getDn() {
		return dn;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setDn(javax.naming.Name)
	 */
	@Override
	public void setDn(Name dn) {
		this.dn = dn;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getCn()
	 */
	@Override
	public String getCn() {
		return cn;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setCn(java.lang.String)
	 */
	@Override
	public void setCn(String cn) {
		this.cn = cn;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getSn()
	 */
	@Override
	public String getSn() {
		return sn;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setSn(java.lang.String)
	 */
	@Override
	public void setSn(String sn) {
		this.sn = sn;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setDisplayName(java.lang.String)
	 */
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getGivenName()
	 */
	@Override
	public String getGivenName() {
		return givenName;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setGivenName(java.lang.String)
	 */
	@Override
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getMail()
	 */
	@Override
	public String getMail() {
		return mail;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setMail(java.lang.String)
	 */
	@Override
	public void setMail(String mail) {
		this.mail = mail;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getUid()
	 */
	@Override
	public String getUid() {
		return uid;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setUid(java.lang.String)
	 */
	@Override
	public void setUid(String uid) {
		this.uid = uid;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#getPortalPersonProfile()
	 */
	@Override
	public List<Name> getPortalPersonProfile() {
		return portalPersonProfile;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.model.Person#setPortalPersonProfile(java.util.List)
	 */
	@Override
	public void setPortalPersonProfile(List<Name> portalPersonProfile) {
		this.portalPersonProfile = portalPersonProfile;
	}

	/**
	 * @return the avatar
	 */
	public Link getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Link avatar) {
		this.avatar = avatar;
	}

	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.model.Person#buildDn(java.lang.String)
	 */
	@Override
	public Name buildDn(String uid) {
		return LdapNameBuilder.newInstance(System.getProperty("ldap.base")).add("ou=users").add("uid="+uid).build();
	}
	
}
