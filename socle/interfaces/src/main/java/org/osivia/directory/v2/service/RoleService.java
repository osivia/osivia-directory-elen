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

import org.osivia.directory.v2.model.Role;
import org.osivia.portal.api.directory.v2.IDirService;

/**
 * Service used to manager applicative roles
 * @author Loïc Billon
 * @since 4.4
 */
public interface RoleService extends IDirService {

    /**
     * Get empty role for searching.
     *
     * @return empty role
     */
	public Role getEmptyRole();


    /**
     * Get a role
     *
     * @param cn 
     * @return role
     */
	public Role getRole(String cn);


    /**
     * Get a role
     *
     * @param dn 
     * @return role
     */
	public Role getRole(Name dn);
	
	/**
	 * Get all roles
	 * @return all applicative roles
	 */
	public List<Role> getAllRoles();
	
	/**
	 * Check is a user has a role
	 * @param person
	 * @param cnRole the name of the role
	 * @return true or false
	 */
	public boolean hasRole(Name person, String cnRole);


	/**
	 * Create a role
	 * @param id
	 */
	public void create(String id, String description);
	
	
}
