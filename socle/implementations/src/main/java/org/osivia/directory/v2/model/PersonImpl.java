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
import java.util.Date;
import java.util.List;

import javax.naming.Name;

import org.apache.commons.lang.StringUtils;
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
 * 
 * @author Loïc Billon
 * @since 4.4
 */
@Component("person")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"portalPerson"})
public final class PersonImpl implements Person, Serializable {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** DN. */
    @Id
    private Name dn;

    /** CN. */
    @Attribute
    private String cn;

    /** SN. */
    @Attribute
    private String sn;

    /** Display name. */
    @Attribute
    private String displayName;

    /** Given name. */
    @Attribute
    private String givenName;

    /** Email. */
    @Attribute
    private String mail;

    /** Title. */
    @Attribute
    private String title;

    /** UID. */
    @Attribute
    private String uid;

    /** Profiles. */
    @Attribute(name = "portalPersonProfile")
    private List<Name> profiles;

    /** User password. */
    @Attribute
    @Transient
    private String userPassword;

    /** Avatar */
    @Transient
    private Link avatar;

    /** External account indicator. */
    @Attribute(name = "portalPersonExternal")
    private Boolean external;

    /** Account validity date. */
    @Attribute(name = "portalPersonValidity")
    private Date validity;

    /** Last connection date. */
    @Attribute(name = "portalPersonLastConnection")
    private Date lastConnection;


    /**
     * Constructor.
     */
    public PersonImpl() {
        super();
        this.profiles = new ArrayList<Name>();
        this.avatar = new Link(StringUtils.EMPTY, false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Name getDn() {
        return this.dn;
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
        return this.cn;
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
    public String getSn() {
        return this.sn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setSn(String sn) {
        this.sn = sn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return this.displayName;
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
    public String getGivenName() {
        return this.givenName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getMail() {
        return this.mail;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setMail(String mail) {
        this.mail = mail;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return this.title;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getUid() {
        return this.uid;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Name> getProfiles() {
        return this.profiles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setProfiles(List<Name> profiles) {
        this.profiles = profiles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Link getAvatar() {
        return this.avatar;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAvatar(Link avatar) {
        this.avatar = avatar;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getExternal() {
        return this.external;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setExternal(Boolean external) {
        this.external = external;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Date getValidity() {
        return this.validity;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidity(Date validity) {
        this.validity = validity;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastConnection() {
        return this.lastConnection;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Name buildBaseDn() {
        return LdapNameBuilder.newInstance(System.getProperty("ldap.base")).add("ou=users").build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Name buildDn(String uid)  {
    	return LdapNameBuilder.newInstance(buildBaseDn()).add("uid=" + uid).build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.dn.toString();
    }

}
