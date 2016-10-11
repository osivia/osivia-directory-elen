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

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.osivia.directory.v2.MappingHelper;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
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
	protected LdapTemplate template;
	
	private SearchControls controls;
		
	
	@Override
	@Cacheable(key = "#dn", value = { "personByDnCache" })
	public Person getPerson(Name dn) throws NameNotFoundException {
				
		Person person = template.findByDn(dn, sample.getClass());
		return person;

	}
	

	@Override
	public List<Person> findByCriteria(Person p) {

		OrFilter filter = MappingHelper.generateOrFilter(p);
				
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
		return template.authenticate(dn, personFilter, currentPassword);
		
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
				controls.setCountLimit(100);
			}
		}
		
		return controls;
	}
	
	
}
