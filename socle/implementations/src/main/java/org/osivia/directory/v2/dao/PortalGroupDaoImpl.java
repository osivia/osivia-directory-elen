package org.osivia.directory.v2.dao;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.MappingHelper;
import org.osivia.directory.v2.model.PortalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository("portalGroupDao")
public class PortalGroupDaoImpl extends GroupDaoImpl implements PortalGroupDao {

    /** LDAP template. */
    @Autowired
    protected LdapTemplate template;

    /** Group sample. */
    @Autowired
    @Qualifier("portalGroup")  
    private PortalGroup sample;
    
    public PortalGroupDaoImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortalGroup get(Name dn) {
        return this.template.findByDn(dn, this.sample.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PortalGroup> find(PortalGroup criteria) {
        // LDAP query
        LdapQueryBuilder query = LdapQueryBuilder.query();
        query.base(System.getProperty("ldap.base"));

        // Filter
        Filter filter = MappingHelper.generateAndFilter(criteria);
        query.filter(filter);

        // Search results
        List<? extends PortalGroup> results = this.template.find(query, this.sample.getClass());
        return new ArrayList<PortalGroup>(results);
    }

}
