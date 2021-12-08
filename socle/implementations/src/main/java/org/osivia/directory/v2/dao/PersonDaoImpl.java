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

import java.util.Date;
import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

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
import org.springframework.ldap.filter.LessThanOrEqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

/**
 * @author Loïc Billon
 *
 */
@Repository("personDao")
public class PersonDaoImpl implements PersonDao {


	@Autowired
	protected Person sample;
	
	
	@Autowired
	@Qualifier("ldapTemplate")
	protected LdapTemplate template;
	
	@Autowired
	@Qualifier("authenticateLdapTemplate")	
	protected LdapTemplate authenticateLdapTemplate;	
	
	
	private SearchControls controls;
		
	@Override
	@Cacheable(key = "#dn", value = { "personByDnCache" })
	public Person getPerson(Name dn) throws NameNotFoundException {
				
		Person person = template.findByDn(dn, sample.getClass());
		return person;

	}


	@Override
	public Person getPersonNoCache(Name dn) {

		Person person = template.findByDn(dn, sample.getClass());
		return person;
	}
	

	@Override
	public List<Person> findByCriteria(Person p, boolean connectedOnly) {

		OrFilter filter = MappingHelper.generateOrFilter(p);
		
		if(connectedOnly) {
			String field = MappingHelper.getLdapFieldName(sample, "lastConnection");
			LikeFilter dateFilter = new LikeFilter(field, "*");
			
			AndFilter connectedFilter = new AndFilter();
			connectedFilter.and(dateFilter);
			connectedFilter.and(filter);
			
			return (List<Person>) template.find(sample.buildBaseDn(), connectedFilter, getSearchControls() , sample.getClass());
		}
		else {
			return (List<Person>) template.find(sample.buildBaseDn(), filter, getSearchControls() , sample.getClass());
		}
		
	}
	
	@Override
	public List<Person> findByNoConnectionDate(Person p) {
		
		
		String lastCoField = MappingHelper.getLdapFieldName(sample, "lastConnection");
		NotFilter dateFilter = new NotFilter(new LikeFilter(lastCoField, "*"));
		
		String profilesField = MappingHelper.getLdapFieldName(sample, "profiles");
		NotFilter noProfileFilter = new NotFilter(new LikeFilter(profilesField, "*"));		
		
		AndFilter filter = MappingHelper.generateAndFilter(p);
		filter.and(dateFilter);
		filter.and(noProfileFilter);
		
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
	
	

	@Override
	public void create(Person p) {
		
		p.setDn(sample.buildDn(p.getUid()));
		
		template.create(p);
		
	}


	@Override
	@CacheEvict(value = "personByDnCache", key = "#p.dn")
	public void update(Person p) {
		template.update(p);
		
	}
	

	@Override
	public boolean verifyPassword(String uid, String currentPassword) {
		
		Name dn = sample.buildDn(uid);;
		String personFilter = MappingHelper.getBasicFilter(sample).encode();
		return authenticateLdapTemplate.authenticate(dn, personFilter, currentPassword);
		
	}	
	

	@Override
	public void updatePassword(Person p, String newPassword) {

		ModificationItem[] mods = new ModificationItem[1];
		Attribute userPasswordAttribute = new BasicAttribute ( MappingHelper.getLdapFieldName(p, "userPassword"), newPassword ); ;
		mods[ 0 ] = new ModificationItem ( DirContext.REPLACE_ATTRIBUTE, userPasswordAttribute  );
		template.modifyAttributes(p.getDn(), mods);
	}	
	
	
	/**
	 * Query optimization
	 * @return
	 */
	protected SearchControls getSearchControls() {
		
		if(controls == null) {
			controls = new SearchControls();
			
			controls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			
			if(System.getProperty("ldap.searchperson.maxTime") != null) {
				int timeout = Integer.parseInt(System.getProperty("ldap.searchperson.maxTime"));
				controls.setTimeLimit(timeout);
			}
			else {
				controls.setTimeLimit(5000);
			}
			
			if(System.getProperty("ldap.searchperson.maxResults") != null) {
				int maxResults = Integer.parseInt(System.getProperty("ldap.searchperson.maxResults"));
				controls.setCountLimit(maxResults);
			}
			else {
				controls.setCountLimit(500);
			}
		}
		
		return controls;
	}


	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.dao.PersonDao#delete(org.osivia.portal.api.directory.v2.model.Person)
	 */
	@Override
	@CacheEvict(value = "personByDnCache", key = "#p.dn")
	public void delete(Person p) {
		template.delete(p);
		
	}

	
}
