package org.osivia.directory.v2.dao;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.MappingHelper;
import org.osivia.directory.v2.model.PortalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

/**
 * Portal group DAO implementation.
 * 
 * @author Cédric Krommenhoek
 * @see PortalGroupDao
 */
@Repository
public class PortalGroupDaoImpl implements PortalGroupDao {

    /** LDAP template. */
    @Autowired
    protected LdapTemplate template;

    /** Portal group sample. */
    @Autowired
    private PortalGroup sample;
    

    /**
     * Constructor.
     */
    public PortalGroupDaoImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Name getBaseDn() {
        String ldapBase = System.getProperty("ldap.base");
        LdapNameBuilder builder = LdapNameBuilder.newInstance(ldapBase);
        builder.add("ou", "groups");
        builder.add("ou", "profiles");
        return builder.build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PortalGroup find(Name dn) {
        return this.template.findByDn(dn, PortalGroup.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PortalGroup> find(PortalGroup criteria) {
        // LDAP query
        LdapQueryBuilder query = LdapQueryBuilder.query();
        query.base(this.getBaseDn());

        // Filter
        Filter filter = MappingHelper.generateAndFilter(criteria);
        query.filter(filter);

        // Search results
        List<? extends PortalGroup> results = this.template.find(query, this.sample.getClass());
        return new ArrayList<PortalGroup>(results);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(PortalGroup portalGroup) {
        this.template.create(portalGroup);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PortalGroup portalGroup) {
        this.template.update(portalGroup);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalGroup portalGroup) {
        this.template.delete(portalGroup);
    }

}
