package org.osivia.directory.v2.dao;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.directory.SearchControls;

import org.apache.commons.lang.math.NumberUtils;
import org.osivia.directory.v2.MappingHelper;
import org.osivia.directory.v2.model.PortalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.Filter;
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
    

    /** Search controls. */
    private SearchControls searchControls;


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
    public PortalGroup find(Name dn) throws NameNotFoundException {
        return this.template.findByDn(dn, this.sample.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PortalGroup> find(PortalGroup criteria) {
        // Filter
        Filter filter = MappingHelper.generateOrFilter(criteria);

        // Search results
        List<? extends PortalGroup> results = this.template.find(this.getBaseDn(), filter, this.getSearchControls(), this.sample.getClass());
        return new ArrayList<PortalGroup>(results);
    }


    /**
     * Get search controls.
     * 
     * @return search controls
     */
    protected SearchControls getSearchControls() {
        if (this.searchControls == null) {
            this.searchControls = new SearchControls();

            // Time limit
            int timeLimit = NumberUtils.toInt(System.getProperty(SEARCH_TIME_LIMIT_PROPERTY));
            if (timeLimit < 1) {
                timeLimit = DEFAULT_SEARCH_TIME_LIMIT;
            }
            this.searchControls.setTimeLimit(timeLimit);

            // Count limit
            long countLimit = NumberUtils.toLong(System.getProperty(SEARCH_COUNT_LIMIT_PROPERTY));
            if (countLimit < 1) {
                countLimit = DEFAULT_SEARCH_COUNT_LIMIT;
            }
            this.searchControls.setCountLimit(countLimit);
        }

        return this.searchControls;
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
