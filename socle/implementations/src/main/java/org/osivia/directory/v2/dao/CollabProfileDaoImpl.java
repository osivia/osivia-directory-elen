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

import org.osivia.directory.v2.MappingHelper;
import org.osivia.directory.v2.model.CollabProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Repository;

/**
 * @author Loïc Billon
 *
 */
@Repository("collabProfileDao")
public class CollabProfileDaoImpl implements CollabProfileDao {

	
	@Autowired
	private LdapTemplate template;
	
	
	@Autowired
	private CollabProfile sample;	

	
	@Override
	public CollabProfile findByDn(Name dn) {
		
		return template.findByDn(dn, sample.getClass());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CollabProfile> findByCriteria(CollabProfile profile) {
		
		LdapQueryBuilder query = LdapQueryBuilder.query();
		
		AndFilter filter = MappingHelper.generateAndFilter(profile);
		
		query.filter(filter);
		
		return (List<CollabProfile>) template.find(query, sample.getClass());
	}
	
	@Override
	public void create(CollabProfile profile) {
		template.create(profile);
	}
	
	@Override
	public void update(CollabProfile profile) {
		template.update(profile);
	}
	
	@Override
	public void delete(CollabProfile profile) {
		template.delete(profile);
	}
}
