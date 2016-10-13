/**
 * 
 */
package org.osivia.directory.v2.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.SearchControls;

import org.osivia.directory.v2.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Loïc Billon
 *
 */
@Repository
public class RoleDaoImpl implements RoleDao {


	@Autowired
	protected LdapTemplate template;
	
	@Autowired
	protected Role sample;

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.dao.RoleDao#findByDn(javax.naming.Name)
	 */
	@Override
	public Role findByDn(Name dn) {
		return template.findByDn(dn, sample.getClass());
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.dao.RoleDao#getAllRoles()
	 */
	@Override
	public List<Role> getAllRoles() {
		
		
		SearchControls controls = new SearchControls();
		return (List<Role>) template.findAll(sample.buildBaseDn(), controls, sample.getClass());
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.dao.RoleDao#create(javax.naming.Name)
	 */
	@Override
	public void create(Role role) {
		template.create(role);
		
	}
	
	
	
}
