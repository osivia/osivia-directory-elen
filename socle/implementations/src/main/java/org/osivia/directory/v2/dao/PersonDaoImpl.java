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
package org.osivia.directory.v2.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.MappingHelper;
import org.osivia.directory.v2.model.converter.DateToGeneralizedTime;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.BinaryLogicalFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.LessThanOrEqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.stereotype.Repository;

/**
 * Person DAO implementation.
 *
 * @author Loïc Billon
 * @see PersonDao
 */
@Repository
public class PersonDaoImpl implements PersonDao {

    /** Person sample. */
    @Autowired
    protected Person sample;

    /** LDAP template. */
    @Autowired
    @Qualifier("ldapTemplate")
    protected LdapTemplate template;

    /** Authenticate LDAP template. */
    @Autowired
    @Qualifier("authenticateLdapTemplate")
    private LdapTemplate authenticateLdapTemplate;

    /** Search controls. */
    private SearchControls searchControls;


    /**
     * Constructor.
     */
    public PersonDaoImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#dn", value = {"personByDnCache"})
    public Person getPerson(Name dn) throws NameNotFoundException {
        return this.template.findByDn(dn, this.sample.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Person getPersonNoCache(Name dn) {
        return this.template.findByDn(dn, this.sample.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Person> findByCriteria(Person criteria) {
        // Global filter
        BinaryLogicalFilter globalFilter = new AndFilter();

        // Quick search LDAP attribute names
        List<String> quickSearchAttributes = this.getQuickSearchAttributes();
        // Quick search LDAP query filters
        List<Filter> quickSearchFilters = new ArrayList<>(quickSearchAttributes.size());

        // Person declared fields
        Field[] fields = criteria.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                // LDAP attribute name
                String attribute = this.getAttribute(criteria, field);

                if (StringUtils.isNotEmpty(attribute)) {
                    // LDAP query filter
                    Filter filter = this.getQueryFilter(criteria, field, attribute);

                    if (filter != null) {
                        if (quickSearchAttributes.contains(attribute)) {
                            quickSearchFilters.add(filter);
                        } else {
                            globalFilter.append(filter);
                        }
                    }
                }
            }
        }

        if (!quickSearchFilters.isEmpty()) {
            BinaryLogicalFilter quickSearchFilter = new OrFilter();
            quickSearchFilter.appendAll(quickSearchFilters);

            globalFilter.append(quickSearchFilter);
        }


		return (List<Person>) template.find(sample.buildBaseDn(), globalFilter, getSearchControls() , sample.getClass());

	}

    /**
     * Get quick search LDAP attribute names.
     *
     * @return LDAP attribute names
     */
    protected List<String> getQuickSearchAttributes() {
        return Arrays.asList(new String[]{"uid", "cn", "sn", "mail", "displayName", "givenName"});
    }


	@Override
	public List<Person> findByNoConnectionDate(Person p) {


		String field = MappingHelper.getLdapFieldName(sample, "lastConnection");
		NotFilter dateFilter = new NotFilter(new LikeFilter(field, "*"));

		AndFilter filter = MappingHelper.generateAndFilter(p);
		filter.and(dateFilter);

		return (List<Person>) template.find(sample.buildBaseDn(), filter, getSearchControls() , sample.getClass());

	}



	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.dao.PersonDao#findByValidityDate(java.util.Date)
	 */
	@Override
	public List<Person> findByValidityDate(Date d) {

		String field = MappingHelper.getLdapFieldName(sample, "validity");

		DateToGeneralizedTime converter = new DateToGeneralizedTime();

		LessThanOrEqualsFilter filter = new LessThanOrEqualsFilter(field, converter.convert(d));

		return (List<Person>) template.find(sample.buildBaseDn(), filter, getSearchControls() , sample.getClass());

	}

	

    /**
     * Get LDAP attribute name.
     *
     * @param criteria search criteria
     * @param field criteria field
     * @return LDAP attribute name
     */
    protected String getAttribute(Person criteria, Field field) {
        // LDAP attribute annotation
        Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
        // LDAP transient annotation
        Transient transientAnnotation = field.getAnnotation(Transient.class);

        // LDAP attribute name
        String attribute;
        if ((attributeAnnotation == null) || (transientAnnotation != null)) {
            attribute = null;
        } else {
            attribute = StringUtils.defaultIfEmpty(attributeAnnotation.name(), field.getName());
        }

        return attribute;
    }


    /**
     * Get LDAP query filter.
     *
     * @param criteria LDAP search criteria
     * @param field criteria field
     * @param attribute LDAP attribute name
     * @return LDAP query filter
     */
    protected Filter getQueryFilter(Person criteria, Field field, String attribute) {
        // LDAP attribute value
        Object value;
        try {
            value = PropertyUtils.getProperty(criteria, field.getName());
        } catch (ReflectiveOperationException e) {
            value = null;
        }

        // Filter
        Filter filter;
        if (value == null) {
            filter = null;
        } else if (value instanceof Boolean) {
            Boolean booleanValue = (Boolean) value;
            filter = new LikeFilter(attribute, BooleanUtils.toString(booleanValue, "TRUE", "FALSE", null));
        } else if (value instanceof Collection) {
            // Sub-query values
            Collection<?> subQueryValues = (Collection<?>) value;

            // Sub-query filters
            Collection<Filter> subQueryFilters = new ArrayList<Filter>(subQueryValues.size());
            for (Object subQueryValue : subQueryValues) {
                // Sub-query filter
                Filter subQueryFilter = new LikeFilter(attribute, subQueryValue.toString());
                subQueryFilters.add(subQueryFilter);
            }

            if (subQueryFilters.isEmpty()) {
                filter = null;
            } else {
                // "OR" filter
                BinaryLogicalFilter orFilter = new OrFilter();
                orFilter.appendAll(subQueryFilters);

                filter = orFilter;
            }
        } else {
            filter = new LikeFilter(attribute, value.toString());
        }

        return filter;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Person person) {
        Name dn = this.sample.buildDn(person.getUid());
        person.setDn(dn);
        this.template.create(person);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(value = "personByDnCache", key = "#p.dn")
    public void update(Person p) {
        this.template.update(p);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyPassword(String uid, String currentPassword) {
        Name dn = this.sample.buildDn(uid);
        String personFilter = MappingHelper.getBasicFilter(this.sample).encode();
        return this.authenticateLdapTemplate.authenticate(dn, personFilter, currentPassword);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePassword(Person person, String newPassword) {
        ModificationItem[] modifications = new ModificationItem[1];
        BasicAttribute userPasswordAttribute = new BasicAttribute(MappingHelper.getLdapFieldName(person, "userPassword"), newPassword);
        modifications[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, userPasswordAttribute);
        this.template.modifyAttributes(person.getDn(), modifications);
    }


    /**
     * Get search controls.
     *
     * @return search controls
     */
    protected SearchControls getSearchControls() {
        if (this.searchControls == null) {
            this.searchControls = new SearchControls();

            this.searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

            if (System.getProperty("ldap.searchperson.maxTime") != null) {
                int timeout = Integer.parseInt(System.getProperty("ldap.searchperson.maxTime"));
                this.searchControls.setTimeLimit(timeout);
            } else {
                this.searchControls.setTimeLimit(5000);
            }

            if (System.getProperty("ldap.searchperson.maxResults") != null) {
                int maxResults = Integer.parseInt(System.getProperty("ldap.searchperson.maxResults"));
                this.searchControls.setCountLimit(maxResults);
            } else {
                this.searchControls.setCountLimit(500);
            }
        }

        return this.searchControls;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(value = "personByDnCache", key = "#p.dn")
    public void delete(Person p) {
        this.template.delete(p);
    }

}
