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

import java.util.List;

import javax.naming.Name;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.directory.v2.dao.RoleDao;
import org.osivia.directory.v2.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Impl of the role service
 * @author Loïc Billon
 * @since 4.4
 */
@Service
public class RoleServiceImpl extends DirServiceImpl implements RoleService {

    /** Application context. */
    @Autowired
    protected ApplicationContext context;	
	
	@Autowired
	protected RoleDao dao;
	
	@Autowired
	protected Role sample;
	

    /** Log. */
    private final Log log = LogFactory.getLog(this.getClass());
	

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.RoleService#getEmptyRole()
	 */
	@Override
	public Role getEmptyRole() {
		return this.context.getBean(sample.getClass());
	}	
	
	public Role getRole(String cn) {
		
		Name dn = sample.buildDn(cn);;
		return getRole(dn);
		
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.RoleService#getRole(javax.naming.Name)
	 */
    @Override
    public Role getRole(Name dn) {

        Role r;
        try {
            r = dao.findByDn(dn);

        } catch (NameNotFoundException e) {
            this.log.warn("Role with dn " + dn + " not found");
            return null;
        }

        return r;
    }
	
	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.RoleService#hasRole(javax.naming.Name, java.lang.String)
	 */
	@Override
	public boolean hasRole(Name person, String cnRole) {

		Role role = getRole(cnRole);
		if(role != null) {
		    return role.getUniqueMember().contains(person);
		} else return false;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.RoleService#getAllRoles()
	 */
	@Override
	public List<Role> getAllRoles() {
		
		return dao.getAllRoles();
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.RoleService#create(java.lang.String)
	 */
	@Override
	public void create(String id, String description) {
		
		Role newRole = getEmptyRole();
		newRole.setCn(id);
		newRole.setDn(sample.buildDn(id));
		newRole.setDescription(description);
		
		dao.create(newRole);
		
	}



}
