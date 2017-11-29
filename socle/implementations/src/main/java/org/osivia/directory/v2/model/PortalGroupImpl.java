package org.osivia.directory.v2.model;

import java.io.Serializable;
import java.util.List;

import javax.naming.Name;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.stereotype.Component;

/**
 * Portal group implementation.
 * 
 * @author Cédric Krommenhoek
 * @see PortalGroup
 * @see Serializable
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"portalProfile"})
public final class PortalGroupImpl implements PortalGroup, Serializable {

    /** Default serial version identifier. */
    private static final long serialVersionUID = 1L;


    /** Portal group DN. */
    @Id
    private Name dn;

    /** Portal group CN. */
    @Attribute
    private String cn;

    /** Portal group display name. */
    @Attribute(name = "portalDisplayName")
    private String displayName;

    /** Portal group description. */
    @Attribute
    private String description;

    /** Portal group members. */
    @Attribute(name = "uniqueMember")
    private List<Name> members;


    /**
     * Constructor.
     */
    public PortalGroupImpl() {
        super();
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
    public String getDescription() {
        return this.description;
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
    public List<Name> getMembers() {
        return this.members;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setMembers(List<Name> members) {
        this.members = members;
    }

}
