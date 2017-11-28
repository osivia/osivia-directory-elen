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

@Component("portalGroup")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"portalProfile"})
public final class PortalGroupImpl implements PortalGroup, Serializable {

    /** Default serial version identifier. */
    private static final long serialVersionUID = 1L;

    /** Group DN. */
    @Id
    private Name dn;

    /** Group CN. */
    @Attribute
    private String cn;

    @Attribute(name = "portalDisplayName")
    private String displayName;

    @Attribute
    private String description;

    /** Group member names. */
    @Attribute(name = "uniqueMember")
    private List<Name> members;

    public PortalGroupImpl() {
        super();
    }

    /**
     * @return the dn
     */
    @Override
    public Name getDn() {
        return dn;
    }

    /**
     * @param dn
     *            the dn to set
     */
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
     * @param cn
     *            the cn to set
     */
    @Override
    public void setCn(String cn) {
        this.cn = cn;
    }

    /**
     * @return the displayName
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the members
     */
    @Override
    public List<Name> getMembers() {
        return members;
    }

    /**
     * @param members
     *            the members to set
     */
    @Override
    public void setMembers(List<Name> members) {
        this.members = members;
    }

}
