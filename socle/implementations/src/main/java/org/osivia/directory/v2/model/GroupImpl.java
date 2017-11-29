package org.osivia.directory.v2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.osivia.portal.api.directory.v2.model.Group;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.stereotype.Component;

/**
 * LDAP group implementation.
 * 
 * @author Cédric Krommenhoek
 * @see Group
 * @see Serializable
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"groupOfUniqueNames"})
public final class GroupImpl implements Group, Serializable {

    /** Default serial version identifier. */
    private static final long serialVersionUID = 1L;


    /** Group DN. */
    @Id
    private Name dn;

    /** Group CN. */
    @Attribute
    private String cn;

    /** Group member names. */
    @Attribute(name = "uniqueMember")
    private List<Name> members;


    /**
     * Constructor.
     */
    public GroupImpl() {
        super();
        this.members = new ArrayList<Name>();
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
    public List<Name> getMembers() {
        return this.members;
    }


    /**
     * {@inheritDoc}
     */
    public void setMembers(List<Name> members) {
        this.members = members;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.dn.toString();
    }

}
