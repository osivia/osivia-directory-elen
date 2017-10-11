package org.osivia.directory.v2.dao;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.MappingHelper;
import org.osivia.portal.api.directory.v2.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Repository;

/**
 * LDAP group DAO implementation.
 *
 * @author Cédric Krommenhoek
 * @see GroupDao
 */
@Repository("groupDao")
public class GroupDaoImpl implements GroupDao {

    /** LDAP template. */
    @Autowired
    protected LdapTemplate template;

    /** Group sample. */
    @Autowired
    private Group sample;


    /**
     * Constructor.
     */
    public GroupDaoImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Group get(Name dn) {
        return this.template.findByDn(dn, this.sample.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> find(Group criteria) {
        // LDAP query
        LdapQueryBuilder query = LdapQueryBuilder.query();
        query.base(System.getProperty("ldap.base"));

        // Filter
        Filter filter = MappingHelper.generateAndFilter(criteria);
        query.filter(filter);

        // Search results
        List<? extends Group> results = this.template.find(query, this.sample.getClass());
        return new ArrayList<Group>(results);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Name dn) {
        Group group = this.get(dn);
        this.template.delete(group);
    }

}
