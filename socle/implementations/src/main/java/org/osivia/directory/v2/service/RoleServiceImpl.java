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
package org.osivia.directory.v2.service;

import javax.naming.Name;

import org.osivia.directory.v2.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

/**
 * Impl of the role service
 * @author Loïc Billon
 * @since 4.4
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	protected LdapTemplate template;
	
	@Autowired
	protected Role sample;
	
	public Role getRole(String cn) {
		
		Name dn = sample.buildDn(cn);;
		return template.findByDn(dn, sample.getClass());
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.RoleService#hasRole(javax.naming.Name, java.lang.String)
	 */
	@Override
	public boolean hasRole(Name person, String cnRole) {

		Role role = getRole(cnRole);
				
		return role.getUniqueMember().contains(person);
	}

}
