/**
 * 
 */
package org.osivia.directory.v2.dao;

import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.model.Role;

/**
 * @author Loïc Billon
 *
 */
public interface RoleDao {

	/**
	 * Find a role by its DN
	 * @param dn identifier
	 * @return the tole
	 */
	Role findByDn(Name dn);

	/**
	 * Get all roles
	 * @return all roles
	 */
	List<Role> getAllRoles();

	/**
	 * Create a role
	 * @param role
	 */
	void create(Role role);

	
}
